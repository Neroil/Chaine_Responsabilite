package mcr.gdx.dungeon.characters;

public class Player {

  // private Point position;
  private int mana;
  private int vigor;
  private int lifePoints;

  public Player(int mana, int vigor, int lifePoints) {
    this.mana = mana;
    this.vigor = vigor;
    this.lifePoints = lifePoints;
  }

  public int getMana() {
    return mana;
  }

  public int getVigor() {
    return vigor;
  }

  // Factoriser avec une super-classe Character -> reduceResource, pour mana ou vigor
  public void reduceVigor(int cost) { vigor -= cost; }

  public int getLifePoints() {
    return lifePoints;
  }

}
