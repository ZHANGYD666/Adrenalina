package server.model.constraints;

import server.model.Map;
import server.model.PlayerCore;

import java.util.ArrayList;

public class ChargeConstraint extends TargetsConstraint {
    private static boolean specialRange = true;

    @Override
    public boolean checkConst(PlayerCore shooter, ArrayList<PlayerCore> targets, Map map) {
        if(!map.areAligned(shooter.getPosition(), shooter.getOldPosition())) return false;
        for(PlayerCore trg : targets) {
            if (!map.areAligned(shooter.getOldPosition(), trg.getPosition()) || (!map.areAligned(shooter.getPosition(), trg.getPosition())) ||
                    (shooter.getPosition()>= shooter.getOldPosition() && (trg.getPosition()> shooter.getPosition() || trg.getPosition() < shooter.getOldPosition())) ||
                    (shooter.getPosition()<= shooter.getOldPosition() && (trg.getPosition()< shooter.getPosition() || trg.getPosition() > shooter.getOldPosition()))) return false;
        }
        return true;
    }
}
