package combat;

import entity.Entity;

public interface Damagable {
    void takeDamage(int amount, Entity attacker);
}
