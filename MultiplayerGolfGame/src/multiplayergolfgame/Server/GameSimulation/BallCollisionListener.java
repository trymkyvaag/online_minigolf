package multiplayergolfgame.Server.GameSimulation;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.BroadphaseCollisionData;
import org.dyn4j.world.ManifoldCollisionData;
import org.dyn4j.world.NarrowphaseCollisionData;
import org.dyn4j.world.listener.CollisionListener;

import multiplayergolfgame.framework.GameObject;

/**
 *
 * @author James Eastwood
 */
public class BallCollisionListener implements CollisionListener<GameObject, BodyFixture> {

    @Override
    public boolean collision(BroadphaseCollisionData<GameObject, BodyFixture> collision) {
        /* I have to put this in to appease the phsyics gods. */
        return true;
    }

    @Override
    public boolean collision(NarrowphaseCollisionData<GameObject, BodyFixture> collision) {
        return true;
    }

    @Override
    public boolean collision(ManifoldCollisionData<GameObject, BodyFixture> collision) {
        /* I have to put this in to appease the phsyics gods. */
        return true;
    }
}
