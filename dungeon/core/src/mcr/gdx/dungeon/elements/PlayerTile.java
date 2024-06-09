package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sun.tools.javac.jvm.Gen;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.characters.DamageRequest;
import mcr.gdx.dungeon.elements.items.Fist;
import mcr.gdx.dungeon.weapons.AttackRequest;
import mcr.gdx.dungeon.weapons.handlers.AttackHandler;
import mcr.gdx.dungeon.weapons.handlers.CooldownHandler;
import mcr.gdx.dungeon.weapons.handlers.HitChanceHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlayerTile extends CharacterTile{
    private final static int MANA_MAX = 180;
    private final static int MANA_GAIN = 15;
    private final static int VIGOR_MAX = 60;
    private final static int VIGOR_GAIN = 30;

    private int mana;
    private int vigor;
    private final Game game;

    private WeaponTile weapon = new Fist(new Vector2(0, 0));
    private final LinkedList<ItemTile> items;
    private GenericHandler attackChain;

    public PlayerTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game){
        super(position, texture, collidableEntities);
        this.mana = MANA_MAX;
        this.vigor = VIGOR_MAX;
        this.game = game;
        this.items = new LinkedList<>();
    }

    public void attack(){

        AttackRequest attack = new AttackRequest(this, weapon);
        if(attackChain.handleRequest(attack)){
            LinkedList<CharacterTile> targets = new LinkedList<>();
            for(int i = 1; i <= weapon.getRange(); ++i){
                Vector2 attackedPos = new Vector2(position);
                attackedPos.mulAdd(getFacingDirection(), i * Constants.TILE_SIZE);
                for(CharacterTile entity : collidableEntities){
                    if(entity.position.equals(attackedPos)){
                        targets.add(entity);
                    }
                }
            }
            for(CharacterTile target : targets){
                //DamageRequest damage = new DamageRequest();
                //target.hit(damage);
            }
        }
    }

    public int getMana() {
        return mana;
    }

    public int getVigor() {
        return vigor;
    }

    public int getManaMax(){
        return MANA_MAX;
    }

    public int getVigorMax(){
        return VIGOR_MAX;
    }


    public void reduceVigor(int cost) {
        if(vigor - cost < 0){
            vigor = 0;
        }else {
            vigor -= cost;
        }
    }

    public void reduceMana(int cost){
        if(mana - cost < 0){
            mana = 0;
        }else {
            mana -= cost;
        }
    }

    public void updateRessources(){
        if(mana + MANA_GAIN <= MANA_MAX){
            mana += MANA_GAIN;
        }
        if(vigor + VIGOR_GAIN <= VIGOR_MAX){
            vigor += VIGOR_GAIN;
        }
    }

    public void pickUpItem(ItemTile item){
        item.pickUp(this);
        // Reset the chain of attack when a new item is picked up
        attackChain = new HitChanceHandler();
        attackChain.setSuccessor(new HitChanceHandler());
        for(ItemTile i : items){
            //TODO: trouver comment faire pour différencier les items (manaring et vigorring modifient les deux le cout sans distinction)
            attackChain.setSuccessor(i.handler());
        }
        attackChain.setSuccessor(weapon.handler()).setSuccessor(new CooldownHandler());
    }

    public void addItem(ItemTile item){
        System.out.println("Picked up item!");
        this.items.add(item);
    }

    public void setWeapon(WeaponTile weapon){
        System.out.println("Picked up weapon!");
        this.weapon = weapon;
    }

    public WeaponTile getWeapon(){
        return weapon;
    }

    public List<ItemTile> getItems(){
        return Collections.unmodifiableList(items);
    }

    @Override
    public void move(Vector2 direction, SpatialHashMap spatialHashMap) {
        super.move(direction, spatialHashMap);
        game.updateStep();
    }
}
