package fr.rushcubeland.rcbapi.database;

public enum DatabaseManager {

    Main_BDD(new DatabaseCredentials("sql.alls-heberg.fr", "DylanL17833", "c2OxJa7Ltjsf0dN", "DylanL17833", 3306));

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
