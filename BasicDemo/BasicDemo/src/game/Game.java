package game;

import city.cs.engine.*;

import org.jbox2d.common.Vec2;

import javax.swing.JFrame;

public class Game {

    private World world;

    private MyView view;

    public Game() {

        // display the view in a frame
        final JFrame frame = new JFrame("Basic world");

        // make the world
        world = new World(60);

        // make cannon
        Cannon cannon = new Cannon(world);
        // make the ground
        Ground ground = new Ground(world, cannon);

        //make ball
        int n = 5;
        for (int i = 0; i < n; i = i + 1) {
            Ball ball = new Ball(world);
        }


        //cannon_movement
        frame.addKeyListener(new Cannon_move(cannon));

        // make a view
        view = new MyView(world, 700, 550);

        // mouse
        view.addMouseListener(new MouseHandler(view, cannon));

        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);

        // display the world in the window
        frame.add(view);

        // don't let the game window be resized
        //frame.setResizable(false);

        // size the game window to fit the world view
        frame.pack();

        // make the window visible
        frame.setVisible(true);

        // debugging view
        //JFrame debugView = new DebugViewer(world, 500, 500);

        world.start();
    }

}
