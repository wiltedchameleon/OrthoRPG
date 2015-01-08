package com.adavieslyons.orthorpg.entities;

import com.adavieslyons.orthorpg.Game;
import com.adavieslyons.orthorpg.gamestate.states.GameState;
import com.adavieslyons.util.Vector2i;
import com.adavieslyons.util.map.Map;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * @author Ashley
 */
public abstract class Entity {
    protected Map map;
    protected Image image;
    protected Image hoverImage;
    // In terms of tiles
    protected Vector2i position;
    protected Vector2i positionLerpFrom;
    protected Vector2i lastPosition;
    private float positionLerpFraction;

    public Entity(Map map) {
        this.map = map;

        position = new Vector2i(0, 0);
        lastPosition = position;
        positionLerpFrom = new Vector2i(0, 0);
    }

    public abstract void update(GameContainer gc, GameState game, int delta)
            throws SlickException;

    public void render(GameContainer gc, Graphics graphics)
            throws SlickException {
        Vector2i positionCoordinates = map.tileCoordinatesToGameCoordinates(position);
        Vector2i lerpFromCoordinates = map.tileCoordinatesToGameCoordinates(positionLerpFrom);
        Vector2f drawCoordinates = lerpFromCoordinates.lerpTo(positionCoordinates, positionLerpFraction).add(new Vector2f(map.getOffset().getX(), map.getOffset().getY()));

        int drawX = (int) (drawCoordinates.getX() + Game.TILE_SIZE_X / 2 - image.getWidth() / 2);
        int drawY = (int) (drawCoordinates.getY() - image.getHeight() / 2 - Game.TILE_SIZE_Y / 2);

        if (gc.getInput().getMouseX() > drawX && gc.getInput().getMouseX() < drawX + image.getWidth()
                && gc.getInput().getMouseY() > drawY && gc.getInput().getMouseY() < drawY + image.getHeight()) {
            Color color = image.getColor(gc.getInput().getMouseX() - drawX, gc.getInput().getMouseY() - drawY);
            if (color.getAlpha() != 0)
                graphics.drawImage(hoverImage, drawX, drawY);
        }
        graphics.drawImage(image, drawX, drawY);
    }

    public void generateHoverImage() throws SlickException {
        hoverImage = new Image(image.getWidth(), image.getHeight());
        Graphics hoverImageGraphics = hoverImage.getGraphics();
        hoverImageGraphics.setColor(new Color(256, 256, 256, 64));

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getColor(x, y).getAlpha() == 0) {
                    if ((x != 0 && image.getColor(x - 1, y).getAlpha() != 0)
                            || (y != 0 && image.getColor(x, y - 1).getAlpha() != 0)
                            || (x != image.getWidth() - 1 && image.getColor(x + 1, y).getAlpha() != 0)
                            || (y != image.getHeight() - 1 && image.getColor(x, y + 1).getAlpha() != 0))
                        hoverImageGraphics.drawRect(x, y, 0, 0);
                }
            }
        }
        hoverImageGraphics.flush();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;

        try {
            generateHoverImage();
            System.out.println("Generated hover image for entity");
        } catch (SlickException e) {
            System.out.println("[ERROR] Cannot generate hover image for entity");
        }
    }

    public Vector2i getPositionLerpFrom() {
        return positionLerpFrom;
    }

    public void setPositionLerpFrom(Vector2i positionLerpFrom) {
        this.positionLerpFrom = positionLerpFrom;
    }

    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(Vector2i position) {
        this.position = position;
    }

    public Vector2f getRenderPosition() {
        Vector2i positionCoordinates = map.tileCoordinatesToGameCoordinates(position);
        Vector2i lerpFromCoordinates = map.tileCoordinatesToGameCoordinates(positionLerpFrom);
        Vector2f drawCoordinates = lerpFromCoordinates.lerpTo(positionCoordinates, positionLerpFraction);
        return drawCoordinates;
    }

    public void setPositionLerpFraction(float positionLerpFraction) {
        this.positionLerpFraction = positionLerpFraction;
    }

    public Rectangle getRenderBounds() {
        return new Rectangle(getRenderPosition().getX(),
                getRenderPosition().getY(), 32, 64);
    }

    public boolean mouseOverThis(GameState game) {
        Rectangle rBounds = getRenderBounds();
        return rBounds.contains(game.getInput().getMouseX(), game.getInput().getMouseY());
    }
}
