package server.network;

import server.LobbyAPI;
import server.controller.Lobby;
import server.model.Color;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class LobbyExportable extends UnicastRemoteObject implements LobbyAPI {
    private Lobby lobbyRelay;

    public LobbyExportable(Lobby lobby) throws RemoteException {
        lobbyRelay = lobby;
    }

    @Override
    public void runAction() {

    }

    @Override
    public void grabAction() {

    }

    @Override
    public void shootAction() {

    }

    @Override
    public void selectPlayers(ArrayList<Color> playersColor) {

    }

    @Override
    public void selectSquare(int index) {

    }

    @Override
    public void selectPowerUp() {

    }

    @Override
    public void selectWeapon() {

    }

    @Override
    public void endOfTurnAction() {

    }
}
