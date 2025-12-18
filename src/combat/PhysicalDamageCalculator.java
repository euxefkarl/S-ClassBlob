package combat;

public class PhysicalDamageCalculator implements DamageCalculator {

    @Override
    public int calculate(Attacker attacker, Defender defender) {
        int damage = attacker.getAttackPower() - defender.getDefense();
        return Math.max(0, damage);
    }
}
