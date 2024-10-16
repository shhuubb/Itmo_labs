package Status_attacks;
import ru.ifmo.se.pokemon.*;

public class Leer extends StatusMove {
    public Leer() {
        super(Type.NORMAL, 0,100);
    }
    protected void applyOppEffects(Pokemon p){
        p.setCondition(new Effect().turns(1).attack(0).stat(Stat.SPECIAL_ATTACK, -1));
    }
    protected String describe(){
        return "использует атаку Confide";
    }
}



