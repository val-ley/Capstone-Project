package com.mygame;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.scene.Node;
import com.mygame.Collision;

public class RandomSpawn {

    private BulletAppState physics;
    private final AssetManager assetManager;
    private final Node rootNode;

    public RandomSpawn(AssetManager assetManager, Node rootNode) { //use the vars above. learn why u use this??
    this.assetManager = assetManager;
    this.rootNode = rootNode;
    }
  

    // ---------- FALLING CUBE ----------
    public void createFallingCube() {
            Box box = new Box(1, 1, 1);
            Geometry cube = new Geometry("Cube", box);

            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Red);
            cube.setMaterial(mat);

            cube.setLocalTranslation(0, 30, 0);
            rootNode.attachChild(cube);

            new Collision(cube, physics, 1); // dynamic
    }
}
