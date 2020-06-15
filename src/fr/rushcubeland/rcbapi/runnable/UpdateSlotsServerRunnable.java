package fr.rushcubeland.rcbapi.runnable;

import fr.rushcubeland.rcbapi.network.Network;

public class UpdateSlotsServerRunnable implements Runnable {
    @Override
    public void run() {
        Network.updateSlotsServers();
    }
}
