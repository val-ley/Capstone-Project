package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.renderer.Camera;
import com.jme3.material.Material;
import com.jme3.texture.Texture;


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

    Spatial box = assetManager.loadModel("Models/deliverybox-sprytile.glb");

    // --- MATERIAL ---
    Material mat = new Material(
        assetManager,
        "Common/MatDefs/Light/Lighting.j3md"
    );

    Texture tex = assetManager.loadTexture("Textures/deliverybox-texture.png");
    mat.setTexture("DiffuseMap", tex);

    // Required for Lighting.j3md
    mat.setBoolean("UseMaterialColors", true);
    mat.setColor("Diffuse", com.jme3.math.ColorRGBA.White);
    mat.setColor("Specular", com.jme3.math.ColorRGBA.Black);
    

    // APPLY MATERIAL TO ALL GEOMETRIES
    box.depthFirstTraversal(spatial -> {
        if (spatial instanceof com.jme3.scene.Geometry g) {
            g.setMaterial(mat);
        }
    });

    // Spawn position
    Vector3f spawnPos = cam.getLocation()
            .add(cam.getDirection().mult(1.2f));
    box.setLocalTranslation(spawnPos);

    // Physics
    BoxCollisionShape shape =
            new BoxCollisionShape(new Vector3f(0.25f, 0.25f, 0.25f));
    RigidBodyControl physics = new RigidBodyControl(shape, 1f);
    box.addControl(physics);

    rootNode.attachChild(box);
    bulletAppState.getPhysicsSpace().add(physics);

    physics.setLinearVelocity(cam.getDirection().mult(20f));
}


}
