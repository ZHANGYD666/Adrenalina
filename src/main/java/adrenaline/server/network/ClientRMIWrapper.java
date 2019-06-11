package adrenaline.server.network;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.ClientAPI;
import adrenaline.server.controller.Lobby;

import java.util.ArrayList;
import java.util.UUID;

public class ClientRMIWrapper implements Client {
    private final String clientID;
    private String nickname = null;
    private ClientAPI thisClient;
    private boolean active;

    public ClientRMIWrapper(ClientAPI newClient) {
        clientID = UUID.randomUUID().toString();
        thisClient = newClient;
        active = true;
    }

    public String getClientID() {
        return clientID;
    }

    public String getNickname(){ return nickname; }

    public boolean setNickname(String nickname) {
        if(this.nickname != null) return false;
        this.nickname = nickname;
        return true;
    }

    public void setActive(boolean active) { this.active = active; }

    public void setLobby(Lobby lobby, ArrayList<String> nicknames) { setLobby(lobby.getID(), nicknames); }

    public void setLobby(String lobbyID, ArrayList<String> nicknames){
        try {
            thisClient.setLobby(lobbyID, nicknames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void setPlayerColor(String nickname, Color color){
        try {
            thisClient.setPlayerColor(nickname,color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void timerStarted(Integer duration, String comment) {
        try {
            thisClient.timerStarted(duration, comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(UpdateMessage updatemsg) {
        try {
            thisClient.update(updatemsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
