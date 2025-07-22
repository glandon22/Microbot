package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.rfd;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.Arrays;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class RFDStart {
    private static final List<ItemBuyer.ItemToBuy> items = List.of(
            new ItemBuyer.ItemToBuy("fruit blast", 1, 10000, false),
            new ItemBuyer.ItemToBuy("eye of newt", 1, 10000, false),
            new ItemBuyer.ItemToBuy("greenman's ale", 1, 10000, false),
            new ItemBuyer.ItemToBuy("ashes", 1, 10000, false)
    );
    private static final List<String> dialogue = Arrays.asList(
            "Do you have any other quests for me?",
            "Angry! It makes me angry!",
            "I don't really care to be honest.",
            "What seems to be the problem?",
            "YES",
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
        Rs2GameObject.interact("crate", "buy");
        sleepUntil(() -> Rs2Widget.getWidget(300, 16) != null);
        //gonna have to add the ability to right click stuff on widgets
        //300,16,1
        // to buy tomato
    }

    public static void completeQuest() {
        prep();
        /*Rs2Walker.walkTo(3208, 3212, 0);
        DialogueHandler.talkToNPC("cook", dialogue, 5);
        Rs2Inventory.use("ashes");
        Rs2Inventory.interact("fruit blast", "use");
        sleepUntil(() -> Rs2Inventory.hasItem("dirty blast"));
        DialogueHandler.talkToNPC("cook", dialogue, 5);*/
    }
}
