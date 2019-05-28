package adrenaline.server.controller;

import org.junit.jupiter.api.Test;
import adrenaline.Color;
import adrenaline.server.model.Player;
import adrenaline.server.model.PowerupCard;
import adrenaline.server.model.WeaponCard;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndTurnAndReloadTest {

    @Test
    void reloadWeaponTest() {


        ArrayList<PowerupCard> discardPowerup;

        Lobby lobby = new Lobby(null);
        Player player = new Player("Anna", Color.BLUE,lobby);

        WeaponCard weaponCard = new WeaponCard("test",new int[]{3,3,3},Color.YELLOW,"Test",2,null);

        player.addWeaponCard(weaponCard);
        player.getWeaponCards().get(0).setLoaded(false);
        player.addAmmoBox(new int[]{3,3,3});

        assertTrue(EndTurnAndReload.reloadWeapon(player,0,null));

        //Try pay with Poeerup
        player.getWeaponCards().get(0).setLoaded(false);
        player.addAmmoBox(new int[]{0,3,3});
        PowerupCard powerupCard = new PowerupCard("test", Color.RED,"Test",true,2);

        discardPowerup = new ArrayList<>();
        discardPowerup.add(powerupCard);
        discardPowerup.add(powerupCard);
        discardPowerup.add(powerupCard);


        assertTrue(EndTurnAndReload.reloadWeapon(player,0,discardPowerup));


    }
}