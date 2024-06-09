package mcr.gdx.dungeon.characters.handlers;

import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.Request;
import mcr.gdx.dungeon.characters.DamageRequest;

public abstract class DamageHandler extends GenericHandler {


    @Override
    public boolean handleRequest(Request request) {
        return handleDamageRequest((DamageRequest) request);
    }

    protected abstract boolean handleDamageRequest(DamageRequest request);
}
