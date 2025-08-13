package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.rfd;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.shop.Rs2Shop;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.Arrays;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.*;

public class RFDStart {
    private static final List<ItemBuyer.ItemToBuy> items = List.of(
            new ItemBuyer.ItemToBuy("fruit blast", 1, 10000, false),
            new ItemBuyer.ItemToBuy("eye of newt", 1, 10000, false),
            new ItemBuyer.ItemToBuy("greenman's ale", 1, 10000, true),
            new ItemBuyer.ItemToBuy("ashes", 1, 10000, true)
    );
    private static final List<String> dialogue = Arrays.asList(
            "Do you have any other quests for me?",
            "Angry! It makes me angry!",
            "I don't really care to be honest.",
            "What seems to be the problem?",
            "YES",
            "Yes.",
            "About those ingredients you wanted for the banquet..."
    );
    private static void prep() {
        MiscellaneousUtilities.walkToGE();
        ItemBuyer.buyItems(items);
        ItemBuyer.ensureAllOffersCollected(true);
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("lumbridge teleport", 1, false, false, false),
                new BankHandler.QuestItem("varrock teleport", 1, false, false, false),
                new BankHandler.QuestItem("coins", 1000, false, false, false),
                new BankHandler.QuestItem("ashes", 1, false, false, false),
                new BankHandler.QuestItem("fruit blast", 1, false, false, false),
                new BankHandler.QuestItem("greenman's ale", 1, false, false, false),
                new BankHandler.QuestItem("eye of newt", 1, false, false, false)
        ), false, false);
        Rs2Walker.walkTo(3229, 3410, 0);
        doUntil(
                Rs2Shop::isOpen,
                () -> Rs2GameObject.interact("crate", "buy"),
                5000,
                100000
        );
        Rs2Shop.buyItem("rotten tomato", "1");
        Rs2Shop.closeShop();
    }

    public static void completeQuest() {
        prep();
        Rs2Walker.walkTo(3208, 3212, 0);
        DialogueHandler.talkToNPC("cook", dialogue, 5);
        doUntil(
                () -> Rs2Inventory.hasItem("dirty blast"),
                () -> {
                    Rs2Inventory.use("ashes");
                    Rs2Inventory.interact("fruit blast", "use");
                },
                2000,
                1000000
        );
        DialogueHandler.talkToNPC("cook", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
        DialogueHandler.handleConversation(dialogue, 5);
        doUntil(
                Rs2Dialogue::isInCutScene,
                () -> Rs2GameObject.interact(12348, "open"),
                3000,
                100000
        );
        DialogueHandler.handleConversationWithCutscene(dialogue, 15);
        System.out.println("Completed the long cutscene.");
        doUntil(
                () -> Rs2Player.getWorldLocation().getY() < 5000,
                () -> Rs2GameObject.interact(12349),
                3000,
                100000
        );
        sleep(3000);
    }
}
