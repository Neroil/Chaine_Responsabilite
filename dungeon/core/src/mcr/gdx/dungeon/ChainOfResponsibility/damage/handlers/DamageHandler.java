package mcr.gdx.dungeon.ChainOfResponsibility.damage.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;

public abstract class DamageHandler extends Handler {


    @Override
    public boolean handleRequest(Request request) {
        return handleDamageRequest((DamageRequest) request);
    }

    protected abstract boolean handleDamageRequest(DamageRequest request);
}
