package combat;

import static org.junit.Assert.*;
import org.junit.Test;

public class HealthComponentTest {

    @Test
    public void testDamage() {
        HealthComponent health = new HealthComponent(10);
        health.damage(4);
        assertEquals(6, health.getLife());
    }

    @Test
    public void testOverkill() {
        HealthComponent health = new HealthComponent(10);
        health.damage(999); // Overkill damage

        // This checks if your "if (life < 0) life = 0;" logic works
        assertEquals(0, health.getLife());
    }
}