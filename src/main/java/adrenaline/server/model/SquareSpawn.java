package adrenaline.server.model;

import adrenaline.server.controller.Lobby;
import adrenaline.MapUpdateMessage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SquareSpawn extends Square {

    private ArrayList<WeaponCard> weaponCards;

    @Override
    public void acceptGrab(Lobby lobby) {
        lobby.grabFromSquare(this);
    }

    @Override
    public void acceptConvertInfo(MapUpdateMessage updatemsg, int index) {
        updatemsg.addWeaponInfo(color, (ArrayList<Integer>) weaponCards.stream().map(WeaponCard::getWeaponID).collect(Collectors.toList()));
    }

    @Override
    public void setCard(Lobby lobby) {
        if(weaponCards.size()<3) lobby.setWeaponCard(this, 3-weaponCards.size());
    }

    public WeaponCard getWeaponCard(int weaponID) {
        for(WeaponCard wc : weaponCards){
            if(wc.getWeaponID() == weaponID)return wc;
        }
        return null;
    }

    public boolean removeCard(WeaponCard card){
        boolean result = weaponCards.remove(card);
        if(result) map.notifyObservers(new MapUpdateMessage(map));
        return result;
    }

    public void addCard(WeaponCard card){
        weaponCards.add(card);
        if(map.anyObserver()) map.notifyObservers(new MapUpdateMessage(map));
    }
}
