package com.mygame;

import com.mygame.Main;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.ActionListener;

public class MainMenu {

    private final Main app;
    
    private final Node guiNode;

    //for buttons
    private Picture playButton;
    private Picture settingsButton;
    private Picture instructionsButton;
    
    //Title
    private Picture Title;


    //for city spinning image
    private Picture cityspin;

    private Geometry background;
    private BitmapText title, start, quit;

    public MainMenu(Main app) {
        this.app = app;
        this.guiNode = app.getGuiNode();
    }

    public void init() {
        float w = app.getCamera().getWidth();
        float h = app.getCamera().getHeight();

        createBackground(w, h);

        ///////////////////////CITY LOOP ANIMATION///////////////////////////////
        //node for city loop
        Node cityTurn = new Node("cityTurn");

        //city loop + animations 
        cityspin = new Picture("cityspin");
        cityspin.setImage(app.getAssetManager(), "Interface/city-spin.png", true);

        float cityspinsize = w*6f;
        cityspin.setWidth(cityspinsize);
        cityspin.setHeight(cityspinsize);

        // turn axle cityspin around its center
        cityspin.setPosition(-cityspinsize/2f, -cityspinsize/2f);
        float x = w; // turn axle is on screen
        float y = 0;
        cityTurn.setLocalTranslation(x, y, 0);

        cityTurn.attachChild(cityspin);
        guiNode.attachChild(cityTurn);

        cityTurn.addControl(new AbstractControl() {
            @Override
            protected void controlUpdate(float tpf) {
                spatial.rotate(0, 0, tpf/4); 
            }

            @Override
            protected void controlRender(com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {}
        });

        /////////////////////////////////////////////////////////////

        
        ///////////////////////BUTTONS///////////////////////////////
        
        //create buttons as Pictures
        
        float buttonHeight = 50f;
        float buttonWidth = 200;
        playButton = new Picture("playBtn");
        playButton.setImage(app.getAssetManager(), "Interface/Buttons/start.png", true);
        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        playButton.setPosition(w/5f - 100, h*0.42f);
        guiNode.attachChild(playButton);

        settingsButton = new Picture("settingsBtn");
        settingsButton.setImage(app.getAssetManager(), "Interface/Buttons/settings.png", true);
        settingsButton.setWidth(buttonWidth);
        settingsButton.setHeight(buttonHeight);
        settingsButton.setPosition(w/5f - 100, h*0.3f);
        guiNode.attachChild(settingsButton);

        instructionsButton = new Picture("instructionsBtn");
        instructionsButton.setImage(app.getAssetManager(), "Interface/Buttons/instructions.png", true);
        instructionsButton.setWidth(buttonWidth);
        instructionsButton.setHeight(buttonHeight);
        instructionsButton.setPosition(w/5f - 100, h*0.18f);
        guiNode.attachChild(instructionsButton);

        
        
        Title = new Picture("Title");
        Title.setImage(app.getAssetManager(), "Interface/title.png", true);
        Title.setWidth(250);
        Title.setHeight(150);
        Title.setPosition(w/5f - 100, h*0.6f);
        guiNode.attachChild(Title);
        
        initKeys();

        ///////////////////////////////////////////////////////////////
        
    }

    /////////////////////////// BUTTONS ///////////////////////////////
    private void initKeys() {
        app.getInputManager().addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(actionListener, "Click");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Click") && !keyPressed) { // on release
                Vector2f mouse = app.getInputManager().getCursorPosition();

                //check each button
                if (isMouseOverButton(playButton, mouse)) {
                    clickStartGame();
                } else if (isMouseOverButton(settingsButton, mouse)) {
                    openSettings();
                } else if (isMouseOverButton(instructionsButton, mouse)) {
                    openInstructions();
                }
            }
        }
    };

    //
    private boolean isMouseOverButton(Picture button, Vector2f mouse) {
        float x = button.getLocalTranslation().x;
        float y = button.getLocalTranslation().y;
        float w = button.getWidth();
        float h = button.getHeight();

        return mouse.x >= x && mouse.x <= x + w &&
               mouse.y >= y && mouse.y <= y + h;
    }

    ////////////////////////////////////////////////////////////////////////

    private void createBackground(float w, float h) {
        background = new Geometry("MenuBG", new Quad(w, h));

        Material mat = new Material(app.getAssetManager(),"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(144f / 255f,184f / 255f,219f / 255f,1f)); //rgb(144 184 219)


        background.setMaterial(mat);
        guiNode.attachChild(background);
    }
   
    public void disappear() {
        background.addControl(fadeOut());
        playButton.addControl(fadeOut());
        settingsButton.addControl(fadeOut());
        instructionsButton.addControl(fadeOut());
        cityspin.addControl(fadeOut());
        Title.addControl(fadeOut());
    }

    private AbstractControl fadeOut() {
        return new AbstractControl() {

            float time = 0.5f;

            @Override
            protected void controlUpdate(float tpf) {
                time -= tpf;
                if (time <= 0f) {
                    spatial.removeFromParent();
                    spatial.removeControl(this);
                }
            }

            @Override
            protected void controlRender( com.jme3.renderer.RenderManager rm, com.jme3.renderer.ViewPort vp) {}
        };
    }

    // placeholder 
    private void clickStartGame() {
        app.startGame();
        app.getInputManager().removeListener(actionListener);
        disappear();

    }

    private void openSettings() {
        // 
    }

    private void openInstructions() {
        // 
    }
}
