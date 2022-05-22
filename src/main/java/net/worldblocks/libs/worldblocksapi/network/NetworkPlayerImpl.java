package net.worldblocks.libs.worldblocksapi.network;

import java.util.UUID;

public class NetworkPlayerImpl implements NetworkPlayer {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUuid() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendTitle(String header, String footer) {

    }

    @Override
    public void sendActionBar(String message) {

    }

    @Override
    public void sendToServer(String server) {

    }
}
