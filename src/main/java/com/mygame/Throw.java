/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author valley
 */
public class Throw {

    private Geometry deliveryBox;
    
    public void disappear() {

    // background fade/remove
    background.addControl(new AbstractControl() {

        float timeLeft = 0.5f;

        @Override
        protected void controlUpdate(float tpf) {
            timeLeft -= tpf;

            if (timeLeft <= 0f) {
                spatial.removeFromParent();
                spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(
                com.jme3.renderer.RenderManager rm,
                com.jme3.renderer.ViewPort vp) { }
    });

}
