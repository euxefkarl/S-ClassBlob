package combat;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Rectangle;

// imports from your project
import entity.Entity;
import entity.Player;
import main.GamePanel;

public class CombatComponentTest {

    private GamePanel gp;
    private Player player;
    private DamageCalculator mockCalculator;
    private CombatComponent combat;

    @Before
    public void setUp() {
        // use a simplified setup to avoid graphics initialization
        gp = new main.FakeGamePanel();

        // create a dummy player entity
        player = new Player(gp, null);
        player.worldX = 100;
        player.worldY = 100;
        player.direction = "down";

        // anonymous inner class for a simple calculator mock
        mockCalculator = (attacker, defender) -> 5;

        combat = new CombatComponent(gp, player, mockCalculator);
    }

    @Test
    public void testInitialState() {
        // verify combat starts in READY state
        assertEquals(CombatComponent.CombatState.READY, combat.getState());
        assertFalse(combat.isAttacking());
    }

    @Test
    public void testTryAttackTransitionsToWindup() {
        // trigger attack
        boolean success = combat.tryAttack();

        // verify success and state change
        assertTrue(success);
        assertEquals(CombatComponent.CombatState.WINDUP, combat.getState());
        assertTrue(combat.isAttacking());
    }

    @Test
    public void testFullStateCycle() {
        combat.tryAttack(); // state: WINDUP (timer 0)

        // 1. transition from WINDUP to ACTIVE
        // default windupDuration is 5
        for (int i = 0; i <= 5; i++) {
            combat.update();
        }
        assertEquals(CombatComponent.CombatState.ACTIVE, combat.getState());

        // 2. transition from ACTIVE to COOLDOWN
        // default activeDuration is 25
        for (int i = 0; i <= 25; i++) {
            combat.update();
        }
        assertEquals(CombatComponent.CombatState.COOLDOWN, combat.getState());

        // 3. transition from COOLDOWN back to READY
        // default cooldownDuration is 5
        for (int i = 0; i <= 5; i++) {
            combat.update();
        }
        assertEquals(CombatComponent.CombatState.READY, combat.getState());
        assertFalse(combat.isAttacking());
    }

    @Test
    public void testHitboxPositionDown() {
        player.direction = "down";
        combat.updateHitboxPosition();

        Rectangle hb = combat.getAttackHitbox();

        // worldY(100) + tileSize(48) = 148
        assertEquals(148, hb.y);
        // centered: worldX(100) + (48-48)/2 = 100
        assertEquals(100, hb.x);
    }

    @Test
    public void testHitboxPositionUp() {
        player.direction = "up";
        combat.updateHitboxPosition();

        Rectangle hb = combat.getAttackHitbox();

        // worldY(100) - height(48) + 10 = 62
        assertEquals(62, hb.y);
    }

    @Test
    public void testSetAttackSpeedChangesDurations() {
        // change speeds to 1 frame each
        combat.setAttackSpeed(1, 1, 1);
        combat.tryAttack(); // WINDUP

        combat.update();
        combat.update(); // stateTimer becomes 2, > windupDuration(1)

        assertEquals(CombatComponent.CombatState.ACTIVE, combat.getState());
    }
}