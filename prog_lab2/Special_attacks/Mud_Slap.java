package Special_attacks;

import ru.ifmo.se.pokemon.*;

public class Mud_Slap extends SpecialMove {
    public Mud_Slap(){
        super(Type.GROUND, 20,100);
    }
    protected void applyOppEffects(Pokemon p) {
        p.setCondition(new Effect().chance(1).attack(1).stat(Stat.ACCURACY, -1));
    }
    protected String describe(){
        return "использует специальную атаку Mud-Slap";
    }
}

