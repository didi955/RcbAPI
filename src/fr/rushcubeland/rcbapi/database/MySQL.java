package fr.rushcubeland.rcbapi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class MySQL {

    public static void update(Connection connection, String qry) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        s.executeUpdate();
        s.close();
        connection.close();
    }

    public static Object query(Connection connection, String qry, Function<ResultSet, Object> consumer) throws SQLException {

        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        s.close();
        connection.close();
        return consumer.apply(rs);

    }

    public static void query(Connection connection, String qry, Consumer<ResultSet> consumer) throws SQLException {
        PreparedStatement s = connection.prepareStatement(qry);
        ResultSet rs = s.executeQuery();
        consumer.accept(rs);
        s.close();
        connection.close();
    }

    public static void createTables() {

        try {
            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS accounts (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "grade VARCHAR(255), " +
                    "grade_end BIGINT, " +
                    "coins BIGINT)");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS boosters (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "booster VARCHAR(255), " +
                    "start BIGINT, " +
                    "end BIGINT)");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS battlepass (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255), " +
                    "start BIGINT, " +
                    "end BIGINT)");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS rank_permissions (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "grade VARCHAR(255), " +
                    "permission VARCHAR(255))");

            update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "CREATE TABLE IF NOT EXISTS player_permissions (" +
                    "`#` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "uuid VARCHAR(255), " +
                    "permission VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
