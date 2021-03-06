package uk.daviesl.rpg.util.inventory;

import uk.daviesl.rpg.gamestate.states.GameState;
import uk.daviesl.rpg.util.FileLoader;
import uk.daviesl.rpg.util.SpriteSheet;
import org.newdawn.slick.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Item {
    private static ArrayList<Item> items;
    private final int id;
    private final String name;
    private final String description;
    private final ItemType type;
    private final Image image;
    private final Modifier[] modifiers;

    private Item(int id, String name, String description, ItemType type,
                 Image image, Modifier modifiers[]) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.image = image;
        this.modifiers = modifiers;
    }

    public static void LoadItems(GameState game) {
        // Load item data from the XML file
        items = new ArrayList<>();

        Document document = FileLoader.getXML("itemData");

        NodeList itemNodes = document.getElementsByTagName("item");

        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element i_itemNode = (Element) itemNodes.item(i);
            String name = i_itemNode.getAttribute("name");
            ItemType type = ItemType.valueOf(i_itemNode.getAttribute("type")
                    .toUpperCase());

            // Description
            String description = "";
            if (i_itemNode.getElementsByTagName("description").getLength() > 0)
                description = i_itemNode.getElementsByTagName("description")
                        .item(0).getTextContent();

            // Image
            Element textureElem = (Element) i_itemNode.getElementsByTagName(
                    "texture").item(0);
            int spriteSheet = Integer.parseInt(textureElem
                    .getAttribute("spritesheet"));
            int xPos = Integer.parseInt(textureElem.getAttribute("xPos"));
            int yPos = Integer.parseInt(textureElem.getAttribute("yPos"));
            Image image = SpriteSheet.getSpriteSheet(spriteSheet).getSubImage(
                    xPos, yPos, 32, 32);

            // Modifiers
            Modifier modifiers[] = null;
            if (i_itemNode.getElementsByTagName("modifiers").getLength() > 0) {
                NodeList modifierNodes = ((Element) i_itemNode
                        .getElementsByTagName("modifiers").item(0))
                        .getElementsByTagName("modifier");

                modifiers = new Modifier[modifierNodes.getLength()];

                for (int m = 0; m < modifierNodes.getLength(); m++) {
                    Element modifierNode = (Element) modifierNodes.item(m);
                    int index = Integer.parseInt(modifierNode
                            .getAttribute("index"));
                    int value = Integer.parseInt(modifierNode
                            .getAttribute("value"));

                    modifiers[m] = new Modifier(game, index, value);
                }
            }

            Item item = new Item(i, name, description, type, image, modifiers);
            items.add(item);
        }
    }

    public static Item getItem(int id) {
        return items.get(id);
    }

    public void enableModifiers() {
        for (Modifier modifier : modifiers)
            modifier.applyModifier();
    }

    public void disableModifiers() {
        for (Modifier modifier : modifiers)
            modifier.removeModifier();
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public ItemType getType() {
        return type;
    }
}
