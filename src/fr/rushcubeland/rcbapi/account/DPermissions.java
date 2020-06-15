package fr.rushcubeland.rcbapi.account;

import java.util.ArrayList;
import java.util.UUID;

public class DPermissions extends AbstractData {

    private ArrayList<String> permissions = new ArrayList<>();

    public DPermissions(UUID uuid){
        this.uuid = uuid;
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

    public ArrayList<String> getPermissions(){
        return this.permissions;
    }

    public boolean hasPermission(String permission){
        if(this.permissions.contains(permission)){
            return true;
        }
        return false;
    }


}
