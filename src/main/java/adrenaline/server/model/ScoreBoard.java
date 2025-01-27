package adrenaline.server.model;

import adrenaline.Color;
import adrenaline.ScoreboardUpdateMessage;
import adrenaline.server.Observable;
import adrenaline.server.network.Client;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreBoard extends Observable {
    private HashMap<Color,Integer> scoreMap = new HashMap<>();
    private HashMap<Color,Integer> diminValues = new HashMap<>();
    private ArrayList<Color> killshotTrack;
    private ArrayList<Boolean> overkillFlags;
    private int maxKills;
    private ArrayList<Color> finalfrenzyDeadPlayers = new ArrayList<>();
    private HashMap<Color,Integer> finalfrenzyModePlayers = new HashMap<>();

    private LinkedHashMap<Color, Integer> finalPlayerPositions = null;

    public ScoreBoard(ArrayList<Client> clients){
        clients.forEach(this::attach);
    }

    public void initPlayerScore(Color color){
        scoreMap.put(color, 0);
        diminValues.put(color, 8);
    }

    public void initKillshotTrack(int skulls){
        killshotTrack = new ArrayList<>(skulls);
        overkillFlags = new ArrayList<>(skulls);
        maxKills = skulls;
        notifyObservers(new ScoreboardUpdateMessage(this));
    }


    public void scoreKill(Color dead, ArrayList<Color> damageTrack){
        if(!finalfrenzyDeadPlayers.contains(dead)) {
            int firstBlood = scoreMap.get(damageTrack.get(0));
            scoreMap.put(damageTrack.get(0), firstBlood + 1);
        }
        List<Integer> frequencies = damageTrack.stream().map(x -> Collections.frequency(damageTrack, x)).distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Color> attackers = damageTrack.stream().distinct().collect(Collectors.toList());
        int points = diminValues.get(dead);
        for (int x : frequencies) {
            for (Color c : attackers) {
                if (Collections.frequency(damageTrack, c) == x) {
                    int score = scoreMap.get(c);
                    scoreMap.put(c, score + points);
                    points = (points - 2) < 1 ? 1 : points - 2;
                }
            }
        }
        points = diminValues.get(dead);
        diminValues.put(dead, (points-2)<1 ? 1 : points-2);
        if(damageTrack.size()>=11) {
            killshotTrack.add(damageTrack.get(10));
            overkillFlags.add(damageTrack.size() >= 12);
        }
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public void scoreDoubleKill(Color killer){
        int score = scoreMap.get(killer);
        scoreMap.put(killer, score+1);
    }

    public boolean gameEnded(){
        return (maxKills==killshotTrack.size());
    }

    public HashMap<Color, Integer> getDiminValues() {
        return diminValues;
    }

    public List<Color> getKillshotTrack() {
        return killshotTrack;
    }

    public List<Boolean> getOverkillFlags() {
        return overkillFlags;
    }

    public HashMap<Color,Integer> getScoreMap() { return scoreMap; }

    public java.util.Map<Color, Integer> getFinalPlayerPositions() { return finalPlayerPositions; }

    public int getMaxKills() { return maxKills; }

    public void setFinalFrenzyMode(Color player, boolean firstPlayerFF){
        finalfrenzyModePlayers.put(player, firstPlayerFF? 2 : 1);
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public void setFinalFrenzyValues(Color player){
        diminValues.put(player,2);
        finalfrenzyDeadPlayers.add(player);
        notifyObservers(new ScoreboardUpdateMessage(this));
    }

    public HashMap<Color, Integer> getFinalfrenzyModePlayers() {
        return finalfrenzyModePlayers;
    }

    public void scoreKillshotTrack(List<Color> inactive) {
        inactive.forEach(x -> scoreMap.remove(x));

        finalPlayerPositions = new LinkedHashMap<>();
        HashMap<Color,Integer> pointsFromKillshoTrack = new HashMap<>();
        HashMap<Color,Integer> tokensOnKillshotTrack = new HashMap<>();
        scoreMap.keySet().forEach(x -> {
            pointsFromKillshoTrack.put(x,0);
            tokensOnKillshotTrack.put(x,0);
        });

        for(int i=0;i<killshotTrack.size();i++){
            int tokens = tokensOnKillshotTrack.get(killshotTrack.get(i));
            tokensOnKillshotTrack.put(killshotTrack.get(i), tokens+(overkillFlags.get(i)? 2 : 1));
        }
        List<Integer> orderedList = tokensOnKillshotTrack.values().stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int points = 8;
        for(Integer x : orderedList) {
            for (Color c : killshotTrack) {
                if(tokensOnKillshotTrack.get(c).equals(x)){
                    pointsFromKillshoTrack.put(c, (points-2 < 1)? 1 : points);
                    points-=2;
                }
            }
        }
        pointsFromKillshoTrack.forEach((x,y)-> {
            int score = scoreMap.get(x);
            scoreMap.put(x,score+y);
        });
        orderedList = scoreMap.values().stream().distinct().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int position=1;
        Color previous=null;
        for(Integer x : orderedList){
            for(java.util.Map.Entry<Color,Integer> entry : scoreMap.entrySet()){
                if(entry.getValue().equals(x)){
                    Color current = entry.getKey();
                    finalPlayerPositions.put(entry.getKey(),position);
                    if(previous!=null && finalPlayerPositions.get(previous).equals(finalPlayerPositions.get(current))){
                        if(pointsFromKillshoTrack.get(current)>pointsFromKillshoTrack.get(previous)){
                            position++;
                            finalPlayerPositions.put(previous, position);
                        }else if(pointsFromKillshoTrack.get(current)<pointsFromKillshoTrack.get(previous)){
                            position++;
                            finalPlayerPositions.put(current, position);
                        }

                    }
                    previous = current;
                }
            }
            position++;
        }
        System.out.println(scoreMap+"\n"+finalPlayerPositions);
        notifyObservers(new ScoreboardUpdateMessage(this));
    }
}

