package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.GenericHandler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.characters.DamageRequest;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.elements.items.weapons.physical.Fist;
import mcr.gdx.dungeon.weapons.AttackRequest;
import mcr.gdx.dungeon.weapons.handlers.CooldownHandler;
import mcr.gdx.dungeon.weapons.handlers.HitChanceHandler;

import java.util.LinkedList;

public class PlayerTile extends CharacterTile{
    private final static int MANA_MAX = 180;
    private final static int MANA_GAIN = 15;
    private final static int VIGOR_MAX = 60;
    private final static int VIGOR_GAIN = 30;

    private int mana;
    private int vigor;
    private final Game game;

    private WeaponTile weapon = new Fist(new Vector2(0, 0));
    private final LinkedList<ItemTile> attackItems;
    private final LinkedList<ItemTile> defenseItems;
    private GenericHandler attackChain;

    public PlayerTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game){
        super(position, texture, collidableEntities);
        this.mana = MANA_MAX;
        this.vigor = VIGOR_MAX;
        this.game = game;
        this.attackItems = new LinkedList<>();
        this.defenseItems = new LinkedList<>();
    }

    public void attack(){

        AttackRequest attack = new AttackRequest(this, weapon);
        if(attackChain.handleRequest(attack)){
            weapon.setLastAttack();
            LinkedList<Vector2> attackedPositions = new LinkedList<>();
            for(int i = 1; i <= weapon.getRange(); ++i){
                Vector2 attackedPos = new Vector2(position);
                attackedPositions.add(attackedPos.mulAdd(getFacingDirection(), i * Constants.TILE_SIZE));
            }
            DamageRequest damageRequest = new DamageRequest(weapon.getDamage(), attackedPositions, collidableEntities);
            requestDamage(damageRequest);
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
        GenericHandler chaining = attackChain;
        for(ItemTile i : attackItems){
            //TODO: trouver comment faire pour différencier les items (manaring et vigorring modifient les deux le cout sans distinction)
            chaining = chaining.setSuccessor(i.handler());
        }
        chaining.setSuccessor(weapon.handler()).setSuccessor(new CooldownHandler());
    }

    public void addAttackItem(ItemTile item){
        System.out.println("Picked up item!");
        this.attackItems.add(item);
    }

    public void addDefenseItem(ItemTile item){
        System.out.println("Picked up item!");
        this.defenseItems.add(item);
    }

    public void setWeapon(WeaponTile weapon){
        System.out.println("Picked up weapon!");
        this.weapon = weapon;
    }

    public WeaponTile getWeapon(){
        return weapon;
    }

//    public List<ItemTile> getItems(){
//        return Collections.unmodifiableList(items);
//    }

    @Override
    public void move(Vector2 direction, SpatialHashMap spatialHashMap) {
        super.move(direction, spatialHashMap);
        game.updateStep();
    }
}
