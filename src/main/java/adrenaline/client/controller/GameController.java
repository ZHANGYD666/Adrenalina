package adrenaline.client.controller;

import adrenaline.Color;
import adrenaline.client.ConnectionHandler;
import adrenaline.client.RMIHandler;
import adrenaline.client.SocketHandler;
import adrenaline.client.model.Map;
import adrenaline.client.model.Player;
import adrenaline.client.model.ScoreBoard;
import adrenaline.client.view.ViewInterface;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class GameController {

    private LinkedHashMap<String, Color> playersNicknames = new LinkedHashMap<>();
    private ScoreBoard scoreBoard;
    private Map map;
    private HashMap<Color, Player> playersMap = new HashMap<>();
    private ViewInterface view;
    private ConnectionHandler connectionHandler;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);


    public LinkedHashMap<String, Color> getPlayersNicknames(){ return playersNicknames; }

    public Map getMap(){ return map; }

    public void setViewController(ViewInterface viewController){
        this.view = viewController;
    }


    public boolean connectRMI(String host, int port){
        try {
            connectionHandler = new RMIHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }


    public boolean connectSocket(String host, int port){
        try {
            connectionHandler = new SocketHandler(host,port,this);
            return true;
        } catch (Exception e) {
            view.showError("Wrong host/port");
            return false;
        }
    }


    public void setNickname(String nickname){ connectionHandler.setNickname(nickname);}

    public void initPlayersNicknames(ArrayList<String> nicknames){
        nicknames.forEach(x -> playersNicknames.put(x, Color.WHITE));
    }

    public void setPlayerColor(String nickname, Color color){
        changes.firePropertyChange("nicknamesColor", playersNicknames, playersNicknames.put(nickname, color));
    }

    public void selectAvatar(Color color){ connectionHandler.selectAvatar(color); }

    public void cleanExit(){
        if(connectionHandler != null) connectionHandler.unregister();
    }


    public void handleReturn(String returnMsg){
        if(!returnMsg.equals("OK")) view.showError(returnMsg);
    }

    public void changeStage(){ view.changeStage(); }

    public void updatePlayer(Player newPlayer){
        playersMap.put(newPlayer.getColor(), newPlayer);
    }

    public void updateMap(Map newMap){
        Map oldMap = map;
        map = newMap;
        changes.firePropertyChange("map", oldMap, newMap);
    }

    public void addPropertyChangeListener(PropertyChangeListener l){
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l){
        changes.removePropertyChangeListener(l);
    }

    public void updateScoreBoard(ScoreBoard newScoreBoard){
        scoreBoard = newScoreBoard;
    }

    public void timerStarted(Integer duration) {
        view.notifyTimer(duration);
        //this is for showing the timer on the view
        //the cli can decide how to tell the player that a timer has started
    }

    public void sendSettings(int selectedMap, int selectedSkull) {
        connectionHandler.sendSettings(selectedMap, selectedSkull);
    }
    
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }
}
