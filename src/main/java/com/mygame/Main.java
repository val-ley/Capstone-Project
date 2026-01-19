package com.mygame;

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

public class Main extends SimpleApplication implements ActionListener {
    
    private MainMenu mainMenu;
    public boolean hideMenu;
    
    private Throw throwBox;
    
    public boolean returnHideMenu () {
        return hideMenu;
    }
    
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
        stateManager.attach(physics); //create this before attaching the cube
                
        RandomSpawn RandomSpawn = new RandomSpawn(assetManager, rootNode, physics);
        
        RandomSpawn.createRandomSpawn();

        flyCam.setMoveSpeed(1);
        setupKeys();

        createFloor();
       createPlayer();
        
        //test();
        //Main menu stuff
        mainMenu = new MainMenu(this);
        mainMenu.init();
        
        //throwing box 
        throwBox = new Throw(assetManager, rootNode, physics);
        
        
    }
    
    //  TEST 
//   private void test() {
//        Spatial boxtest = assetManager.loadModel("Models/deliverybox.glb");
//        Material mat_default = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        boxtest.setMaterial(mat_default);
//        rootNode.attachChild(boxtest);
//    }

    //  FLOOR 
    private void createFloor() {
         //////// //////// //////// //////// ////////
//        Box floor = new Box(100, 1, 100); // create cube shape
//        Geometry floors = new Geometry("Floor", floor);  // create cube geometry from the shape
//        Material mat = new Material(assetManager,
//          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
//        mat.setColor("Color", ColorRGBA.White);   // set color of material to blue
//        floors.setMaterial(mat);                   // set the cube's material
//        rootNode.attachChild(floors);              //
        
        //////// //////// //////// //////// ////////
     Spatial city = assetManager.loadModel("Scenes/houseblock.glb");
        
        city.setLocalTranslation(0, -1, 0);
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

   

    //  PLAYER HITBOX
    private void createPlayer() {
    CapsuleCollisionShape shape =
            new CapsuleCollisionShape(0.5f, 1.8f);

    player = new CharacterControl(shape, 0.05f);
    player.setGravity(10);
    player.setJumpSpeed(5);

    Node playerNode = new Node("Player");
    playerNode.addControl(player);
    rootNode.attachChild(playerNode);

    player.setPhysicsLocation(new Vector3f(1, 1, 0));
    physics.getPhysicsSpace().add(player);
    
   
}



    //  INPUT 
    private void setupKeys() {
        inputManager.addMapping("Left",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up",    new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump",  new KeyTrigger(KeyInput.KEY_SPACE));
        
        inputManager.addMapping("MenuStart", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(this, "MenuStart");
        
        inputManager.addMapping("Throw", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addListener(this, "Throw");


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
        
        
        //start of the game
        if (name.equals("MenuStart") && pressed) {
            mainMenu.disappear();
            hideMenu = true;
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
        
    }
}
