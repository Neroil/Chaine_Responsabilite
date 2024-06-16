package mcr.gdx.dungeon.elements;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import mcr.gdx.dungeon.ChainOfResponsibility.Handler;
import mcr.gdx.dungeon.Constants;
import mcr.gdx.dungeon.Game;
import mcr.gdx.dungeon.SpatialHashMap;
import mcr.gdx.dungeon.ChainOfResponsibility.damage.DamageRequest;
import mcr.gdx.dungeon.elements.items.WeaponTile;
import mcr.gdx.dungeon.elements.items.weapons.physical.Fist;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.AttackRequest;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.CooldownHandler;
import mcr.gdx.dungeon.ChainOfResponsibility.attack.handlers.HitChanceHandler;

import java.util.LinkedList;

/**
 * The PlayerTile class represents the player in the game. It extends the CharacterTile class and adds on top of it
 * the player's mana, vigor, attack items, defense items, weapon, and attack chain. It's also here that we handle the
 * player's attack, movement and his ressources (grabbing items).
 *
 * @version 1.0
 * @author Haeffner Edwin
 * @author Junod Arthur
 * @author Lopez Esteban
 * @author Ouadahi Yanis
 */
public class PlayerTile extends CharacterTile{
    private final static int MANA_MAX = 180;
    private final static int MANA_GAIN = 10;
    private final static int VIGOR_MAX = 80;
    private final static int VIGOR_GAIN = 20;
    private final static int HEALTH_MAX = 20;

    private int mana;
    private int vigor;

    private WeaponTile weapon = new Fist(new Vector2(0, 0));
    private final LinkedList<ItemTile> attackItems;
    private final LinkedList<ItemTile> defenseItems;
    private Handler attackChain;

    /**
     * Constructs a new PlayerTile with the specified position, texture, collidable entities, and game.
     * @param position              The position of the player
     * @param texture               The texture of the player
     * @param collidableEntities    The entities that the player can collide with
     * @param game                  The game that the player is in
     */
    public PlayerTile(Vector2 position, TextureRegion texture, LinkedList<CharacterTile> collidableEntities, Game game){
        super(position, texture, collidableEntities, game, HEALTH_MAX);
        this.mana = MANA_MAX;
        this.vigor = VIGOR_MAX;
        this.attackItems = new LinkedList<>();
        this.defenseItems = new LinkedList<>();
        createAttackChain();
    }

    /**
     * Returns the list of attack items the player has.
     * @return  The list of attack items
     */
    public LinkedList<ItemTile> getAttackItems(){
        return attackItems;
    }

    /**
     * Returns the list of defense items the player has.
     * @return  The list of defense items
     */
    public LinkedList<ItemTile> getDefenseItems(){
        return defenseItems;
    }

    /**
     * Exits the current level.
     */
    public void exitLevel(){
        game.exitLevel();
    }

    /**
     * Resets the player's stats and items to their initial state.
     */
    public void reset(){
        mana = MANA_MAX;
        vigor = VIGOR_MAX;
        healthPoint = HEALTH_MAX;
        weapon = new Fist(new Vector2(0, 0));
        attackItems.clear();
        defenseItems.clear();
        createAttackChain();
    }

    /**
     * Function to call when the player attacks, attacking will update the game's step.
     */
    public void attack(){

        System.out.println("Player attacking!");
        AttackRequest attack = new AttackRequest(this, weapon, game.getStep());

        // If the attack is successful, we get the positions of the entities that are going to be attacked
        if(attackChain.handleRequest(attack)){
            weapon.setLastAttack(game.getStep()); //Update the last time the weapon was used
            LinkedList<Vector2> attackedPositions = new LinkedList<>();
            //Create the list of positions that are going to be attacked since every weapons attacks in a line
            for(int i = 1; i <= weapon.getRange(); ++i){
                Vector2 attackedPos = new Vector2(position);
                attackedPositions.add(attackedPos.mulAdd(getFacingDirection(), i * Constants.TILE_SIZE));
            }
            DamageRequest damageRequest = new DamageRequest(attack.getWeaponDamage(), attackedPositions, collidableEntities);
            //Request damage on the attacked positions
            requestDamage(damageRequest);
        }

        game.updateStep();
    }

    /**
     * Returns the current mana of the player.
     * @return  The current mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Returns the current vigor of the player.
     * @return  The current vigor
     */
    public int getVigor() {
        return vigor;
    }

    /**
     * Returns the maximum mana of the player.
     * @return  The maximum mana
     */
    public int getManaMax(){
        return MANA_MAX;
    }

    /**
     * Returns the maximum vigor of the player.
     * @return  The maximum vigor
     */
    public int getVigorMax(){
        return VIGOR_MAX;
    }

    /**
     * Reduces the player's vigor by the specified cost, it's not possible to have less than 0 vigor.
     * @param cost  The cost to reduce the vigor by
     */
    public void reduceVigor(int cost) {
        if(vigor - cost < 0){
            vigor = 0;
        }else {
            vigor -= cost;
        }
    }

    /**
     * Reduces the player's mana by the specified cost, it's not possible to have less than 0 mana.
     * @param cost  The cost to reduce the mana by
     */
    public void reduceMana(int cost){
        if(mana - cost < 0){
            mana = 0;
        }else {
            mana -= cost;
        }
    }

    /**
     * Updates the player's resources (mana and vigor).
     */
    public void updateRessources(){
        System.out.println("Updating ressources!");
        if(mana + MANA_GAIN <= MANA_MAX){
            mana += MANA_GAIN;
        }
        if(vigor + VIGOR_GAIN <= VIGOR_MAX){
            vigor += VIGOR_GAIN;
        }
    }

    /**
     * Function to call when the player picks up an item, it creates a new attack chain everytime a new item is picked up.
     * @param item  The item that the player picked up
     */
    public void pickUpItem(ItemTile item){
        item.pickUp(this);
        // Reset the chain of attack when a new item is picked up
        createAttackChain();
    }

    /**
     * Creates the chain of attack handlers for the player.
     */
    private void createAttackChain(){
        attackChain = new CooldownHandler();
        Handler chaining = attackChain.setSuccessor(new HitChanceHandler());
        for(ItemTile i : attackItems){
            chaining = chaining.setSuccessor(i.handler());
        }
        chaining.setSuccessor(weapon.handler());
    }

    /**
     * Adds the specified item to the player's attack items.
     * @param item  The item to add
     */
    public void addAttackItem(ItemTile item){
        System.out.println("Picked up item!");
        this.attackItems.add(item);
    }

    /**
     * Adds the specified item to the player's defense items.
     * @param item  The item to add
     */
    public void addDefenseItem(ItemTile item){
        System.out.println("Picked up item!");
        this.defenseItems.add(item);
    }

    /**
     * Sets the player's weapon to the specified weapon.
     * @param weapon  The weapon to set
     */
    public void setWeapon(WeaponTile weapon){
        System.out.println("Picked up weapon!");
        this.weapon = weapon;
    }

    /**
     * Returns the player's current weapon.
     * @return  The current weapon
     */
    public WeaponTile getWeapon(){
        return weapon;
    }

    /**
     * Moves the player in the specified direction and updates the game's step.
     * @param direction         The direction to move in
     * @param spatialHashMap    The spatial hash map of the game for collision detection
     */
    @Override
    public void move(Vector2 direction, SpatialHashMap spatialHashMap) {
        super.move(direction, spatialHashMap);
        game.updateStep();
    }
}
