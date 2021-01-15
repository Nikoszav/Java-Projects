package game;


import city.cs.engine.Walker;
import org.jbox2d.common.Vec2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Cannon_move extends KeyAdapter {
    private static final float Walking_speed=4;
    private Walker body;
    public Cannon_move(Walker body){
        this.body=body;
    }
    @Override
    public void keyPressed(KeyEvent e){
        int code =e.getKeyCode();

        if (code == KeyEvent.VK_LEFT) {
            body.startWalking(-Walking_speed);
        }else if (code==KeyEvent.VK_RIGHT){
            body.startWalking(Walking_speed);
        }
    }
    public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT){
            body.setLinearVelocity(new Vec2(0,0));
            body.stopWalking();
        }
        if (code == KeyEvent.VK_RIGHT){
            body.setLinearVelocity(new Vec2(0,0));
            body.stopWalking();
        }
    }
}
