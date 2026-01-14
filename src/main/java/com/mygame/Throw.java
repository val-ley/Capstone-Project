package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.renderer.Camera;

public class Throw {

    private final AssetManager assetManager;
    private final Node rootNode;
    private final BulletAppState bulletAppState;

    public Throw(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
    }

    public void throwBox(Camera cam) {

    Spatial box = assetManager.loadModel("Models/deliverybox.glb");

    box.scale(0.5f); // adjust to your GLB size

    // Spawn in front of camera
    Vector3f spawnPos = cam.getLocation().add(cam.getDirection().mult(1.2f));
    box.setLocalTranslation(spawnPos);

    // Physics
    BoxCollisionShape shape = new BoxCollisionShape(new Vector3f(0.25f, 0.25f, 0.25f));
    RigidBodyControl physics = new RigidBodyControl(shape, 1f);
    box.addControl(physics);

    rootNode.attachChild(box);
    bulletAppState.getPhysicsSpace().add(physics);

    // Shoot forward
    physics.setLinearVelocity(cam.getDirection().mult(20f));
}

}
