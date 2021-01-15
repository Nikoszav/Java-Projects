package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

public class Cannon extends Walker {
    public Cannon(World w){
        super(w);
        Shape cannonShape = new PolygonShape(
                0.149f,0.975f, 0.775f,0.193f, 0.772f,-0.099f, 0.401f,-0.928f, -0.36f,-0.922f, -0.719f,-0.025f, -0.725f,0.163f, -0.14f,0.972f);
        SolidFixture fixture = new SolidFixture(this, cannonShape);
        this.addImage(new BodyImage("images/c.png", 4f));
        this.setPosition(new Vec2(8, -8));
    }
    public Body getBody(){
        return this;
    }
}
