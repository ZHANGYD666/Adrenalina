package server.controller;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import server.network.Client;
import server.model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


public class Lobby implements Runnable {

    private final String lobbyID;
    private HashMap <String, Player> players;
    private Map map;
    private ScoreBoard scoreBoard;
    private ArrayList<Player> deckOfPlayers;
    private DeckWeapon deckWeapon;
    private DeckAmmo deckAmmo;
    private DeckPowerup deckPowerup;



    public Lobby(ArrayList<Client> players) {
        lobbyID = UUID.randomUUID().toString();
        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Avatar.json");
            Avatar[] avatarsGson= gson.fromJson(fileReader,Avatar[].class);
            ArrayList<Avatar> avatars = new ArrayList<>(Arrays.asList(avatarsGson));
        }catch (JsonIOException e){

        }catch (FileNotFoundException e) {

        }
        scoreBoard = new ScoreBoard();
        deckOfPlayers = new ArrayList<>();
        deckAmmo = new DeckAmmo();
        deckPowerup = new DeckPowerup();
        deckWeapon = new DeckWeapon();

    }



    //For new Map,It has to ensure the map number entry 1~4
    public void chooseAndNewAMap(int num){

        this.map= new Map();


        try{
            Gson gson = new Gson();
            FileReader fileReader = new FileReader("src/main/resource/Jsonsrc/Map"+ num +".json");
            this.map=gson.fromJson(fileReader,Map.class);

        }catch (JsonIOException e){
            System.out.println("JsonIOException!");
        }
        catch (FileNotFoundException e) {
            System.out.println("PowerupCard.json file not found");
        }
        setSquaresCards();
    }



    public void setSquaresCards(){

        for (int i=0;i<this.map.getRows();i++){
            for (int j=0;j<this.map.getColumns();j++){

                if(!this.map.getSquare(i,j).isSpawn() &&
                        this.map.getSquare(i,j).getColor()!= Color.BLACK &&
                        this.map.getSquare(i,j).getAmmoTile() == null)
                    this.map.getSquare(i,j).setAmmoTile(getDeckAmmo().draw());

                if (this.map.getSquare(i,j).isSpawn())
                    while(this.map.getSquare(i,j).getWeaponCardDeck().size() < 3) {
                        WeaponCard weaponCard=getDeckWeapon().draw();
                        if (weaponCard!=null)
                            this.map.getSquare(i, j).getWeaponCardDeck().add(weaponCard);
                    }
            }
        }
    }



    public Map getMap() {
        return map;
    }

    public ArrayList<Player> getDeckOfPlayers() {
        return deckOfPlayers;
    }

    public ScoreBoard getScoreBoard() {

        return scoreBoard;

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


    //It will return how much Players have already entered
    public int getNumOfPlayers(){

        return this.getDeckOfPlayers().size();

    }


    //Use this method to add every player
    public void addNewPlayerToDeck(Player newPlayer) {

        this.getDeckOfPlayers().add(newPlayer);

    }


    @Override
    public void run() {
        //TODO handles the game flow
    }

    public String getID() {
        return this.lobbyID;
    }
}
