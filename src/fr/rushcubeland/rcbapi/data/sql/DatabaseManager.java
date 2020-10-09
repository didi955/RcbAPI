package fr.rushcubeland.rcbapi.data.sql;

import fr.rushcubeland.rcbapi.RcbAPI;

public enum DatabaseManager {

    Main_BDD(new DatabaseCredentials(RcbAPI.getInstance().getConfig().getString("Databases.database1.host"), RcbAPI.getInstance().getConfig().getString("Databases.database1.user"), RcbAPI.getInstance().getConfig().getString("Databases.database1.pass"), RcbAPI.getInstance().getConfig().getString("Databases.database1.dbname"), RcbAPI.getInstance().getConfig().getInt("Databases.database1.port")));

    private DatabaseAccess databaseAccess;

    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.initPool();
        }

    }

    public static void closeAllDatabaseConnection() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.closePool();
        }

    }
}
