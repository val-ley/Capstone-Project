package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.Node;

public class RandomSpawn {

    private final SimpleApplication app;
    private final Node rootNode;
    private final AssetManager assetManager;

    private final Vector3f[] spawnPoints = {
        new Vector3f(0, 1, -5),
        new Vector3f(2, 1, -5),
        new Vector3f(-2, 1, -5),
        new Vector3f(0, 1, -8)
    };

    private int i = 0; 
    private float timer = 0; 
    private Spatial currentCircle = null; 
    private final float displayTime = 5f; 

    public RandomSpawn(SimpleApplication app) {
        this.app = app;
        this.rootNode = app.getRootNode();
        this.assetManager = app.getAssetManager();
    }

    public void update(float tpf) {
        timer += tpf;

        if (timer >= displayTime) {
            if (currentCircle != null) {
                rootNode.detachChild(currentCircle);
            }

            currentCircle = assetManager.loadModel("Models/circle-3d.glb");
            currentCircle.setLocalScale(5f);
            currentCircle.setName("target");

            currentCircle.setLocalTranslation(spawnPoints[i]);
            rootNode.attachChild(currentCircle);

            // move to the next spawn point
            i++;
            if (i >= spawnPoints.length) {
                i = 0; // loop back to start, not random but it works
            }

            timer = 0; 
        }
    }
}
