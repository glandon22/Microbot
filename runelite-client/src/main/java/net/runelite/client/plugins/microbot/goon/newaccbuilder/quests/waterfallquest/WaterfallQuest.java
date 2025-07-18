package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.waterfallquest;

import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.tile.Rs2Tile;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import net.runelite.client.plugins.pestcontrol.Game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class WaterfallQuest {
    List<String> dialogue = new ArrayList<>(List.of(
            "How can I help?",
            "Yes."
    ));
    private void almera() {
        Rs2Walker.walkTo(2520, 3496, 0, 2);
        DialogueHandler.talkToNPC("almera", dialogue, 5);
    }

    private void hadley() {
        Rs2Walker.walkTo(2514, 3429, 0, 3);
        DialogueHandler.selectAllDialogueOptions("hadley", new ArrayList<>(List.of("goodbye")), 5);
    }

    private void dungeonLedge(boolean hudon) {
        Rs2Walker.walkTo(2511,3494, 0,1);
        Rs2GameObject.interact("log raft", "board");
        sleep(6000);
        if (hudon) {
            DialogueHandler.talkToNPC("hudon", dialogue, 5);
        }
        Rs2Inventory.use("rope");
        Rs2GameObject.interact(1996, "use");
        sleep(11000);
        Rs2Inventory.use("rope");
        Rs2GameObject.interact(2020, "use");
        sleep(5000);
    }

    private void barrellOff() {
        Rs2GameObject.interact("barrel", "get in");
        sleep(7000);
    }

    private void getBook() {
        Rs2Walker.walkTo(2519, 3426, 1, 2);
        System.out.println("searching book case for baxtorian book");
        Rs2GameObject.interact(1989, "search");
        sleepUntil(() -> Rs2Inventory.hasItem("book on baxtorian"), 5000);
        sleep(800);
        System.out.println("opening book");
        Rs2Inventory.interact("book on baxtorian", "read");
        sleepUntil(() -> Rs2Widget.getWidget(392, 77) != null);
        System.out.println("clicking through pages");
        sleepUntil(
                () -> Rs2Widget.getWidget(392, 77) == null,
                () -> Rs2Widget.clickWidget(392, 77),
                5000, 100
        );
        System.out.println("closing book");
        Rs2Keyboard.keyPress(KeyEvent.VK_ESCAPE);
    }

    private void pebble() {
        Rs2Walker.walkTo(2535, 3155, 0, 2);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2533, 9557, 0, 2);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Walker.walkTo(2546, 9564, 0, 2);
        Rs2GameObject.interact(1990, "search");
        sleepUntil(() -> Rs2Inventory.hasItem("key"));
        Rs2Walker.walkTo(2515, 9580, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        DialogueHandler.talkToNPC("golrie", dialogue, 5);
        Rs2Player.drinkPrayerPotionAt(15);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2533, 9557, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        Rs2Walker.walkTo(2535, 3155, 0, 2);
    }

    private void amulet() {
        Rs2Walker.walkTo(2554, 3444, 0, 3);
        Rs2Inventory.use("glarial's pebble");
        Rs2GameObject.interact(1992, "use");
        sleep(6500);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2541, 9813, 0, 3);
        Rs2GameObject.interact(1993, "search");
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Walker.walkTo(2531, 9844, 0, 3);
        Rs2GameObject.interact(1994, "open");
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Walker.walkTo(2550, 3444, 0, 3);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
    }

    private void enterChamber() {
        Rs2Walker.walkTo(2534, 3573, 0, 3);
        BankHandler.withdrawQuestItems(List.of(
            new BankHandler.QuestItem("rope", 1, false, false, false),
            new BankHandler.QuestItem("games necklace", 1, false, false, false),
            new BankHandler.QuestItem("prayer potion(4)", 2, false, false, false),
            new BankHandler.QuestItem("glarial's urn", 1, false, false, false),
            new BankHandler.QuestItem("glarial's amulet", 1, false, false, false),
            new BankHandler.QuestItem("air rune", 6, false, false, false),
            new BankHandler.QuestItem("water rune", 6, false, false, false),
            new BankHandler.QuestItem("earth rune", 6, false, false, false),
            new BankHandler.QuestItem("trout", 6, false, false, false)
        ), true, false);
        dungeonLedge(false);
        Rs2GameObject.interact(2010, "open");
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() > 9000, 7500);
        Rs2Player.drinkPrayerPotionAt(10);
        sleepUntil(Rs2Player::hasPrayerPoints);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2590,9883,0,3);
        Rs2Player.drinkPrayerPotionAt(10);
        sleepUntil(Rs2Player::hasPrayerPoints);
        Rs2GameObject.interact(1999, "search");
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Player.drinkPrayerPotionAt(10);
        sleepUntil(Rs2Player::hasPrayerPoints);
        Rs2Walker.walkTo(2566,9900,0, 3);
        Rs2GameObject.interact(2002, "open");
        sleepUntil(() -> Microbot.getClient().getTopLevelWorldView().getScene().isInstance());
        System.out.println("waiting for chamber instance to load");
        sleep(5000);
        //Rs2Walker.walkTo(2604,9909,0, 3);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
    }

    private void placeRunes() {
        WorldPoint[] points = {
                new WorldPoint(2562, 9910, 0),
                new WorldPoint(2562, 9912, 0),
                new WorldPoint(2562, 9914, 0),
                new WorldPoint(2569, 9914, 0),
                new WorldPoint(2569, 9912, 0),
                new WorldPoint(2569, 9910, 0)
        };

        String[] runes = {"air rune", "earth rune", "water rune"};

        for (WorldPoint point : points) {
            for (String rune : runes) {
                Rs2Inventory.use(rune);
                Rs2GameObject.interact(point, "use");
                Rs2Inventory.waitForInventoryChanges(5000);
            }
        }
    }

    private void placeRunesV2() {
        List<GameObject> allObjects = Rs2GameObject.getGameObjects();
        List<GameObject> filtered = new ArrayList<>();
        for (GameObject object : allObjects) {
            if (object == null) continue;
            if (object.getId() == 2005) {
                if (object.getWorldLocation().distanceTo2D(Microbot.getClient().getLocalPlayer().getWorldLocation()) < 15) {
                    filtered.add(object);
                }
            }
        }

        String[] runes = {"air rune", "earth rune", "water rune"};

        for (GameObject object : filtered) {
            for (String rune : runes) {
                Rs2Inventory.use(rune);
                Rs2GameObject.interact(object, "use");
                Rs2Inventory.waitForInventoryChanges(5000);
            }
        }
    }

    private void placeItems() {
        Rs2Inventory.use("glarial's amulet");
        Rs2GameObject.interact(2006, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(List.of(), 5);
        Rs2Inventory.use("glarial's urn");
        Rs2GameObject.interact(2014, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(List.of(), 5);
    }

    public void completeQuest() {
        almera();
        dungeonLedge(true);
        barrellOff();
        hadley();
        getBook();
        pebble();
        amulet();
        enterChamber();
        placeRunesV2();
        placeItems();
        DialogueHandler.handleConversation(List.of(), 5);
        MiscellaneousUtilities.waitForQuestFinish("Waterfall Quest");
    }
}
