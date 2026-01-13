package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;

/*
  SimMAIN MNU WHICH LATER WILL HAVE SETTING SHOPEFULLY
*/

public class MainMenu {

    private final SimpleApplication app;
    private final Node guiNode;

    private Geometry background;
    private BitmapText title;
    private BitmapText start;
    private BitmapText quit;

    private ColorRGBA bgColor = ColorRGBA.Black; // init bg color
    private boolean colorChanged = false;

    public MainMenu(SimpleApplication app) {
        this.app = app;
        this.guiNode = app.getGuiNode();
    }
    
    //ON OFF MAKE LATER CAUSE THATS REALLY LONG TO USE

//    background.addControl(new AbstractControl() {
//
//            float timeLeft = 2f; // seconds
//
//            @Override
//            protected void controlUpdate(float tpf) {
//                timeLeft -= tpf;
//
//                if (timeLeft <= 0f) {
//                    spatial.removeFromParent();
//                }
//            }
//
//            @Override
//            protected void controlRender(
//                    com.jme3.renderer.RenderManager rm,
//                    com.jme3.renderer.ViewPort vp) {
//                // Not used
//            }
//        });

    
    //  INIT MENU 
    public void init() {
        float width = app.getCamera().getWidth();
        float height = app.getCamera().getHeight();

        // BG
        Quad quad = new Quad(width, height);
        background = new Geometry("MenuBackground", quad);

        Material mat = new Material(app.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", bgColor);
        background.setMaterial(mat);

        
        //SCREEN DISSAPHEARS
        background.addControl(new AbstractControl() {

            float timeLeft = 2f; // seconds

            @Override
            protected void controlUpdate(float tpf) {
                timeLeft -= tpf;

                if (timeLeft <= 0f) {
                    spatial.removeFromParent();
                }
            }

            @Override
            protected void controlRender(
                    com.jme3.renderer.RenderManager rm,
        });

        
        guiNode.attachChild(background);

        // JUST A  AFONT VAR
        BitmapFont font = app.getAssetManager()
                .loadFont("Interface/Fonts/Default.fnt"); //either do a png or choose a nicer font later on

        // THE TITLE
        title = new BitmapText(font); //in jmonkey tut
        title.setText("DELIVERY GAME");
        title.setSize(font.getCharSet().getRenderedSize() * 4f); //size, idk how to upscale tho
        title.setColor(ColorRGBA.White);

        centerText(title, width, height * 0.65f); //so its a bit lower
        guiNode.attachChild(title);

        // START
        start = new BitmapText(font);
        start.setText("Press ENTER to Start Game");
        start.setSize(font.getCharSet().getRenderedSize() * 1.5f);
        start.setColor(ColorRGBA.White);
        
        centerText(start, width, height * 0.45f); //weird way to control it but it works
        guiNode.attachChild(start);

        // QUIT
        quit = new BitmapText(font);
        quit.setText("PRESS ESC TO QUIT"); //do these buttons do anything... no, buuut, they will :D
        quit.setSize(font.getCharSet().getRenderedSize() * 1.2f);
        quit.setColor(ColorRGBA.Gray);

        centerText(quit, width, height * 0.35f);
        guiNode.attachChild(quit);
    }

    // CHANGE BG COLOR 
    public void changeColor(ColorRGBA newColor) {
        bgColor = newColor;
        background.getMaterial().setColor("Color", bgColor);
        colorChanged = true;
    }

    public boolean isColorChanged() {
        return colorChanged;
    }
    private void centerText(BitmapText text, float screenWidth, float y) { //lowkey stolen from tutorials
        float textWidth = text.getLineWidth();
        text.setLocalTranslation(
                (screenWidth - textWidth) / 2f,
                y,
                0
        );
    }

}
