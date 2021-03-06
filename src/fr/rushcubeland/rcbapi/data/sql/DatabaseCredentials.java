package fr.rushcubeland.rcbapi.data.sql;

public class DatabaseCredentials {

    private String host;
    private String user;
    private String pass;
    private String dbName;
    private int port;

    public DatabaseCredentials(String host, String user, String pass, String dbName, int port){
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
        this.port = port;
    }

    public String toURI(){
        final StringBuilder sb = new StringBuilder();

        sb.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(dbName);

        return sb.toString();
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public String getPass() {
        return pass;
    }

    public int getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }
}
