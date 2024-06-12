package mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;

abstract public class AttackHandler extends Handler {

    @Override
    public boolean handleRequest(Request request) {
        return handleAttackRequest((AttackRequest) request);
    }

    protected abstract boolean handleAttackRequest(AttackRequest request);
}
