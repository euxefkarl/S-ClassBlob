package combat;

public class HealthComponent {

    private final int maxLife;
    private int life;

    public HealthComponent(int maxLife) {
        this.maxLife = maxLife;
        this.life = maxLife;
    }

    public void damage(int amount) {
        life -= amount;
        if (life < 0) life = 0;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public int getLife() {
        return life;
    }

    public int getMaxLife() {
        return maxLife;
    }
}
