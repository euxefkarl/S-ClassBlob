package combat;

import entity.Entity;

public class CombatComponent {

    public final Entity owner;
    private final DamageCalculator calculator;

    public CombatComponent(Entity owner, DamageCalculator calculator) {
        this.owner = owner;
        this.calculator = calculator;
    }

    public void attack(Entity target) {
        if (!(target instanceof Damagable damageable)) return;

        int damage = calculator.calculate((Attacker) owner, (Defender) target);
        damageable.takeDamage(damage, owner);
    }
}
