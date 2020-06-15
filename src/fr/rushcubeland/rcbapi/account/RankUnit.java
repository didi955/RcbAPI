package fr.rushcubeland.rcbapi.account;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.database.DatabaseManager;
import fr.rushcubeland.rcbapi.database.MySQL;
import org.bukkit.ChatColor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public enum RankUnit {

    JOUEUR("Joueur", 20, "§7 ", ChatColor.GRAY),
    VIP("VIP", 15, "§e[VIP] §f", ChatColor.YELLOW),
    VIPP("VIP+", 10, "§5[VIP+] §f", ChatColor.DARK_PURPLE),
    ASSISTANT("Assistant", 5, "§a[Assistant] §f", ChatColor.GREEN),
    MODERATEUR("Modérateur", 3, "§6[Modérateur] §f", ChatColor.GOLD),
    DEVELOPPEUR("Développeur", 2, "§9[Développeur] §f", ChatColor.BLUE),
    ADMINISTRATEUR("Admin", 0, "§c[Admin] §f", ChatColor.RED);

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
                    if(rs.next()){

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

        for(String perms : getPermissions()){
            try {

                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO rank_permissions (grade, permission) VALUES ('%s', '%s')",
                        getName(), perms));

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void onDisableServer(){
        sendDataofRankPermissionsToMySQL();
    }

    public void onEnableServer(){
        permissions = getDataofRankPermissionsFromMySQL();
    }
}