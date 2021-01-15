package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler extends MouseAdapter {

    private WorldView view;
    private Cannon cannon;
    public MouseHandler(WorldView view, Cannon cannon) {
        this.cannon=cannon;
        this.view=view;
    }
    public void mousePressed(MouseEvent e) {
        float[] boxArray = {-0.31f,-0.452f, -0.29f,0.212f, -0.184f,0.414f, -0.038f,0.496f, 0.134f,0.454f, 0.284f,0.226f, 0.314f,-0.45f};
        for (int i = 0; i < boxArray.length; i++) {
            boxArray[i] = boxArray[i] * 3;
        }
        final Shape ballShape = new PolygonShape(boxArray);
        Walker bullets= new Walker(view.getWorld(), ballShape);
        bullets.setPosition(new Vec2(cannon.getPosition().x,cannon.getPosition().y+1));
        bullets.setLinearVelocity(new Vec2(0,30));
        bullets.addImage(new BodyImage("images/bullet.png",3));
    }
}
