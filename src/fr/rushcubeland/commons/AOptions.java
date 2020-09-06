package fr.rushcubeland.commons;

import fr.rushcubeland.rcbapi.options.OptionUnit;

import java.util.UUID;

public class AOptions implements Cloneable {

    private UUID uuid;

    private OptionUnit statePartyInvite;
    private OptionUnit stateFriendRequests;
    private OptionUnit stateChat;
    private OptionUnit stateFriendsStatutNotif;
    private OptionUnit stateMP;

    public AOptions(){
    }

    public AOptions(UUID uuid, OptionUnit statePartyInvite, OptionUnit stateFriendRequests, OptionUnit stateChat, OptionUnit stateFriendsStatutNotif, OptionUnit stateMP) {
        this.uuid = uuid;
        this.statePartyInvite = statePartyInvite;
        this.stateFriendRequests = stateFriendRequests;
        this.stateChat = stateChat;
        this.stateFriendsStatutNotif = stateFriendsStatutNotif;
        this.stateMP = stateMP;
    }

    public UUID getUuid() {
        return uuid;
    }

    public OptionUnit getStatePartyInvite() {
        return statePartyInvite;
    }

    public void setStatePartyInvite(OptionUnit statePartyInvite) {
        this.statePartyInvite = statePartyInvite;
    }

    public OptionUnit getStateFriendRequests() {
        return stateFriendRequests;
    }

    public void setStateFriendRequests(OptionUnit stateFriendRequests) {
        this.stateFriendRequests = stateFriendRequests;
    }

    public OptionUnit getStateChat() {
        return stateChat;
    }

    public void setStateChat(OptionUnit stateChat) {
        this.stateChat = stateChat;
    }

    public OptionUnit getStateFriendsStatutNotif() {
        return stateFriendsStatutNotif;
    }

    public void setStateFriendsStatutNotif(OptionUnit stateFriendsStatutNotif) {
        this.stateFriendsStatutNotif = stateFriendsStatutNotif;
    }

    public OptionUnit getStateMP() {
        return stateMP;
    }

    public void setStateMP(OptionUnit stateMP) {
        this.stateMP = stateMP;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AOptions clone(){
        try {

            return (AOptions) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
