package fr.rushcubeland.rcbapi.account;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.database.DatabaseManager;
import fr.rushcubeland.rcbapi.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public enum RankUnit {

    JOUEUR("Joueur", 50, "" + ChatColor.GRAY, ChatColor.GRAY),
    VIP("VIP", 45,  ChatColor.YELLOW + "[VIP] " + ChatColor.WHITE, ChatColor.YELLOW),
    VIPP("VIP+", 40,    ChatColor.DARK_PURPLE + "[VIP+] " + ChatColor.WHITE, ChatColor.DARK_PURPLE),
    INFLUENCEUR("Influenceur", 38, ChatColor.AQUA + "[Influenceur] " + ChatColor.WHITE, ChatColor.AQUA),
    INFLUENCEUSE("Influenceuse", 37, ChatColor.AQUA + "[Influenceuse] " + ChatColor.WHITE, ChatColor.AQUA),
    AMI("Ami", 35, ChatColor.DARK_AQUA + "[Ami] " + ChatColor.WHITE, ChatColor.DARK_AQUA),
    AMIE("Amie", 30, ChatColor.DARK_AQUA + "[Amie] " + ChatColor.WHITE, ChatColor.DARK_AQUA),
    COPINE("Copine", 25, ChatColor.LIGHT_PURPLE + "[♥] " + ChatColor.WHITE, ChatColor.LIGHT_PURPLE),
    ASSISTANT("Assistant", 20, ChatColor.GREEN +"[Assistant] " + ChatColor.WHITE, ChatColor.GREEN),
    ASSISTANTE("Assistante", 15, ChatColor.GREEN + "[Assistante] " + ChatColor.WHITE, ChatColor.GREEN),
    DEVELOPPEUR("Développeur", 10, ChatColor.BLUE + "[Développeur] " + ChatColor.WHITE, ChatColor.BLUE),
    DEVELOPPEUSE("Développeuse", 9, ChatColor.BLUE + "[Développeuse] " + ChatColor.WHITE, ChatColor.BLUE),
    MODERATEUR("Modérateur", 8, ChatColor.GOLD + "[Modérateur] " + ChatColor.WHITE, ChatColor.GOLD),
    MODERATRICE("Modératrice", 7, ChatColor.GOLD + "[Modératrice] " + ChatColor.WHITE, ChatColor.GOLD),
    RESPMOD("RespMod", 5, ChatColor.GOLD + "[RespMod] " + ChatColor.WHITE, ChatColor.GOLD),
    ADMINISTRATEUR("Admin", 0, ChatColor.DARK_RED + "[Admin] " + ChatColor.WHITE, ChatColor.DARK_RED);

    private String name;
    private int power;
    private String prefix;
    private ChatColor color;
    private ArrayList<String> permissions = new ArrayList<>();


    RankUnit(String name, int power, String prefix, ChatColor color) {
        this.name = name;
        this.power = power;
        this.prefix = prefix;
        this.color = color;
    }

    public static RankUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(RankUnit.JOUEUR);
    }

    public static RankUnit getByPower(int power){
        return Arrays.stream(values()).filter(r -> r.getPower() == power).findAny().orElse(RankUnit.JOUEUR);
    }

    public String getName() {
        return name;
    }

    public Integer getPower() {
        return power;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColoredName(){
        return color + name;
    }


    public void addPermission(String permission){
        if(!this.permissions.contains(permission)){
            this.permissions.add(permission);
        }
    }

    public void removePermission(String permission){
        if(this.permissions.contains(permission)){
            this.permissions.remove(permission);
        }
    }

    public boolean hasPermission(String permission){
        if(this.permissions.contains(permission)){
            return true;
        }
        return false;
    }

    public ArrayList<String> getPermissions(){
        return this.permissions;
    }

    private ArrayList<String> getDataofRankPermissionsFromMySQL(){
        ArrayList<String> dataRankperms = new ArrayList<>();
        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM rank_permissions WHERE grade='%s'",
                    getName()), rs -> {

                try {
                    while(rs.next()){

                        dataRankperms.add(rs.getString("permission"));

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataRankperms;
    }

    private void sendDataofRankPermissionsToMySQL(){
        if(!getPermissions().isEmpty()){
            for(String perms : getPermissions()){
                try {

                    MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO rank_permissions (grade, permission) VALUES ('%s', '%s')",
                            getName(), perms));

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    private void sendTaskNoAsync(){
        sendDataofRankPermissionsToMySQL();
    }

    private void getTaskAsync(){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            ArrayList<String> dataRankperms = getDataofRankPermissionsFromMySQL();
            for(String perm : dataRankperms){
                addPermission(perm);
            }
        });
    }

    public void onDisableServer(){
        sendTaskNoAsync();
    }

    public void onEnableServer(){
        getTaskAsync();
    }
}