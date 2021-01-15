package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.*;

public class Ground extends StaticBody {



    public Ground(World world, Cannon cannon){
        super(world);


        /*
        float[] boxArray = {-2.855f, -0.459f, -2.844f, 0.489f, 2.821f, 0.5f, 2.821f, -0.459f};
        for (int i = 0; i < boxArray.length; i++) {
            boxArray[i] = boxArray[i] * 4;
        }
        */
        Shape shape = new BoxShape(30,0.5f);
        Body ground = new StaticBody(world, shape);
       // ground.addImage(new BodyImage("images/ground_image.png", size));
        ground.setPosition(new Vec2(-1 ,-14.13f));
        ground.setFillColor(new Color(0x591D03));
    }

}
