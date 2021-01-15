package game;

import city.cs.engine.UserView;
import city.cs.engine.World;

import java.awt.*;

public class MyView extends UserView {
    Image image;
   public MyView(World world, int width, int height) {
       super(world, width, height);
       image=Toolkit.getDefaultToolkit().createImage("images/background.6jpg.jpg");
   }
   @Override
    protected void paintBackground(Graphics2D g){
      g.drawImage(image,0,0,null);
   }
}
