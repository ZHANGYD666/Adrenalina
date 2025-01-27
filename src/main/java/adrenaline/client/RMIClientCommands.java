package adrenaline.client;

import adrenaline.Color;
import adrenaline.UpdateMessage;
import adrenaline.client.controller.GameController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * RMIClientCommands implements ClientAPI,for implement the command from server,
 * set the game flow value for client via RMI
 *
 *
 */
public class RMIClientCommands extends UnicastRemoteObject implements ClientAPI{

    private RMIHandler client;
    private GameController gameController;

    /**
     *
     * The RMIClientCommands constructor, set the client and gameController attitude
     *
     *
     */
    RMIClientCommands(RMIHandler client, GameController gameController) throws RemoteException {
        this.client = client;
        this.gameController = gameController;
    }

    /**
     *
     * Received the nickname set from server terminal
     *
     * @param nickname The nickname string
     *
     */
    public void setNickname(String nickname){
        gameController.setOwnNickname(nickname);
    }

    /**
     *
     * Received the lobby set from server terminal
     *
     * @param lobbyID The lobby id string
     * @param nicknames The players' nickname ArrayList
     */
    public void setLobby(String lobbyID, ArrayList<String> nicknames) {
        client.setMyLobby(lobbyID);
        gameController.changeStage();
        gameController.initPlayersNicknames(nicknames);
    }

    /**
     *
     *
     * Received the players' color set from server terminal
     *
     * @param nickname The nickname of players
     * @param color The color of players
     */
    public void setPlayerColor(String nickname, Color color) {
        gameController.setPlayerColor(nickname, color);
    }

    /**
     *
     *
     * To remind the client timer start.
     *
     * @param duration The duration of timer in seconds
     * @param comment The comment for this timer
     */
    public void timerStarted(Integer duration, String comment) {
        gameController.timerStarted(duration, comment);
    }

    /**
     *
     * Show the valid squares when player did a action
     *
     * @param validSquares The ArrayList of the valid squares
     *
     */
    public void validSquaresInfo(ArrayList<Integer> validSquares) { gameController.validSquaresInfo(validSquares); }

    /**
     *
     *
     * To do update message for client model
     *
     * @param updatemsg The UpdateMessage reference
     *
     */
    public void update(UpdateMessage updatemsg) {
        updatemsg.applyUpdate(gameController);
    }

    /**
     *
     * This client is kicked from server because he did nothing during the whole turn
     * he have to do the reconnection to reconnect the server
     */
    public void kick(){
        client.closeConnection();
        gameController.notifyDisconnect();
    }

}
