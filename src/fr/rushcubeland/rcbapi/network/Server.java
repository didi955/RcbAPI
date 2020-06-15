package fr.rushcubeland.rcbapi.network;

public class Server {

    public static void initServerGroup(){
        for(ServerUnit serverUnit : ServerUnit.values()){
            if(serverUnit.getServerGroup() == ServerGroup.Lobby){
                ServerGroup.Lobby.getServersInGroup().add(serverUnit);
            }
            if(serverUnit.getServerGroup() == ServerGroup.Minigame){
                ServerGroup.Minigame.getServersInGroup().add(serverUnit);
            }
        }
    }

}
