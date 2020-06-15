package fr.rushcubeland.rcbapi.account;

import java.util.UUID;

public class DBattlePass extends AbstractData {

    private int exp;

    public DBattlePass(UUID uuid) {
        this.uuid = uuid;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addExp(int exp){
        this.exp = getExp()+exp;
    }

    public void removeExp(int exp){
        this.exp = getExp()-exp;
    }

    // Methodes global des données du joueur concernant le battlepass en cours

    // Enregistrement de ses données à la déco et récupération à la co

    public void onLogout(){

    }

    public void onLogin(){

    }

}
