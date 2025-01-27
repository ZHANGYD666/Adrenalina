package adrenaline;

import adrenaline.client.controller.GameController;
import adrenaline.client.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

/**
 *
 * To update player massage.The realize of observer pattern
 *
 */
public class PlayerUpdateMessage implements UpdateMessage {
    private Player clientsidePlayer;

    /**
     *
     * For update the message
     * @param serversidePlayer The player model if server side
     */
    public PlayerUpdateMessage(adrenaline.server.model.Player serversidePlayer){
        ArrayList<Integer> weaponIDs = new ArrayList<>();
        ArrayList<Integer> powerupIDs = new ArrayList<>();

        for(WeaponCard wc : serversidePlayer.getWeaponCards()){
            weaponIDs.add(wc.getWeaponID());
        }
        for(PowerupCard puc : serversidePlayer.getPowerupCards()){
            powerupIDs.add(puc.getPowerupID());
        }

        clientsidePlayer = new Player(serversidePlayer.getColor(), serversidePlayer.getPosition(), serversidePlayer.getOldPosition(),
                                        serversidePlayer.getAmmoBox(), serversidePlayer.getDamageTrack(),
                                        serversidePlayer.getMarks(), weaponIDs, powerupIDs);
    }

    /**
     *
     * For update the message
     * @param clientGameController The game controller if client
     */
    @Override
    public void applyUpdate(GameController clientGameController) {
        clientGameController.updatePlayer(clientsidePlayer);
    }
}
