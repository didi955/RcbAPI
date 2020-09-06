package fr.rushcubeland.rcbapi.queue;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.data.redis.RedisAccess;
import fr.rushcubeland.rcbapi.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class Queue {

    private int tid;

    public void checkForJoinQueue(Player player, QueueUnit queueUnit){
        if(queueUnit.getPlayers().size() >= queueUnit.getMaxPlayer()){
            return;
        }
        if(playerIsInTheQueue(player, queueUnit)){
            player.sendMessage("§cVous etes déjà dans cette file d'attente !");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.NEUTRAL, 0F, 0F);
        }
        else if(playerIsInAQueue(player)){
            leaveQueue(player, getCurrentQueue(player));
            joinQueue(player, queueUnit);
        }
        else
        {
            joinQueue(player, queueUnit);
        }

    }

    public void joinQueue(Player player, QueueUnit queueUnit){
        queueUnit.getPlayers().add(player);

        // MESSAGES + SOUNDS
        if(hasPriorityInQueues(player)){
            Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
                final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
                final String key = "account:" + player.getUniqueId().toString();
                final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

                final Account account = accountRBucket.get();
                int count = 0;
                for(Player pls : queueUnit.getPlayers()){
                    final String key2 = "account:" + pls.getUniqueId().toString();
                    final RBucket<Account> accountRBucket2 = redissonClient.getBucket(key);

                    final Account account2 = accountRBucket2.get();

                    if(account2.getRank().getPower() > account.getRank().getPower()){
                        queueUnit.getPlayers().remove(player);
                        queueUnit.getPlayers().add(count, player);
                        break;
                    }
                    count = count+1;
                }

                if(account.getRank().getPower() <= 38){
                    player.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + queueUnit.getName() + ", §6Priorité: §cTrès Élevée");
                }
                else
                {
                    player.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + queueUnit.getName() + ", §6Priorité: §cÉlevée");
                }
            });

        }
        else {
            player.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + queueUnit.getName() + ", §6Priorité: §fNormale");
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0F, 0F);

        if(queueUnit.getPlayers().size() == 1){
            queueUnit.startTask();
        }
    }

    public void leaveQueue(Player player, QueueUnit queueUnit){
        if(playerIsInTheQueue(player, queueUnit)){
            queueUnit.getPlayers().remove(player);

            // MESSAGES + SOUNDS

        }
    }

    public boolean playerIsInAQueue(Player player){
        for(QueueUnit queueUnit : QueueUnit.values()){
            if(queueUnit.getPlayers().contains(player)){
                return true;
            }
        }
        return false;
    }

    public boolean playerIsInTheQueue(Player player, QueueUnit queueUnit){
        if(queueUnit.getPlayers().contains(player)){
            return true;
        }
        return false;
    }

    public QueueUnit getCurrentQueue(Player player){
        if(playerIsInAQueue(player)){
            for(QueueUnit queueUnit : QueueUnit.values()){
                queueUnit.getPlayers().contains(player);

                return queueUnit;
            }
        }
        return null;
    }

    public Integer getPositionInQueue(Player player, QueueUnit queueUnit){
        if(playerIsInTheQueue(player, queueUnit) && queueUnit.getPlayers().size() > 0){
            int counts = 1;
            int position;
            for(Player pls : queueUnit.getPlayers()){
                counts = counts+1;
                if(player == pls){
                    position = counts;
                    return position;
                }
            }
        }
        return null;
    }

    public boolean hasPriorityInQueues(Player player){

        Boolean[] value = new Boolean[1];

        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "account:" + player.getUniqueId().toString();
            final RBucket<Account> accountRBucket = redissonClient.getBucket(key);
            final Account account = accountRBucket.get();

            RankUnit playerRank = account.getRank();

            value[0] = playerRank.getPower() <= 45 && account.rankIsValid();

        });
        return value[0];
    }
}
