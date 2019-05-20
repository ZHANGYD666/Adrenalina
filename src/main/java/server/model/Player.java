package server.model;

import server.controller.Lobby;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;




/**
 *
 * This class is for new a Player
 *
 */

public class Player {

    private Avatar avatar;
    private Lobby lobby;
    private int[] ammoBox;
    private ArrayList<Player> damage;    //use a ArrayList for index the source of damage
    private ArrayList<Player> mark;
    private ArrayList<PowerupCard> powerup;
    private ArrayList<WeaponCard> weaponCard;
    private int score;

    private int position;
    private int oldPosition;  //Last position
    private int adrenalineState;  //This's an attribute for index how much steps this player can move
                                //The first element is for steps,second is for index how much steps can move before grab
                                //The third element is for index how much steps can move before shoot

    private int numOfActions; //This's for index the times of action the player can choose.Max is 2.
                                // When his turn is finished, This value will be reload at 2.



    //costruttore temporaneo per fare buildare il progetto
    public Player(String avatar, Color color, Lobby lobby){
        damage = new ArrayList<>();
        powerup = new ArrayList<>();
        weaponCard = new ArrayList<>();
        mark = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{0,0,0};
        numOfActions = 2;
        score = 0;
        this.lobby = lobby;

    }


    public Player(Avatar avatar){
        this.avatar = avatar;
        damage = new ArrayList<>();
        powerup = new ArrayList<>();
        weaponCard = new ArrayList<>();
        mark = new ArrayList<>();
        adrenalineState = 0;
        ammoBox = new int[]{0,0,0};
        numOfActions = 2;
    }



    /**
     *
     * This is for add score of player
     *
     * @param point Deposit the points got by the player at this time
     *
     */

    public void addScore(int point) {

        this.score= this.score+point;

    }


    /**
     *
     * Get the score of this player
     *
     * @return The score of this player
     *
     */

    public int getScore() {
        return score;

    }



    /**
     *
     *
     * This is a getter, It can get the lobby of this player
     *
     * @return The reference of lobby of this player
     *
     *
     */

    public Lobby getLobby() {
        return lobby;
    }



    /**
     *
     *
     * Add damage for this player
     *
     * @return This boolean is for index if the this play is already died.
     *
     *
     */

    public boolean sufferDamage(Player damageOrigin, int amount) {

        boolean sufferDamageNotFinished;

        for (;amount>0;amount--) {
            if (amount>1 || !this.mark.isEmpty())
                sufferDamageNotFinished = true;
            else
                sufferDamageNotFinished = false;
            if(addDamageToTrack(damageOrigin, sufferDamageNotFinished))
                return true;
        }

        //If  this player have mark, Put all of them to damage track
        return putMarkToDamageTrackAndClearThem();

    }


    /**
     *
     *
     * For remove Power Card for this player.
     *
     * @param powerup The reference of the powerup to be removed
     *
     */

    void removePowerup(PowerupCard powerup) {
        this.powerup.remove(powerup);
    }


    /**
     *
     *
     * Add a mark from other to this Player
     *
     * @param markOrigin The Origin player of this mark
     *
     */

    public void addMark(Player markOrigin) {

        if (this.mark.size()<3)
            mark.add(markOrigin);
        else
            //Remind the mark board can have up to 3 marks from each other player
            // so this mark is wasted
            ;

    }


    /**
     *
     *
     * Clear all mark for this player
     *
     *
     */

    private void clearMark() {

        this.mark.clear();
    }



    /**
     *
     *
     * Return the mark arrayList of this player
     *
     * @return Return ArrayList of mark of this Player.
     *
     */

    public ArrayList<Player> getMark() {
        return mark;
    }




    /**
     *
     *
     * A private method,for add damage to damage track
     *
     * @param damageOrigin The damage origin player
     * @param addDamageNotFinished The boolean value for index if the damage process is finished
     *
     */

    private boolean addDamageToTrack(Player damageOrigin, boolean addDamageNotFinished){


        this.damage.add(damageOrigin);


        //Attention: This is only available for mode 1.
        //First blood
        if (this.damage.size() == 1)
            damageOrigin.addScore(1);


        //judgment for upgrade
        if (this.damage.size() >= 3)
            this.adrenalineState = 1;

        if (this.damage.size() >= 6)
            this.adrenalineState = 2;


        //kill
        if (this.damage.size() == 11 && !addDamageNotFinished) {

            //set score kill
            killAndOverkillScoreCount();

            //set status dead
            return true;
        }



        //overkill
        if (this.damage.size() == 12) {

            //overkill and break
            killAndOverkillScoreCount();

            //set status dead
            //If you overkill a player, that player will give you a mark representing his or her desire for revenge
            damageOrigin.addMark(this);
            return true;
        }

        return false;
    }


    /**
     *
     *
     * This is a private class, for count the points,when someone is dead.
     *
     *
     */

    private void killAndOverkillScoreCount(){


        ArrayList<Player> playerToBeSort;

        Comparator comparator = (Comparator<Player>) (Player o1, Player o2) -> {

            if (Collections.frequency(this.damage,o1)>Collections.frequency(this.damage,o2))
                return 1;
            else if (Collections.frequency(this.damage,o1)==Collections.frequency(this.damage,o2)
                    && Collections.frequency(this.damage,o1)!=0){
                if (this.damage.indexOf(o1)<this.damage.indexOf(o2))
                    return 1;
                else
                    return -1;
            }
            else
                return -1;
        };


        playerToBeSort = (ArrayList<Player>) getLobby().getPlayersList().clone();

        playerToBeSort.sort(comparator);


        /*
        int maxElement=0;
        int a;
        Player maxPlayer=null;

        //Collections.frequency(this.damage,damageOrigin);


        for (int i=0;i<this.player.getLobby().getPlayersList().size();i++){
            a = Collections.frequency(this.damage,this.player.getLobby().getPlayersList().get(i));
            if (a>maxElement){
                maxElement=a;
                maxPlayer=this.player.getLobby().getPlayersList().get(i);
            }
            else if(a==maxElement && maxPlayer!=null && maxElement!=0){
                if (this.damage.indexOf(this.player.getLobby().getPlayersList().get(i))
                        < this.damage.indexOf(maxPlayer)){
                    maxElement=a;
                    maxPlayer=this.player.getLobby().getPlayersList().get(i);
                }
            }
        }
        maxPlayer.addScore(this.scoreBoard[this.player.getNumberOfDeaths()]);
         */

    }




    /**
     *
     *
     * This is a private class, To put mark to damage track,and clean make track
     *
     * @return It will return a boolean value,this value is for index,if the this play is already died.
     */

    private boolean putMarkToDamageTrackAndClearThem(){

        boolean markNotFinished;

        if (!this.mark.isEmpty()){
            for (int i=0;i<this.mark.size();i++){
                if (i < this.mark.size()-1)
                    markNotFinished = true;
                else
                    markNotFinished =false;

                if(addDamageToTrack(this.mark.get(i),markNotFinished))
                    return true;
            }
            clearMark();
        }
        return false;
    }




    public ArrayList<WeaponCard> getWeaponCard() {
        return weaponCard;
    }


    public void addWeaponCard(WeaponCard weaponCard) {

        this.weaponCard.add(weaponCard);

    }

    public void setPosition(int position) {
        this.oldPosition = this.position;
        this.position = position;
    }


    public void deletePowerup(PowerupCard powerup) {

        this.getPowerup().remove(powerup);
    }

    public int getPosition(){ return this.position;}


    public int getAdrenalineState() { return adrenalineState;}

    public int[] getAmmoBox() {
        return ammoBox;
    }

    public void setAmmoBox(int[] ammoBox) {
        this.ammoBox = ammoBox;
    }

    public int getOldPosition() {

        return this.oldPosition;
    }


    public void addPowerup(PowerupCard newPowerUpCard) {

        if (this.powerup.size()<3)
            this.powerup.add(newPowerUpCard);
    }


    public ArrayList<Player> getDamageTrack() {
        return damage;
    }


    public ArrayList<PowerupCard> getPowerup() {
        return powerup;
    }


    @Override
    public String toString() {
        return "Player{" +
                ", lobby=" + lobby +
                ", ammoBox=" + Arrays.toString(ammoBox) +
                ", damage=" + damage +
                ", powerup=" + powerup +
                ", weaponCard=" + weaponCard +
                ", mark=" + mark +
                ", position=" + position +
                ", oldPosition=" + oldPosition +
                ", adrenalineState=" + adrenalineState +
                ", numOfActions=" + numOfActions +
                '}';
    }
}
