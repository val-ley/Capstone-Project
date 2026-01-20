package com.mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import java.util.concurrent.Callable;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;

public class Main extends SimpleApplication implements ActionListener, PhysicsCollisionListener {
    
    private MainMenu mainMenu;
    public boolean hideMenu;
    private boolean gameRunning = false;
        
    private Throw throwBox;
    
    private Timer timer;
    public boolean returnHideMenu () {
        return hideMenu;
    }
    
    private BulletAppState physics;
    private CharacterControl player;

    private boolean left, right, up, down, jump;
    private final Vector3f walkDir = new Vector3f();
    
    private RandomSpawn spawner;
    
    private BitmapText scoreBitmapText;
    private int score = 0;


    

    public static void main(String[] args) {
        new Main().start();
    }

    @Override
public void simpleInitApp() {

    // physics 
    physics = new BulletAppState();
    stateManager.attach(physics);

    //  collision listener
    physics.getPhysicsSpace().addCollisionListener(this);

    flyCam.setMoveSpeed(30);
    setupKeys();

    //  player and floor
    createPlayer();
    createFloor();

    // menu
    mainMenu = new MainMenu(this);
    mainMenu.init();

    //  box throwing
    throwBox = new Throw(assetManager, rootNode, physics);
    
    timer = new Timer(this, guiNode);
    spawner = new RandomSpawn(this);
}

    //  FLOOR 
    private void createFloor() {
     Spatial city = assetManager.loadModel("Scenes/houseblock.glb");
        
        city.setLocalTranslation(0, -2, 0);
        rootNode.attachChild(city);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        
        rootNode.addLight(sun);

        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 3);
        dlsr.setLight(sun);
        dlsr.setShadowIntensity(0.7f); //main shadows from main sun
        viewPort.addProcessor(dlsr);

        DirectionalLight fillLight = new DirectionalLight();
        fillLight.setDirection(new Vector3f(0.5f, -0.3f, 0.5f).normalizeLocal()); // opposite side from sun #1
        fillLight.setColor(ColorRGBA.White.mult(0.15f)); // makes shadows not 100% pitch black
        rootNode.addLight(fillLight);
        
        city.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        
        new Collision(city, physics, 0); 
}
    
    public void randomSpawn() {

        Vector3f[] spawnPoints = {
        new Vector3f(0, 1, -5),
        new Vector3f(2, 1, -5),
        new Vector3f(-2, 1, -5),
        new Vector3f(0, 1, -8)
    };

       for (int i = 0; i < spawnPoints.length; i++) {
        Spatial circle = assetManager.loadModel("Models/circle-3d.glb");
        circle.setLocalTranslation(spawnPoints[i]);
        rootNode.attachChild(circle);
    }


}

    
    //  PLAYER HITBOX
    private void createPlayer() {
    CapsuleCollisionShape shape = new CapsuleCollisionShape(0.5f, 3f);

    player = new CharacterControl(shape, 0.05f);
    player.setGravity(15);
    player.setJumpSpeed(10);

    Node playerNode = new Node("Player");
    playerNode.addControl(player);
    rootNode.attachChild(playerNode);

    player.setPhysicsLocation(new Vector3f(1, 1, -5));
    physics.getPhysicsSpace().add(player);
}
    
    ///////////////////collision listener////////////////////////////////////
    @Override
    public void collision(PhysicsCollisionEvent event) {
        
                BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        scoreBitmapText = new BitmapText(font, false);
        scoreBitmapText.setSize(font.getCharSet().getRenderedSize());
        scoreBitmapText.setText("Score: 0");
        scoreBitmapText.setLocalTranslation(10, cam.getHeight() - 10, 0);
        guiNode.attachChild(scoreBitmapText);
        
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();

        if ((a != null && a.getName() != null && a.getName().equals("box") &&
             b != null && b.getName() != null && b.getName().equals("target")) ||
            (b != null && b.getName() != null && b.getName().equals("box") &&
             a != null && a.getName() != null && a.getName().equals("target"))) {

            score++;
            System.out.println("Point scored! Total: " + score);

            // Update GUI
            scoreBitmapText.setText("Score: " + score);

            // Remove target
            if (a.getName().equals("target")) {
                rootNode.detachChild(a);
            } else {
                rootNode.detachChild(b);
            }
        }
    }

    //  INPUT 
    private void setupKeys() {
        //Movement
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump",  new KeyTrigger(KeyInput.KEY_SPACE));
        //Main Menu
        inputManager.addMapping("MenuStart", new KeyTrigger(KeyInput.KEY_RETURN));
        //Actions
        inputManager.addMapping("Throw", new KeyTrigger(KeyInput.KEY_E));
       
        inputManager.addListener(this, "Left", "Right", "Up", "Down", "Jump", "MenuStart", "Throw");
    }
    
    
    
    @Override
    public void onAction(String name, boolean pressed, float tpf) {
        if (name.equals("Left"))  left  = pressed;
        if (name.equals("Right")) right = pressed;
        if (name.equals("Up"))    up    = pressed;
        if (name.equals("Down"))  down  = pressed;
        if (name.equals("Jump")) jump = pressed;
        
        if (name.equals("MenuStart") && pressed) {
            mainMenu.disappear();
            startGame();
        }
        //box throw
        if (name.equals("Throw") && pressed) {
            throwBox.throwBox(cam);
        }
    }
    
    
    // REFRESH
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
        
        if (gameRunning && timer != null) {
        timer.update(tpf);
        
        //updating the spawn location
        if (gameRunning && timer != null) {
                timer.update(tpf);
                spawner.update(tpf);
            }


        }
    }
    
    
    public void startGame() {
        gameRunning = true;
    }

    public void stopGame() {
        gameRunning = false;
    }
    
    
}

