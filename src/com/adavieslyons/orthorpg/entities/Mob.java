package com.adavieslyons.orthorpg.entities;

import com.adavieslyons.orthorpg.Game;
import com.adavieslyons.orthorpg.gamestate.states.GameState;
import com.adavieslyons.util.FileLoader;
import com.adavieslyons.util.Vector2i;
import com.adavieslyons.util.XMLParser;
import com.adavieslyons.util.dialog.DialogNode;
import com.adavieslyons.util.dialog.IDialogable;
import com.adavieslyons.util.map.Map;
import com.adavieslyons.util.map.MapTileData;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Ashley
 */
public class Mob extends MovingPathEntity implements IDialogable, ICombat, ITakeTurns {
    private int HP;
    private int mana;
    private int fortitude;
    private int strength;
    private int intelligence;
    private int swordsmanship;
    private int archery;
    private int speed;
    private int combatMagic;
    private int darkMagic;
    private int utilityMagic;
    private int healingMagic;
    private int protectiveMagic;
    private String name;
    private DialogNode[] dialog;
    private MapTileData tileOccupied;
    private Image dialogImage;
    private final GameState game;
    private boolean myTurn;

    public Mob(GameContainer gc, GameState game, int mobID, Map map,
               Vector2i path[]) {
        super(map);
        this.game = game;
        this.HP = getFortitude() * 10 + 10;
        this.mana = 100;
        setPath(path);

        loadDataFromXML(gc, mobID, game);
    }

    private void loadDataFromXML(GameContainer gc, int mobID, GameState game) {
        Document mobDoc = FileLoader.getXML("mob/" + mobID);

        Element mobInfo = (Element) mobDoc.getElementsByTagName("info").item(0);

        String imageName = mobInfo.getAttribute("avatar");
        Element textureElement = (Element) mobDoc.getElementsByTagName("texture").item(0);

        name = mobInfo.getAttribute("name");
        dialogImage = FileLoader.getImage("ui/avatar/mob/" + imageName);
        setImage(XMLParser.loadTexture(textureElement));

        // Parse dialog if they have any
        NodeList dialogList = mobDoc.getElementsByTagName("dialog");

        if (dialogList.getLength() > 0) {
            dialog = XMLParser.loadDialog((Element) dialogList.item(0));
        }


    }

    @Override
    public void update(GameContainer gc, GameState game, int delta) {
        updateMove(delta);
        updatePath();
    }

    @Override
    public void attack(ICombat enemy) {
        //TODO: implement
    }

    @Override
    public void render(GameContainer gc, Graphics graphics)
            throws SlickException {
        super.render(gc, graphics);

        if (game.isBattle()) {
            Vector2i positionCoordinates = map.tileCoordinatesToGameCoordinates(position);
            Vector2i lerpFromCoordinates = map.tileCoordinatesToGameCoordinates(positionLerpFrom);
            Vector2f drawCoordinates = lerpFromCoordinates.lerpTo(positionCoordinates, positionLerpFraction).add(new Vector2f(map.getOffset().getX(), map.getOffset().getY()));

            int drawX = (int) (drawCoordinates.getX() + Game.TILE_SIZE_X / 2 - 16);
            int drawY = (int) (drawCoordinates.getY() - image.getHeight() / 2 - 5 - Game.TILE_SIZE_Y / 2);

            graphics.setColor(Color.black);
            graphics.drawRect(drawX, drawY, 32, 5);

            int hpFraction = getHP() / getMaxHP();

            graphics.setColor(Color.red);
            graphics.fillRect(drawX + 1, drawY + 1, 31, 4);
            graphics.setColor(Color.yellow);
            graphics.fillRect(drawX + 1, drawY + 1, 31 * hpFraction, 4);
        }
    }

    @Override
    public void onClick(GameState game) {
        if (game.isBattle()) {
            game.getPlayer().attack(this);
        } else {
            game.showDialog(dialog, this);
        }
    }


    @Override
    public void dialogCloseRequested() {
        // TODO Auto-generated method stub
        game.getGameStateManager().popState();
    }

    @Override
    protected void occupiedTileStartChange(Vector2i newTile) {
        tileOccupied = map.setOccupied(newTile.getX(), newTile.getY(), this);
        if (map.getGame().isBattle())
            AP--;
    }

    @Override
    protected void occupiedTileEndChange(Vector2i oldTile) {
        map.setOccupied(oldTile.getX(), oldTile.getY(), null);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDialogTitle() {
        return name;
    }

    @Override
    public Image getDialogImage() {
        return dialogImage;
    }

    @Override
    public int getFortitude() {
        return fortitude;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getIntelligence() {
        return intelligence;
    }

    @Override
    public int getSwordsmanship() {
        return swordsmanship;
    }

    @Override
    public int getArchery() {
        return archery;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public int getCombatMagic() {
        return combatMagic;
    }

    @Override
    public int getDarkMagic() {
        return darkMagic;
    }

    @Override
    public int getUtilityMagic() {
        return utilityMagic;
    }

    @Override
    public int getHealingMagic() {
        return healingMagic;
    }

    @Override
    public int getProtectiveMagic() {
        return protectiveMagic;
    }

    @Override
    public int getAP() {
        return AP;
    }

    @Override
    public void setAP(int AP) {
        this.AP = AP;
    }

    @Override
    public int getMaxAP() {
        return 7;
    }

    @Override
    public int getHP() {
        return HP;
    }

    @Override
    public void setHP(int HP) {
        this.HP = HP;
    }

    @Override
    public int getMaxHP() {
        return getFortitude() * 10 + 10;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int Mana) {
        this.mana = Mana;
    }

    @Override
    public int getMaxMana() {
        return 100;
    }

    @Override
    public void starTurn() {
        myTurn = true;
    }

    @Override
    public void endTurn() {
        myTurn = false;
    }

    @Override
    public boolean isMyTurn() {
        return myTurn;
    }
}
