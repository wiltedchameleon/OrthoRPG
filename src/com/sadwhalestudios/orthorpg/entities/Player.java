package com.sadwhalestudios.orthorpg.entities;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Point;

/**
 *
 * @author Ashley
 */
public class Player extends Entity {
    Point position;
    Image image;
    
    public Player() throws SlickException
    {
        position = new Point(0, 0);
        image = new Image("/resources/img/player.png");
    }
    
    @Override
    public void update(GameContainer gc, int delta)
    {
        if (Keyboard.isKeyDown(Input.KEY_UP))
        {
            position.setY(position.getY() - 0.1f * delta);
        }
        else if (Keyboard.isKeyDown(Input.KEY_DOWN))
        {
            position.setY(position.getY() + 0.1f * delta);
        }
        
        if (Keyboard.isKeyDown(Input.KEY_LEFT))
        {
            position.setX(position.getX() - 0.1f * delta);
        }
        else if (Keyboard.isKeyDown(Input.KEY_RIGHT))
        {
            position.setX(position.getX() + 0.1f * delta);
        }
    }
    
    @Override
    public void render(GameContainer gc, Graphics graphics)
    {
        image.draw(position.getX(), position.getY());
    }
}
