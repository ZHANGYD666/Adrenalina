import java.util.ArrayList;
import java.util.Optional;

public class Firemode {
    private int extraCost;
    private int targetLimit;
    private ArrayList<RangeConstraint> rngConstraints;
    private ArrayList<TargetsConstraint> trgConstraints;
    private ArrayList<Integer[]> dmgmrkToEachTarget;

    public int getExtraCost(){
        return extraCost;
    }

    /* returns list of INVALID squares for target: targets cannot be in one of these squares */
    public ArrayList<Integer> getRange(int shooterPosition){
        ArrayList<Integer> invalidSquares = new ArrayList<Integer>();
        for(RangeConstraint rc : rngConstraints){
            invalidSquares.addAll(rc.checkConst(shooterPosition));
        }
        return invalidSquares;
    }


    public ArrayList<Integer[]> fire(ArrayList<Player> targets, ArrayList<Integer> invalidSquares) throws InvalidTargetsException{
        for(Player trg : targets) {
            if (invalidSquares.contains(trg.getPosition())){
                if(trgConstraints.stream().noneMatch(TargetsConstraint::isSpecialRange)) throw new InvalidTargetsException();
            }
        }
        for(TargetsConstraint trgconst : trgConstraints){
            if(!trgconst.checkConst(targets)) throw new InvalidTargetsException();
        }
        /*TARGETS VALID*/
        ArrayList<Integer[]> returnToEachTarget = new ArrayList<>();
        Integer[] clone;
        for(Player trg : targets) {
            clone = dmgmrkToEachTarget.get(targets.indexOf(trg) < dmgmrkToEachTarget.size() ? targets.indexOf(trg) : dmgmrkToEachTarget.size()-1);
            returnToEachTarget.add(clone);

        }
        return returnToEachTarget;
    }

}
