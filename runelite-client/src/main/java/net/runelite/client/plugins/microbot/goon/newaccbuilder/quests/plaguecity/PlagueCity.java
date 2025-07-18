package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.plaguecity;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class PlagueCity {
    ArrayList<BankHandler.QuestItem> items = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("dwellberries", 1, false, false, false),
                    new BankHandler.QuestItem("spade", 1, false, false, false),
                    new BankHandler.QuestItem("rope", 1, false, false, false),
                    new BankHandler.QuestItem("bucket of milk", 1, false, false, false),
                    new BankHandler.QuestItem("chocolate dust", 1, false, false, false),
                    new BankHandler.QuestItem("snape grass", 1, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 1, false, false, false),
                    new BankHandler.QuestItem("bucket of water", 4, false, false, false)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "Yes.",
            "What's happened to her?",
            "Yes, I'll return it for you.",
            "I fear not a mere plague.",
            "I want to check anyway.",
            "I need permission to enter a plague house.",
            "This is urgent though! Someone's been kidnapped!",
            "This is really important though!",
            "Do you know what's in the cure?",
            "They won't listen to me!",
            "Okay, I'll look for it."
    ));
    private void prep() {
        Rs2Walker.walkTo(2653, 3284, 0, 3);
        BankHandler.withdrawQuestItems(items, true, true);
    }

    public void completeQuest() {
        //broke somewhere in here trying to walk through east ardy, gotta check what quest stop im on
        // couldnt figure out what broke. need to keep an eye on this one
        prep();
        Rs2Walker.walkTo(2568, 3333, 0, 3);
        DialogueHandler.talkToNPC("edmond", dialogue, 3);
        Rs2Walker.walkTo(2575,3334,0);
        DialogueHandler.talkToNPC("alrena", dialogue, 3);
        Rs2Walker.walkTo(2575,3334,0);
        Rs2GroundItem.take(1510);
        Rs2Inventory.waitForInventoryChanges(7000);
        Rs2Walker.walkTo(2567, 3333, 0, 3);
        DialogueHandler.talkToNPC("edmond", dialogue, 3);
        while (Rs2Inventory.hasItem("bucket of water")) {
            Rs2Inventory.use("bucket of water");
            Rs2GameObject.interact(2532, "use");
            sleep(1200);
        }
        Rs2Inventory.use("spade");
        Rs2GameObject.interact(2532, "use");
        sleep(5000);
        Rs2Walker.walkTo(2514, 9742, 0, 3);
        Rs2GameObject.interact(11422, "open");
        DialogueHandler.handleConversation(List.of(), 5);
        Rs2Inventory.use("rope");
        Rs2GameObject.interact(11422, "open");
        DialogueHandler.handleConversation(List.of(), 5);
        Rs2Walker.walkTo(2516, 9753, 0, 3);
        DialogueHandler.talkToNPC("edmond", dialogue, 30);
        Rs2Inventory.equip(1506);
        sleep(1200);
        Rs2GameObject.interact(2542);
        sleep(5500);
        DialogueHandler.talkToNPC("jethick", dialogue, 5);
        Rs2Walker.walkTo(2531, 3325, 0, 3);
        Rs2GameObject.interact(2537);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(List.of(), 5);
        DialogueHandler.talkToNPC("martha rehnison", dialogue, 5);
        Rs2GameObject.interact(2539);
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 1);
        DialogueHandler.talkToNPC("milli rehnison", dialogue, 5);
        Rs2Walker.walkTo(2540, 3275,0, 3);
        Rs2GameObject.interact(37321);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(dialogue, 5);
        Rs2Walker.walkTo(2525,3314,0);
        Rs2Npc.interact(4255, "talk-to");
        DialogueHandler.handleConversation(dialogue, 5);
        Rs2Walker.walkTo(2529, 3314, 0, 0);
        Rs2GameObject.interact(2528);
        sleep(3000);
        DialogueHandler.talkToNPC("bravek", dialogue, 5);

        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2Inventory.use("chocolate dust");
            Rs2Inventory.interact("bucket of milk", "use");
        }, "Chocolatey milk", 900, 9000);

        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2Inventory.use("snape grass");
            Rs2Inventory.interact("Chocolatey milk", "use");
        }, "hangover cure", 900, 9000);
        DialogueHandler.talkToNPC("bravek", dialogue, 25);
        Rs2Walker.walkTo(2540, 3275, 0);
        Rs2GameObject.interact(37321);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(dialogue, 5);
        Rs2GameObject.interact(2522);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() > 5500);
        sleep(1200);
        Rs2GameObject.interact(2526);
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(dialogue, 5);
        Rs2GameObject.interact(2523);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() < 5500);
        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2GameObject.interact(2530);
        }, "key", 600, 9000);
        Rs2GameObject.interact(2522);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() > 5500);
        sleep(1200);
        Rs2GameObject.interact(2526);
        sleepUntil(() -> Rs2Player.getWorldLocation().getX() >= 9672);
        sleep(1200);
        DialogueHandler.talkToNPC("elena", dialogue, 5);
        Rs2Walker.walkTo(2533, 3304,0);
        Rs2GameObject.interact(2543, "open");
        sleepUntil(() -> Rs2GameObject.exists(2544), 10000);
        Rs2GameObject.interact(2544, "climb-down");
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() > 5500);
        Rs2Walker.walkTo(2516, 9753,0);
        Rs2GameObject.interact(2533);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() < 5500);
        DialogueHandler.talkToNPC("edmond", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
        sleepUntil(() -> Rs2Inventory.hasItem(1505), 10000);
        Rs2Inventory.interact(1505, "read");
        DialogueHandler.handleConversation(List.of(), 5);
    }
}
