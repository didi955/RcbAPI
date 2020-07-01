package fr.rushcubeland.rcbapi.account;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.database.DatabaseManager;
import fr.rushcubeland.rcbapi.database.MySQL;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Account extends AbstractData {

    private boolean newPlayer;
    private boolean hasBooster;

    private final DRank dataRank;
    private final DCoins dataCoins;
    private final DPermissions dataPermissions;
    private final DBoosters dataBoosters;
    private final DBattlePass dataBattlePass;

    public Account(UUID uuid) {
        this.uuid = uuid;
        this.newPlayer = false;
        this.hasBooster = false;
        this.dataRank = new DRank(uuid);
        this.dataCoins = new DCoins(uuid);
        this.dataBoosters = new DBoosters(uuid);
        this.dataPermissions = new DPermissions(uuid);
        this.dataBattlePass = new DBattlePass(uuid);
    }

    private String[] getDataOfPlayerFromMySQL() {
        String[] data = new String[3];

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM accounts WHERE uuid='%s'", getUUID()), rs -> {
                try {
                    if(rs.next()) {
                        data[0] = rs.getString("grade");
                        data[1] = rs.getString("grade_end");
                        data[2] = rs.getString("coins");
                    }
                    else
                    {
                        newPlayer = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return data;
    }

    private void sendDataOfPlayerToMysql() {
        if(newPlayer) {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO accounts (uuid, grade, grade_end, coins) VALUES ('%s', '%s', '%s', '%s')",
                        getUUID(), dataRank.getRank().getName(), dataRank.getEnd(), dataCoins.getCoins()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE accounts SET grade='%s', grade_end='%s', coins='%s' WHERE uuid='%s'",
                        dataRank.getRank().getName(), dataRank.getEnd(), dataCoins.getCoins(), getUUID()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void sendDataOfPlayerPermissionsToMySQL(){
        for(String perms : dataPermissions.getPermissions()){

            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM player_permissions WHERE uuid='%s' AND permission='%s'",
                        getUUID(), perms), rs -> {

                    try {
                        if(!rs.next()){
                            try {
                                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO player_permissions (uuid, permission) VALUES ('%s', '%s')",
                                        getUUID(), perms));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private ArrayList<String> getDataOfPlayerPermissionsFromMySQL(){
        ArrayList<String> dataPlayerperms = new ArrayList<>();

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM player_permissions WHERE uuid='%s'",
                    getUUID()), rs -> {

                try {
                    while(rs.next()){

                        dataPlayerperms.add(rs.getString("permission"));

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataPlayerperms;
    }

    private String[] getDataOfPlayerBoosterFromMySQL() {
        String[] data = new String[3];
        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM boosters WHERE uuid='%s'", getUUID()), rs -> {
                try {
                    if(rs.next()) {
                        hasBooster = true;
                        data[0] = rs.getString("booster");
                        data[1] = rs.getString("start");
                        data[2] = rs.getString("end");
                    }
                    else {
                        hasBooster = false;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return data;
    }

    private void sendDataOfPlayerBoosterToMysql() {
        if(hasBooster) {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO boosters (uuid, booster, start, end) VALUES ('%s', '%s', '%s', '%s')",
                        getUUID(), dataBoosters.getBooster().getName(), dataBoosters.getStart(), dataBoosters.getEnd()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM boosters WHERE uuid='%s'",
                        getUUID()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void sendTaskNoAsync(){
        sendDataOfPlayerToMysql();
        sendDataOfPlayerPermissionsToMySQL();
        //sendDataOfPlayerBoosterToMysql();
    }

    private void getTaskNoAsync(){
        String[] data = getDataOfPlayerFromMySQL();
        ArrayList<String> dataPlayerperms = getDataOfPlayerPermissionsFromMySQL();
        // = getDataOfPlayerBoosterFromMySQL();
        //}

        if(newPlayer){
            dataRank.setRank(RankUnit.JOUEUR);
            dataCoins.setCoins(0);
        }
        else
        {
            dataRank.setRank(RankUnit.getByName(data[0]), Long.parseLong(data[1]));
            dataCoins.setCoins(Long.parseLong(data[2]));
            for(String perm : dataPlayerperms){
                dataPermissions.addPermission(perm);
                getPlayer().addAttachment(RcbAPI.getInstance()).setPermission(perm, true);
            }
            if(hasBooster){
                //String[] dataBooster = getDataOfPlayerBoosterFromMySQL();
                //dataBoosters.setBooster(BoostersUnit.getByName(dataBooster[0]), Long.parseLong(dataBooster[1]), Long.parseLong(dataBooster[2]));
            }
        }
    }

    public void onLogin() {
        RcbAPI.getInstance().getAccounts().add(this);
        getTaskNoAsync();
    }

    public void onLogout() {
        sendTaskNoAsync();
        RcbAPI.getInstance().getAccounts().remove(this);
    }

    public DRank getDataRank() {
        return dataRank;
    }

    public DCoins getDataCoins() {
        return dataCoins;
    }

    public DPermissions getDataPermissions() {
        return dataPermissions;
    }

    public DBoosters getDataBoosters(){
        return dataBoosters;
    }

    public DBattlePass getDataBattlePass(){
        return dataBattlePass;
    }
}