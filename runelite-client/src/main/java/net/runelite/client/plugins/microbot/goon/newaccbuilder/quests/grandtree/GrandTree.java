package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.grandtree;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import org.w3c.dom.ls.LSInput;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GrandTree {
    List<String> dialogue = new ArrayList<>(List.of(
            "You seem worried, what's up?", "I'd be happy to help!",
            "I'd be happy to help!", "Yes.",
            "I think so!",
            //this is selected twice
            "None of the above.",
            "None of the above.",
            "A man came to me with the King's seal.", "I gave the man Daconia rocks.",
            "And Daconia rocks will kill the tree!", "Climb Up.", "Climb Down.", "Take me to Karamja please!",
            "Glough sent me.", "Ka.", "Lu.", "Min.", "Sadly his wife is no longer with us!", "He loves worm holes.", "Anita.",
            "I suppose so."
    ));
    private void kingNarnode(int timeout) {
        Rs2Walker.walkTo(2465, 3494, 0, 3);
        DialogueHandler.talkToNPC("king narnode shareen", dialogue, timeout);
    }
    private void glough(int timeout) {
        Rs2Walker.walkTo(2478, 3464, 1, 2);
        DialogueHandler.talkToNPC("glough", dialogue, timeout);
    }

    private void killDemon() {
        long lastSeenDemon = System.currentTimeMillis();
        System.out.println("starting");
        while (System.currentTimeMillis() - lastSeenDemon < 10000) {
            System.out.println("currtime " + System.currentTimeMillis() + " last seen: " + lastSeenDemon);
            Rs2NpcModel blackDemon = Rs2Npc.getNpc("black demon");
            if (blackDemon != null) lastSeenDemon = System.currentTimeMillis();

            Rs2Player.drinkPrayerPotionAt(10);

            if (Rs2Player.getInteracting() != null) System.out.println("Currently fighting.");
            else if (blackDemon != null) {
                Rs2Npc.interact(blackDemon, "attack");
            }
        }
        System.out.println("done");
    }

    private void searchRocks() {
        for (TileEntry root : TileEntry.ROOT_LIST) {
            // some rocks arent in view
            if (root.getWalkToX() != null) {
                System.out.println("walking to intermediate root point");
                Rs2Walker.walkTo(root.getWalkToX(), root.getWalkToY(), root.getWalkToZ(), 2);
            }
            System.out.println("clicking root");
            Rs2GameObject.interact(new WorldPoint(
                    root.getTileX(),
                    root.getTileY(),
                    root.getTileZ()
            ), "search");
            System.out.println("waiting to start searching");
            sleepUntil(Rs2Player::isAnimating);
            System.out.println("searching root");
            sleepUntil(() -> !Rs2Player.isAnimating());
            sleep(600);
            System.out.println("checking if i got a rock");
            if (Rs2Dialogue.isInDialogue()) Rs2Dialogue.clickContinue();
            Rs2Inventory.waitForInventoryChanges(2000);
            if (Rs2Inventory.hasItem("daconia rock")) return;
        }
    }

    public void completeQuest() {
        kingNarnode(30);
        Rs2Walker.walkTo(2670,3111,0 ,2);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2675,3087,1,2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        DialogueHandler.talkToNPC("hazelmere", dialogue, 5);
        kingNarnode(5);
        glough(5);
        kingNarnode(5);
        Rs2Walker.walkTo(2465, 3496, 3, 2);
        DialogueHandler.talkToNPC("charlie", dialogue, 5);
        Rs2Walker.walkTo(2478, 3464, 1, 2);
        Rs2GameObject.interact("cupboard", "open");
        sleepUntil(() -> Rs2GameObject.hasAction(Rs2GameObject.convertToObjectComposition(2434), "search"), 10000);
        Rs2GameObject.interact("cupboard", "search");
        Rs2Inventory.waitForInventoryChanges(5000);
        glough(30);
        DialogueHandler.talkToNPC("charlie", dialogue, 5);
        DialogueHandler.talkToNPC("captain errdo", dialogue, 5);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2944, 3041, 0, 3);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        Rs2GameObject.interact(2439, "open");
        DialogueHandler.handleConversation(dialogue, 3);
        Rs2Walker.walkTo(3001, 3045, 0, 3);
        DialogueHandler.talkToNPC("foreman", dialogue, 15);
        Rs2Walker.walkTo(2460, 3381, 0, 3);
        DialogueHandler.talkToNPC("femi", dialogue, 5);
        Rs2Walker.walkTo(2465, 3496, 3, 2);
        DialogueHandler.talkToNPC("charlie", dialogue, 5);
        Rs2Walker.walkTo(2389, 3515, 1, 2);
        DialogueHandler.talkToNPC("anita", dialogue, 5);
        Rs2Walker.walkTo(2478, 3464, 1, 2);
        Rs2Inventory.use("key");
        Rs2GameObject.interact(2436, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        kingNarnode(5);
        Rs2Walker.walkTo(2478, 3464, 1, 2);
        Rs2GameObject.interact(2447, "climb-up");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 2, 10000);
        // t
        Rs2Inventory.use(789);
        Rs2GameObject.interact(2440, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleep(600);
        // u
        Rs2Inventory.use(790);
        Rs2GameObject.interact(2441, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleep(600);
        // z
        Rs2Inventory.use(791);
        Rs2GameObject.interact(2442, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleep(600);
        // o
        Rs2Inventory.use(792);
        Rs2GameObject.interact(2443, "use");
        Rs2Inventory.waitForInventoryChanges(5000);
        sleep(600);
        Rs2Player.drinkPrayerPotionAt(10);
        System.out.println("climbing into demon fight");
        Rs2GameObject.interact(2444, "climb-down");
        sleepUntil(Rs2Dialogue::isInDialogue, 30000);
        System.out.println("talking to glough");
        DialogueHandler.handleConversation(dialogue, 2);
        System.out.println("done talking to glough");
        sleepUntil(() -> Rs2Npc.getNpc("black demon") != null);
        System.out.println("black demon is present");
        //2492,9865 safe spot maybe?
        Rs2Walker.walkTo(2492,9865,0,0);
        killDemon();
        Rs2Walker.walkTo(2467,9895,0,3);
        DialogueHandler.talkToNPC("king narnode shareen", dialogue, 25);
        searchRocks();
        Rs2Walker.walkTo(2467,9895,0,3);
        DialogueHandler.talkToNPC("king narnode shareen", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish("Grand Tree");
    }
}
