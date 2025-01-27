package adrenaline.server.controller.states;

import adrenaline.server.controller.Lobby;
import adrenaline.Color;
import adrenaline.server.model.PowerupCard;

import java.util.ArrayList;

/**
 *
 * The grab state to do all operation of grab action
 *
 */
public class GrabState implements GameState {

    private Lobby lobby;
    private ArrayList<Integer> validSquares;

    /**
     *
     * The constructor init lobby and validSquares attitudes
     * @param lobby The current lobby
     * @param moveRange The moveRang of the player
     */
    public GrabState(Lobby lobby, int moveRange) {
        this.lobby = lobby;
        this.validSquares = lobby.sendCurrentPlayerValidSquares(moveRange);
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String runAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String grabAction() {
        return "Select a square to grab from or a weapon card";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String shootAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPlayers(ArrayList<Color> playersColor) {
        return "You can't do that now!";
    }

    /**
     *
     * To do the select square request which received from client terminal
     *
     * @param index The square index from 0 to 11
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String selectSquare(int index) {
        if(!validSquares.contains(index)) return "You can't grab from that square! Please select a valid square" ;
        lobby.movePlayer(index);
        lobby.grabFromSquare(index);
        lobby.incrementExecutedActions();
        lobby.clearTempAmmo();
        return "OK";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectPowerUp(PowerupCard powerUp) {
        return "You can't do that now! To use the card for paying an ammo cost, please select the square you want to move in first";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectWeapon(int weaponID) {
        return "Select the square you want to move in first. You can select your current square if you want to stay there";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFiremode(int firemode) {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAmmo(Color color) {
        return "You can't do that now!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String moveSubAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String endOfTurnAction() {
        return "Select something to grab or GO BACK to action selection!";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectFinalFrenzyAction(Integer action) { return "KO"; }

    /**
     *
     * To do the go Back action request which received from client terminal
     *
     *
     * @return The result of this request to client
     *
     */
    @Override
    public String goBack() {
        lobby.setState(lobby.isFinalfrenzy() ? new SelectFreneticActionState(lobby) : new SelectActionState(lobby));
        lobby.clearTempAmmo();
        return "OK Select an action";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectAvatar(Color color) {
        return "KO";
    }

    /**
     * The client can't do this at current time
     */
    @Override
    public String selectSettings(int mapID, int skulls, String voterID) {
        return "KO";
    }
}
