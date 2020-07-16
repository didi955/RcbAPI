package fr.rushcubeland.rcbapi.network;

public enum ServerUnit {

    Lobby_1("Lobby", ServerGroup.Lobby, 500, "127.0.0.1", 25566),
    DeterrentBorder_1("DeterrentBorder_1", ServerGroup.Minigame, 16, "127.0.0.1", 25567);
    //Minigame_2("Minigame_2", ServerGroup.Minigame,16, "127.0.0.1", 25567),
    //Minigame_3("Minigame_3", ServerGroup.Minigame,16, "127.0.0.1", 25568);

    private String name;
    private int maxPlayers;
    private ServerGroup serverGroup;
    private int port;
    private int slots;
    private String ip;

    ServerUnit(String name, ServerGroup serverGroup, int maxPlayers, String ip, int port){
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.serverGroup = serverGroup;
        this.port = port;
        this.ip = ip;
        serverGroup.getServersInGroup().add(this);
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public ServerGroup getServerGroup(){
        return serverGroup;
    }

    public Integer getPort(){
        return port;
    }

    public void setSlots(Integer playersCount){
        slots = playersCount;
    }

    public Integer getSlots(){
        return slots;
    }

    public String getIp() {
        return ip;
    }

}
