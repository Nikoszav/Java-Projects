package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import java.util.Random;

public class Ball {
    private World world;
    Ball(World world){
        this.world=world;
        Shape shape = new CircleShape(1.25f);
        Body ball = new DynamicBody(world,shape);
        ball.addImage(new BodyImage("images/ball1.png",2));
        SolidFixture ballFixture = new SolidFixture(ball, shape);
        ballFixture.setRestitution(0.8f);
        ((DynamicBody) ball).setLinearVelocity(new Vec2((float)(Math.random()*3),0));
        Random no = new Random();
        int n = no.nextInt(15) -13;
        int y =no.nextInt(20) +15;
        ball.setPosition(new Vec2(n,y));
    }

}
