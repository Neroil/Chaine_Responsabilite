package mcr.gdx.dungeon.weapons.handlers;

import mcr.gdx.dungeon.weapons.AttackRequest;

import java.util.Random;

public class HitChanceHandler extends AttackHandler{
    private static final int HIT_CHANCE = 80;
    private static final Random random = new Random();


    @Override
    protected boolean handleAttackRequest(AttackRequest request) {
        int chance = random.nextInt(0, 101);

        // 80% chances to hit the enemy
        if(chance <= HIT_CHANCE){
            return invokeSuccessor(request);
        }else {
            return false;
        }
    }

}
