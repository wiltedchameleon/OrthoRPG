package com.adavieslyons.orthorpg.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.adavieslyons.orthorpg.gamestate.states.GameState;
import com.adavieslyons.util.Vector2i;
import com.adavieslyons.util.map.MapTile;

public class TileSelectorGUI extends GUIWindow {
	private int selectedTile;
	private boolean tileSelected;
	
	public void reset() {
		selectedTile = -1;
		tileSelected = false;
	}
	
	public TileSelectorGUI(GameContainer gc, GameState game) throws SlickException {
		super(gc, game, 504, 624);
		renderPrimaryContent(gc);
	}
	
	public void renderPrimaryContent(GameContainer gc) throws SlickException {
		Graphics graphics = gc.getGraphics();
		graphics.clear();

		int x = 0;
		for (MapTile tile : MapTile.getTiles())
		{
			graphics.drawImage(tile.getBasicTexture(), BW + 8 + x, BW + 8);
			x += 32;
		}

		graphics.copyArea(windowDynamicContent, 0, 0);
		graphics.clear();
	}

	@Override
	public void render(GameContainer gc, Graphics graphics)
			throws SlickException {
		graphics.drawImage(windowBg, windowRect.getX(), windowRect.getY());
		graphics.drawImage(windowDynamicContent, windowRect.getX(),
				windowRect.getY());
	}

	@Override
	public void update(GameContainer gc, GameState game, int delta)
			throws SlickException {
		if (game.getInput().isMouseButtonDown(0))
		{
			Vector2i mouseCoords = new Vector2i(game.getInput().getMouseX(), game.getInput().getMouseY());
			
			if (mouseCoords.getY() > windowRect.getY() + BW + 8 && mouseCoords.getY() < windowRect.getY() + BW + 8 + 32)
			{
				if (mouseCoords.getX() > windowRect.getX() + BW + 8 && mouseCoords.getX() < windowRect.getX() + BW + 8 + 32 * MapTile.getTiles().length)
				{
					// Inside a tile
					int tile = (int) Math.floor(mouseCoords.getX() - windowRect.getX() - BW - 8) / 32;
					selectedTile = tile;
					tileSelected = true;
				}
			}
		}
	}
	
	public boolean getTileSelected() {
		return tileSelected;
	}
	
	public int getSelectedTile() {
		return selectedTile;
	}
}