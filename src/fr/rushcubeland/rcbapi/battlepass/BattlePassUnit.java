package fr.rushcubeland.rcbapi.battlepass;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.data.sql.DatabaseManager;
import fr.rushcubeland.rcbapi.data.sql.MySQL;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.*;

public enum BattlePassUnit {

    JANVIER("JANVIER", 50000, 50, 0, 0,0),
    FEVRIER("FEVRIER", 50000, 50, 0, 0,1),
    MARS("MARS", 50000, 50, 0, 0,2),
    AVRIL("AVRIL", 50000, 50, 0, 0,3),
    MAI("MAI", 50000, 50, 0, 0,4),
    JUIN("JUIN", 50000 , 50, 0, 0,5),
    JUILLET("JUILLET", 50000, 50, 0,0,6),
    AOUT("AOUT", 50000, 50, 0, 0,7),
    SEPTEMBRE("SEPTEMBRE", 50000, 50, 0, 0,8),
    OCTOBRE("OCTOBRE", 50000, 50, 0, 0, 9),
    NOVEMBRE("NOVEMBRE", 50000, 50, 0, 0,10),
    DECEMBRE("DECEMBRE", 50000, 50, 0, 0,11);

    private String name;
    private int maxExp;
    private final int nbPaliers;
    private long start;
    private long end;
    private int id;

    private static long remainingDuration;

    private static BattlePassUnit battlePass;
    private static boolean existsTable;

    BattlePassUnit(String name, int maxExp, int nbPaliers, long start, long end, int id){
        this.name = name;
        this.maxExp = maxExp;
        this.nbPaliers = nbPaliers;
        this.start = start;
        this.end = end;
        this.id = id;
    }

    public static BattlePassUnit getByName(String name){
        for(BattlePassUnit battlePassUnit : BattlePassUnit.values()){
            if(battlePassUnit.getName() == name){
                return battlePassUnit;
            }
        }
        return null;
    }

    public static Optional<BattlePassUnit> getById(int id){
        return Arrays.stream(values()).filter(r -> r.getId() == id).findFirst();
    }

    public static void checkStateBattlePass(){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        for(BattlePassUnit battlePassUnit : BattlePassUnit.values()){
            if(calendar.get(Calendar.MONTH) != battlePassUnit.getId()){
                setBattlePass(battlePassUnit);
                battlePassUnit.initBattlePass();
                break;

            }
            else {
                return;
            }
        }
    }

    public static BattlePassUnit getCurrentBattlePass(){
        return battlePass;

    }

    public String getName() {
        return name;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getNbPaliers() {
        return nbPaliers;
    }

    public int getId() {
        return id;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public static void setBattlePass(BattlePassUnit battlePass) {
        BattlePassUnit.battlePass = battlePass;
    }

    public void initBattlePass(){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        long start = calendar.getTimeInMillis();
        long end = start+getMillisInAMonth();
        setEnd(end);
        setStart(start);

    }

    private static long getMillisInAMonth(){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTimeInMillis();

    }

    public static long getRemainingDuration() {
        return remainingDuration;
    }

    public static void setRemainingDuration(long remainingDuration) {
        BattlePassUnit.remainingDuration = remainingDuration;
    }


    private String[] getDataOfBattlePassFromMySQL() {
            String[] data = new String[2];
            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT FROM battlepass WHERE name='%s'", getName()), rs -> {
                    try {
                        if(rs.next()) {
                            data[0] = rs.getString("start");
                            data[1] = rs.getString("end");
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

    private void sendDataOfBattlepassToMysql() {
        if(existsTable) {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE battlepass SET name='%s', start='%s', end='%s' WHERE name='%s'",
                        getName(), getStart(), getEnd()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO battlepass (name, start, end) VALUES ('%', '%', '%')",
                        getName(), getStart(), getEnd()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private boolean aBattlePassAlreadyRegisterInMySQL(){
        final String[] s = {null};

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM battlepass WHERE name='%s'", getName()), rs -> {
                try {
                    if(rs.next()) {

                        s[0] = rs.getString("name");
                        existsTable = true;
                    }
                    else
                    {
                        existsTable = false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(s[0] != null){
            existsTable = true;
            return true;
        }
        else {
            existsTable = false;
            return false;
        }
    }

    private void sendTaskAsync(){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            aBattlePassAlreadyRegisterInMySQL();
            sendDataOfBattlepassToMysql();
        });
    }

    private void getTaskAsync(){
        Bukkit.getScheduler().runTaskAsynchronously(RcbAPI.getInstance(), () -> {
            String[] data = getDataOfBattlePassFromMySQL();
            setBattlePass(getByName(data[0]));
            setStart(Long.parseLong(data[1]));
            setEnd(Long.parseLong(data[2]));
        });
    }

    public void onEnableServer(){
        getTaskAsync();
    }

    public void onDisableServer(){
        sendTaskAsync();
    }



}
