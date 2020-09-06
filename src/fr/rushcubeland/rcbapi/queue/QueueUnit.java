package fr.rushcubeland.rcbapi.queue;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.data.redis.RedisAccess;
import fr.rushcubeland.rcbapi.network.Network;
import fr.rushcubeland.rcbapi.network.ServerGroup;
import fr.rushcubeland.rcbapi.network.ServerUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public enum QueueUnit {

    Minigame("Minigame", ServerGroup.Minigame, -1);

    private String name;
    private ArrayList<String> serversInGroup = new ArrayList<>();
    private int playersMax;
    private ServerGroup serverGroup;
    private int tid;
    private static QueueUnit instance;

    private ArrayList<Player> playersInQueue = new ArrayList<>();

    QueueUnit(String name, ServerGroup serverGroup, Integer playersMax){
        this.name = name;
        this.playersMax = playersMax;
        this.serverGroup = serverGroup;
    }

    public static QueueUnit getInstance() {
        return instance;
    }

    public static Optional<QueueUnit> getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny();
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayer(){
        return playersMax;
    }

    public ArrayList<Player> getPlayers(){
        return playersInQueue;
    }

    public ArrayList<String> getTargetServers(){
        return serversInGroup;
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public Integer getTaskID(){
        return tid;
    }

    public void startTask(){
        tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(RcbAPI.getInstance(), () -> {
            for(Player pls : getPlayers()){
                if(RcbAPI.getInstance().getQueue().hasPriorityInQueues(pls)){
                    Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {

                        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
                        final String key = "account:" + pls.getUniqueId().toString();
                        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

                        final Account account = accountRBucket.get();

                        if(account.getRank().getPower() <= 10){
                            pls.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + QueueUnit.getInstance().getName()  + ", §6Priorité: §cTrès Élevée");
                        }
                        else
                        {
                            pls.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + QueueUnit.getInstance().getName() + ", §6Priorité: §cÉlevée");
                        }

                    });
                }
                else {
                    pls.sendMessage("§e[File d'attente] §6Vous avez rejoin la file pour le jeu §c" + QueueUnit.getInstance().getName()  + ", §6Priorité: §fNormale");
                }
            }
            if(getPlayers().size() >= 1){
                Player player = getPlayers().get(0);
                ServerUnit bestTargetServer = Network.getBestServerForQueue(player, getServerGroup());
                Network.sendPlayerToServer(player, bestTargetServer);
            }
        }, 0L, 20L);
    }

    public void stopTask(){
        RcbAPI.getInstance().getServer().getScheduler().cancelTask(getTaskID());
    }

}
