package fr.rushcubeland.rcbapi;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.rushcubeland.rcbapi.account.Account;
import fr.rushcubeland.rcbapi.account.RankUnit;
import fr.rushcubeland.rcbapi.commands.NpcCommand;
import fr.rushcubeland.rcbapi.database.DatabaseManager;
import fr.rushcubeland.rcbapi.database.MySQL;
import fr.rushcubeland.rcbapi.files.DataManager;
import fr.rushcubeland.rcbapi.listeners.PlayerJoin;
import fr.rushcubeland.rcbapi.listeners.PlayerQuit;
import fr.rushcubeland.rcbapi.network.Network;
import fr.rushcubeland.rcbapi.network.Server;
import fr.rushcubeland.rcbapi.queue.Queue;
import fr.rushcubeland.rcbapi.tools.PacketReader;
import fr.rushcubeland.rcbapi.tools.cameras.CameraManager;
import fr.rushcubeland.rcbapi.tools.npc.NPC;
import fr.rushcubeland.rcbapi.tools.scoreboard.ScoreboardSign;
import fr.rushcubeland.rcbapi.tools.tablist.Tablist;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class RcbAPI extends JavaPlugin {

    private static RcbAPI instance;

    public static NPC npc = null;

    private CameraManager cameraManager;
    private Queue queue;
    private Tablist tablist;

    public static DataManager data;

    private List<Account> accounts;

    public Map<Player, ScoreboardSign> boards = new HashMap<>();

    public static RcbAPI getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("========================");
        getLogger().info("API initialization in progress...");
        getLogger().info("========================");

        registerListeners();
        registerCommands();

        data = new DataManager(this);
        if(data.getConfig().contains("data"))
            loadNPC();

        Server.initServerGroup();
        Tablist tablist = new Tablist(this);
        tablist.initTabListTeam();
        this.tablist = tablist;

        DatabaseManager.initAllDatabaseConnections();

        MySQL.createTables();

        accounts = new ArrayList<>();

        initAllRankPermissions();
        //BattlePassUnit.getCurrentBattlePass().onEnableServer();

        Network.startTaskUpdateSlotsServer();

        Bukkit.getLogger().info("RcbAPI enabled");

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "rcbproxy:main");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Network());

    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()){
            PacketReader reader = new PacketReader();
            reader.inject(player);
            for(EntityPlayer npc : NPC.getNPCs()){
                NPC.removeNPC(player, npc);
            }
        }
        closeAllRankPermissions();
        //BattlePassUnit.getCurrentBattlePass().onDisableServer();
        DatabaseManager.closeAllDatabaseConnection();

        Bukkit.getLogger().info("RcbAPI disabled");

    }

    private void initAllRankPermissions(){
        for(RankUnit rank  : RankUnit.values()){
            rank.onEnableServer();
        }
    }

    private void closeAllRankPermissions(){
        for(RankUnit rank  : RankUnit.values()){
            rank.onDisableServer();
        }
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
    }

    private void registerCommands(){
        getCommand("createnpc").setExecutor(new NpcCommand(this));
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> getAccount(Player player){
        return new ArrayList<>(accounts).stream().filter(a -> a.getUUID().equals(player.getUniqueId().toString())).findFirst();
    }

    public static FileConfiguration getData(){
        return data.getConfig();
    }

    public static void saveData(){
        data.saveConfig();
    }

    public void loadNPC(){
        FileConfiguration file = data.getConfig();
        data.getConfig().getConfigurationSection("data").getKeys(false).forEach(npc ->{
            Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world")),
                    file.getInt("data." + npc + ".x"), file.getInt("data." + npc + ".y"), file.getInt("data." + npc + ".z"));
            location.setPitch((float) file.getDouble("data." + npc + ".p"));
            location.setYaw((float) file.getDouble("data." + npc + ".yaw"));

            String name = file.getString("data." + npc + ".name");
            String displayname = file.getString("data." + npc + ".displayname");
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displayname);
            gameProfile.getProperties().put("textures", new Property("textures", file.getString("data." + npc + ".text"),
                    file.getString("data." + npc + ".signature")));

            NPC.loadNPC(location, gameProfile);
        });
    }

    public Tablist getTablist() {
        return tablist;
    }

    public CameraManager getCameraManager(){
        return cameraManager;
    }

    public Queue getQueue(){
        return queue;
    }
}
