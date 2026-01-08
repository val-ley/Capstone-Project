package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class RandomSpawn {

    private final AssetManager assetManager;
    private final Node rootNode;
    private final BulletAppState physics;

    public RandomSpawn(AssetManager assetManager, Node rootNode, BulletAppState physics) {
        this.assetManager = assetManager; // store reference
        this.rootNode = rootNode;
        this.physics = physics;
    }

    // ---------- FALLING CUBE ----------
    public void createFallingCube() {
        Box box = new Box(1, 1, 1);
        Geometry cube = new Geometry("Cube", box); // need to change to a circle?maybe use particles or dont use physics
                                                   //change geometry to the othe rone.

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        cube.setMaterial(mat);
        
        ////////////// when adding the actual mesh
        /*        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        Material mat_default = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        teapot.setMaterial(mat_default);
        rootNode.attachChild(teapot);
        */

        // random X/Z spawn
        float x = (float)(Math.random() * 50f - 10f); //random generate where the cube is
        float z = (float)(Math.random() * 50f - 10f);

        cube.setLocalTranslation(x, 1, z);
        rootNode.attachChild(cube);

        new Collision(cube, physics, 1); // dynamic
    }
}