package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;

public class RandomSpawn {

    private final AssetManager assetManager;
    private final Node rootNode;
    private final BulletAppState physics;

    // Predefined spawn points
    private final Vector3f[] spawnPoints = {
        new Vector3f(0, 5, 0),
        new Vector3f(5, 5, 5),
        new Vector3f(-5, 5, 10),
        new Vector3f(10, 5, -5)
    };
    
    public RandomSpawn(AssetManager assetManager, Node rootNode, BulletAppState physics) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.physics = physics;
    }

    public void createRandomSpawn() {
        // Pick a random spawn point
        Vector3f point = spawnPoints[(int) (Math.random() * spawnPoints.length)];

        // Create cube
        Box box = new Box(1, 1, 1); // smaller and easier to see
        Geometry cube = new Geometry("Cube", box);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White); // visible
        cube.setMaterial(mat);

        cube.setLocalTranslation(point);

        // Remove cube after 5 seconds
        cube.addControl(new AbstractControl() {
            float timeLeft = 5f;

            @Override
            protected void controlUpdate(float tpf) {
                timeLeft -= tpf;
                if (timeLeft <= 0f) {
                    spatial.removeFromParent();
                }
            }

            @Override
            protected void controlRender(com.jme3.renderer.RenderManager rm,
                                         com.jme3.renderer.ViewPort vp) {
            }
        });

        rootNode.attachChild(cube);

        new Collision(cube, physics, 1);
    }
}
