package fr.rushcubeland.rcbapi.queue;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.account.Account;
import fr.rushcubeland.rcbapi.account.RankUnit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Queue {

    private int tid;

    public void checkForJoinQueue(Player player, QueueUnit queueUnit){
        if(queueUnit.getPlayers().size() >= queueUnit.getMaxPlayer()){
            return;
        }
        if(playerIsInTheQueue(player, queueUnit)){
            player.sendMessage("§cVous etes déjà dans cette file d'attente !");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, SoundCategory.NEUTRAL, 0F, 0F);
            return;
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
            int count = 0;
            for(Player pls : queueUnit.getPlayers()){
                if(RcbAPI.getInstance().getAccount(pls).get().getDataRank().getRank().getPower() > RcbAPI.getInstance().getAccount(player).get().getDataRank().getRank().getPower()){
                    queueUnit.getPlayers().remove(player);
                    queueUnit.getPlayers().add(count, player);
                    break;
                }
                count = count+1;
            }
            if(RcbAPI.getInstance().getAccount(player).get().getDataRank().getRank().getPower() <= 10){
                player.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + queueUnit.getName() + ", §6Priorité: §cTrès Élevée");
            }
            else
            {
                player.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + queueUnit.getName() + ", §6Priorité: §cÉlevée");
            }
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
        Account playerAccount = RcbAPI.getInstance().getAccount(player).get();
        RankUnit playerRank = playerAccount.getDataRank().getRank();
        return playerRank.getPower() <= 15 && playerAccount.getDataRank().isValid();
    }
}
