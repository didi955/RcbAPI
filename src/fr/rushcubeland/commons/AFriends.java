package fr.rushcubeland.commons;

import java.util.List;
import java.util.UUID;

public class AFriends implements Cloneable {

    private UUID uuid;
    private List<UUID> friends;
    private int maxFriends;

    public AFriends(UUID uuid, int maxFriends, List<UUID> friends) {
        this.uuid = uuid;
        this.maxFriends = maxFriends;
        this.friends = friends;
    }

    public AFriends(){}

    public UUID getUuid() {
        return uuid;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public int getMaxFriends() {
        return maxFriends;
    }

    public void setMaxFriends(int maxFriends) {
        this.maxFriends = maxFriends;
    }

    public void setFriends(List<UUID> friends) {
        this.friends = friends;
    }

    public void addFriend(UUID uuid){
        if(!friends.contains(uuid)){
            friends.add(uuid);
        }
    }

    public void removeFriend(UUID uuid){
        friends.remove(uuid);
    }

    public boolean areFriendWith(UUID uuid){
        if(friends.contains(uuid)){
            return true;
        }
        return false;
    }

    public boolean hasReachedMaxFriends(){
        int a = 0;
        for(UUID friend : friends){
            a = a+1;
        }
        if(maxFriends <= a){
            return true;
        }
        return false;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AFriends clone(){
        try {

            return (AFriends) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
