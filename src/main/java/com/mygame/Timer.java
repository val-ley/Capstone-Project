package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;

public class Timer {

    private float time = 180f;
    private final BitmapText timerText;

    public Timer(SimpleApplication app, Node guiNode) {
        BitmapFont font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        timerText = new BitmapText(font);
        timerText.setText(formatTime(time));
        timerText.setLocalTranslation(10, app.getCamera().getHeight() - 10, 0);
        guiNode.attachChild(timerText);
    }

    public void update(float tpf) {
        if (time > 0) {
            time -= tpf; // count down
            if (time < 0) time = 0; //stop if under 0
            timerText.setText(formatTime(time));
        }
    }

    // formatting
    private String formatTime(float seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("Time: %02d:%02d", mins, secs);
    }
}
