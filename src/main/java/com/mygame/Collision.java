package com.mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

public class Collision {

    private final RigidBodyControl body;

    public Collision(Spatial spatial, BulletAppState physics, float mass) {

        CollisionShape shape;

        if (mass == 0) {
            // static object (target)
            shape = CollisionShapeFactory.createMeshShape(spatial);
        } else {
            // dynamic object (box)
            shape = CollisionShapeFactory.createDynamicMeshShape(spatial);
        }

        body = new RigidBodyControl(shape, mass);
        spatial.addControl(body);
        physics.getPhysicsSpace().add(body);
    }

    public RigidBodyControl getBody() {
        return body;
    }
}
