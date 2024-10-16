package Status_attacks;
import ru.ifmo.se.pokemon.*;

public class Confide extends StatusMove {
    public Confide() {
        super(Type.NORMAL, 0,100);
    }
    protected void applyOppEffects(Pokemon p){
        p.setCondition(new Effect().turns(1).stat(Stat.DEFENSE, -1));
    }
    protected String describe(){
        return "использует атаку Confide";
    }
}



