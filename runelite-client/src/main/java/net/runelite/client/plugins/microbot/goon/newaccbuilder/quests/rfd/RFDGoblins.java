package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.rfd;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.MicrobotOverlay;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.Arrays;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.*;

public class RFDGoblins {
    public static final List<String> dialogue = Arrays.asList(
            "I need your help...",
            "What do you need? Maybe I can get it for you.",
            "I've got the charcoal you were after.",
            "I've got the ingredients we need..."
    );
    private static final List<ItemBuyer.ItemToBuy> items = List.of(
            new ItemBuyer.ItemToBuy("bread", 1, 1000, true),
            new ItemBuyer.ItemToBuy("orange", 1, 1000, true),
            new ItemBuyer.ItemToBuy("blue dye", 1, 5000, true),
            new ItemBuyer.ItemToBuy("gnome spice", 1, 10000, true),
            new ItemBuyer.ItemToBuy("bucket of water", 1, 10000, true),
            new ItemBuyer.ItemToBuy("charcoal", 1, 10000, true),
            new ItemBuyer.ItemToBuy("fishing bait", 1, 500, true)
    );

    private static void prep() {
        Rs2Inventory.interact("varrock teleport", "break");
        sleep(5000);
        MiscellaneousUtilities.walkToGE();
        ItemBuyer.buyItems(items);
        ItemBuyer.ensureAllOffersCollected(true);
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("lumbridge teleport", 5, false, false, false),
                new BankHandler.QuestItem("varrock teleport", 5, false, false, false),
                new BankHandler.QuestItem("falador teleport", 5, false, false, false),
                new BankHandler.QuestItem("bread", 1, false, false, false),
                new BankHandler.QuestItem("orange", 1, false, false, false),
                new BankHandler.QuestItem("knife", 1, false, false, false),
                new BankHandler.QuestItem("blue dye", 1, false, false, false),
                new BankHandler.QuestItem("gnome spice", 1, false, false, false),
                new BankHandler.QuestItem("bucket of water", 1, false, false, false),
                new BankHandler.QuestItem("charcoal", 1, false, false, false),
                new BankHandler.QuestItem("fishing bait", 1, false, false, false)
        ), false, false);
    }

    public static void completeQuest() {
        prep();
        Rs2Walker.walkTo(3208, 3212, 0);
        doUntil(
                () -> Rs2Player.getWorldLocation().getY() > 5000,
                () -> Rs2GameObject.interact(12348, "open"),
                3000,
                100000
        );
        doUntil(
                Rs2Dialogue::isInDialogue,
                () -> Rs2GameObject.interact(12334, "inspect"),
                3000,
                100000
        );
        DialogueHandler.handleConversation(dialogue, 5);
        Rs2Inventory.interact("falador teleport", "break");
        sleepUntil(() -> Rs2Player.getWorldLocation().getX() < 3000, 10000);
        sleep(3000);
        Microbot.log("Walking towards goblin cook.");
        Rs2Walker.walkTo(2957, 3505, 0, 2);
        doUntil(
                () -> !Rs2GameObject.exists(12444),
                () -> Rs2GameObject.interact(12444),
                5000,
                10000
        );
        Rs2Walker.walkTo(2960, 3506, 0, 0);
        doUntil(
                () -> !Rs2GameObject.exists(12389),
                () -> Rs2GameObject.interact(12389),
                5000,
                10000
        );
        Microbot.log("Talking to goblin cook.");
        DialogueHandler.talkToNPC("goblin cook", dialogue, 5);
        Microbot.log("Using charcoal on cook to start cutscene.");
        doUntil(
                () -> Rs2Dialogue.isInCutScene() || Rs2Dialogue.isInDialogue(),
                () -> {
                    Rs2Inventory.use("charcoal");
                    Rs2Npc.interact(4851);
                },
                5000,
                100000
        );
        Microbot.log("Handling ketlle explosion cutscene.");
        DialogueHandler.handleConversationWithCutscene(dialogue, 15);
        DialogueHandler.talkToNPC("goblin cook", dialogue, 5);
        Microbot.log("Making soggy bread.");
        doUntil(
                () -> Rs2Inventory.hasItem("soggy bread"),
                () -> {
                    Rs2Inventory.use("bucket of water");
                    Rs2Inventory.interact("bread", "use");
                },
                5000,
                100000
        );
        Microbot.log("Making dyed orange.");
        doUntil(
                () -> Rs2Inventory.hasItem("orange slices"),
                () -> {
                    if (Rs2Widget.hasWidgetText("", 270, 14, false)) {
                        Rs2Widget.clickWidget(270, 14);
                    }
                    else {
                        Rs2Inventory.use("knife");
                        Rs2Inventory.interact("orange", "use");
                    }
                },
                5000,
                100000
        );
        doUntil(
                () -> Rs2Inventory.hasItem("dyed orange"),
                () -> {
                    Rs2Inventory.use("blue dye");
                    Rs2Inventory.interact("orange slices", "use");
                },
                5000,
                100000
        );
        Microbot.log("Making spicy maggots.");
        doUntil(
                () -> Rs2Inventory.hasItem("spicy maggots"),
                () -> {
                    Rs2Inventory.use("gnome spice");
                    Rs2Inventory.interact("fishing bait", "use");
                },
                5000,
                100000
        );
        DialogueHandler.talkToNPC("goblin cook", dialogue, 5);
        Rs2Inventory.interact("lumbridge teleport", "break");
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() < 5000, 100000);
        sleep(3000);
        Rs2Walker.walkTo(3208, 3212, 0);
        doUntil(
                () -> Rs2Player.getWorldLocation().getY() > 5000,
                () -> Rs2GameObject.interact(12348, "open"),
                3000,
                100000
        );
        Rs2Walker.walkTo(1861, 5325, 0);
        doUntil(
                () -> !Rs2Inventory.hasItem("slop of compromise"),
                () -> {
                    Rs2Inventory.use("slop of compromise");
                    Rs2GameObject.interact(12332, "use");
                },
                5000,
                100000
        );
        MiscellaneousUtilities.waitForQuestFinish();
        doUntil(
                () -> Rs2Player.getWorldLocation().getY() < 5000,
                () -> Rs2GameObject.interact(12349),
                3000,
                100000
        );
        sleep(3000);
    }
}
