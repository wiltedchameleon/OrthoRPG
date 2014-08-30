package com.adavieslyons.util.map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * 
 * @author Ashley
 */
public class MapLayer {
	MapTileData tiles[][];
	Map map;
	
	public MapLayer(MapTileData tiles[][], Map map) {
		this.map = map;
		this.tiles = tiles;
	}
	
	public boolean getCollideable(int y, int x) {
		return MapTile.getTile(tiles[y][x].getId()).getCollision();
	}
	
	void render(GameContainer gc, Graphics graphics) {
		int y = 0;
		int x = 0;
		
		for (MapTileData[] tileRow : tiles) {
			x = 0;
			for (MapTileData tile : tileRow) {
				if (!map.isFogOfWar(x, y))
					MapTile.getTile(tile.getId()).render(gc, graphics, x * 32, y * 32);
				x++;
			}
			y++;
		}
	}
}
