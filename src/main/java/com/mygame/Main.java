package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.bullet.BulletAppState;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Main extends SimpleApplication implements ActionListener {

    private BulletAppState physics;
    private CharacterControl player;

    private boolean left, right, up, down, jump;
    private final Vector3f walkDir = new Vector3f();

    public static void main(String[] args) {
        new Main().start();
    }

    @Override
    public void simpleInitApp() {

        physics = new BulletAppState();
        stateManager.attach(physics);

        flyCam.setMoveSpeed(1);
        setupKeys();

        createFloor();
        createFallingCube();
        createPlayer();
    }

    // ---------- FLOOR ----------
    private void createFloor() {
        Box floorBox = new Box(200, 1, 200);
        Geometry floor = new Geometry("Floor", floorBox);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(mat);

        floor.setLocalTranslation(0, -1, 0);
        rootNode.attachChild(floor);

        new Collision(floor, physics, 0); // static
    }

    // ---------- FALLING CUBE ----------
    private void createFallingCube() {
        Box box = new Box(1, 1, 1);
        Geometry cube = new Geometry("Cube", box);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        cube.setMaterial(mat);

        cube.setLocalTranslation(0, 30, 0);
        rootNode.attachChild(cube);

        new Collision(cube, physics, 1); // dynamic
    }

    // ---------- PLAYER ----------
    private void createPlayer() {
        CapsuleCollisionShape shape =
                new CapsuleCollisionShape(0.5f, 1.8f);

        player = new CharacterControl(shape, 0.05f);
        player.setGravity(5);
        player.setJumpSpeed(1);
        player.setPhysicsLocation(new Vector3f(3, 5, 0));

        physics.getPhysicsSpace().add(player);
    }

    // ---------- INPUT ----------
    private void setupKeys() {
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump",  new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this,
                "Left", "Right", "Up", "Down", "Jump");
    }

    @Override
    public void onAction(String name, boolean pressed, float tpf) {
        if (name.equals("Left"))  left  = pressed;
        if (name.equals("Right")) right = pressed;
        if (name.equals("Up"))    up    = pressed;
        if (name.equals("Down"))  down  = pressed;

        if (name.equals("Jump")) jump = pressed;
    }

    // ---------- UPDATE ----------
    @Override
    public void simpleUpdate(float tpf) {

        walkDir.set(0, 0, 0);

        if (left)  walkDir.addLocal(cam.getLeft());
        if (right) walkDir.addLocal(cam.getLeft().negate());
        if (up)    walkDir.addLocal(cam.getDirection());
        if (down)  walkDir.addLocal(cam.getDirection().negate());
        if (jump) player.jump();

        walkDir.y = 0;
        player.setWalkDirection(walkDir.mult(0.3f));

        cam.setLocation(player.getPhysicsLocation().add(0, 1.5f, 0));
    }
}
