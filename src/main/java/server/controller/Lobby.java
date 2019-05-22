package server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.LobbyAPI;
import server.controller.states.GameState;
import server.controller.states.*;
import server.model.Map;
import server.network.Client;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Lobby implements Runnable, LobbyAPI {

    private final String lobbyID;
    private LinkedHashMap<String, Client> clientMap;
    private HashMap <String, Player> playersMap;
    private HashMap <Color, Player> playersColor;
    private ArrayList<Player> playersList;
    private String currentTurnPlayer;
    private GameState currentState;

    private Map map;
    private ScoreBoard scoreBoard;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;



    public Lobby(ArrayList<Client> clients) {
        lobbyID = UUID.randomUUID().toString();
        clientMap = new LinkedHashMap<>();

        //ZHANG has added this try-catch for NullPointerException
        //If not necessary，Delete it and retest Test code,add clients parameter
        try {
            for(Client c : clients){
                clientMap.put(c.getClientID(),c);
            }
        }catch (NullPointerException e){
            System.err.println("NullPointerException of clients ArrayList");
        }
        playersMap = new HashMap<>();
        playersColor = new HashMap<>();
        playersList = new ArrayList<>();

        scoreBoard = new ScoreBoard();
        deckWeapon = new DeckWeapon();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
            currentState = new AvatarSelectionState(this, avatars);
        }catch (JsonIOException e){
        }catch (FileNotFoundException e) {}
    }



    //MERGED INTO initMap, TO BE SAFELY REMOVED
    public void chooseAndNewAMap(int num){
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resources/Jsonsrc/Map"+ num +".json");
            this.map=gson.fromJson(fileReader,Map.class);

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
        //setSquaresCards();
    }

    /*
    public void setSquaresCards(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getSquare(i,j).isSpawn() &&
                        this.map.getSquare(i,j).getColor()!= Color.BLACK &&
                        this.map.getSquare(i,j).getAmmoTile() == null)
                    this.map.getSquare(i,j).setAmmoTile(getDeckAmmo().draw());

                if (this.map.getSquare(i,j).isSpawn())
                    while(this.map.getSquare(i,j).getWeaponCards().size() < 3) {
                        WeaponCard weaponCard=getDeckWeapon().draw();
                        if (weaponCard!=null)
                            this.map.getSquare(i, j).getWeaponCards().add(weaponCard);
                    }
            }
        }
    }
    */



    public Map getMap() {
        return map;
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public DeckPowerup getDeckPowerup() {
        return deckPowerup;
    }

    public DeckAmmo getDeckAmmo() {
        return deckAmmo;
    }


    public DeckWeapon getDeckWeapon() {
        return deckWeapon;
    }

    @Override
    public void run() {
        while(clientMap.size()>playersMap.size()); //waits for state to change from AvatarSelectionState
        initMap();
        currentState = new SelectActionState(this, 0);
        //TODO handles the game flow
    }

    public String getID() {
        return this.lobbyID;
    }

    public void setState(GameState newState){ currentState = newState; }

    public String runAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.runAction();
        else return "You can only do that during your turn!";
    }

    public String grabAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.grabAction();
        else return "You can only do that during your turn!";
    }

    public String shootAction(String clientID) {

        if(clientID.equals(currentTurnPlayer)) return currentState.shootAction();
        else return "You can only do that during your turn!";
    }

    public String selectPlayers(String clientID, ArrayList<Color> playersColor) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectPlayers(playersColor);
        else return "You can only do that during your turn!";
    }

    public String selectSquare(String clientID, int index) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectSquare(index);
        else return "You can only do that during your turn!";
    }

    public String selectPowerUp(String clientID, int powerupID) {
        //TODO granata venom during opponent's turn
        if(clientID.equals(currentTurnPlayer)) return currentState.selectPowerUp(powerupID);
        else return "You can only do that during your turn!";
    }

    public String selectWeapon(String clientID, int weaponID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectWeapon(weaponID);
        else return "You can only do that during your turn!";
    }

    public String endOfTurnAction(String clientID) {
        if(clientID.equals(currentTurnPlayer)) return currentState.endOfTurnAction();
        else return "You can only do that during your turn!";
    }

    public String selectAvatar(String clientID, Color color) {
        if(clientID.equals(currentTurnPlayer)) return currentState.selectAvatar(color);
        else return "You can only do that during your turn!";
    }

    public String selectMap(String clientID, int mapID) {
        if(clientMap.keySet().contains(clientID)){
            return currentState.selectMap(mapID, clientID);
        }
        //else: user not part of the lobby
        return "You shouldn't be here!";
    }

    public int getCurrentPlayerAdrenalineState(){
        return playersMap.get(currentTurnPlayer).getAdrenalineState();
    }

    public void endTurn(){
        currentState = new SelectActionState(this, 0);
        nextPlayer();
    }

    public void nextPlayer(){
        Iterator<String> itr = clientMap.keySet().iterator();
        String temp = itr.next();
        while (!temp.equals(currentTurnPlayer)) temp= itr.next();
        if (itr.hasNext()) currentTurnPlayer = itr.next();
        else currentTurnPlayer = clientMap.keySet().iterator().next();
    }

    public synchronized void initCurrentPlayer(Avatar chosen){
        Player newPlayer = new Player(chosen);
        playersMap.put(currentTurnPlayer, newPlayer);
        playersColor.put(chosen.getColor(), newPlayer);
        nextPlayer();
    }

    private void initMap(){
        MapSelectionState mapSelectionState = new MapSelectionState(this, new ArrayList<>(clientMap.keySet()));
        currentState = mapSelectionState;
        int mapID = mapSelectionState.startTimer();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Map"+ mapID +".json");
            this.map=gson.fromJson(fileReader,Map.class);
        }catch (JsonIOException e){
        }
        catch (FileNotFoundException e) {
        }
    }

    public ArrayList<Integer> sendCurrentPlayerValidSquares(int range){
        ArrayList<Integer> validSquares = map.getValidSquares(playersMap.get(currentTurnPlayer).getPosition(), range);
        //TODO sends list to client
        return validSquares;
    }

    public void movePlayer(int squareIndex){
        if(playersMap.get(currentTurnPlayer).getPosition()!= squareIndex) playersMap.get(currentTurnPlayer).setPosition(squareIndex);
    }

    public void movePlayer(int squareIndex, Color playerColor){
        if(playersColor.get(playerColor).getPosition()!= squareIndex) playersColor.get(playerColor).setPosition(squareIndex);
    }

    public void grabFromSquare(int squareIndex, int actionNumber){
        map.getSquare(squareIndex).acceptGrab(this, actionNumber);
    }

    public void grabFromSquare(SquareAmmo square, int actionNumber){
        Player currentPlayer = playersMap.get(currentTurnPlayer);
        AmmoCard grabbedAmmoTile= square.getAmmoTile();
        if (grabbedAmmoTile!=null) {
            int[] grabbedAmmoContent = grabbedAmmoTile.getAmmoContent();
            //Discard the tile. && Remove the ammo tile.
            deckAmmo.addToDiscarded(grabbedAmmoTile);
            square.setAmmoTile(null);
            //Move the depicted cubes into your ammo box.
            currentPlayer.addAmmoBox(grabbedAmmoContent);
            //If the tile depicts a powerup card, draw one.
            if (grabbedAmmoContent[3] != 0 && currentPlayer.getPowerupHandSize()<3) currentPlayer.addPowerup(deckPowerup.draw());
        }
        setState(new SelectActionState(this, actionNumber));
    }

    public void grabFromSquare(SquareSpawn square, int actionNumber){
        setState(new WeaponGrabState(this, actionNumber, square.getWeaponCards()));
    }

    public void consumePowerup(int powerUpID){
        playersMap.get(currentTurnPlayer).consumePower(powerUpID);
    }
}

