package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.monksfriend;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class MonksFriend {

    ArrayList<BankHandler.QuestItem> items = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("games necklace", 1, false, false, false),
                    new BankHandler.QuestItem("jug of water", 1, false, false, false),
                    new BankHandler.QuestItem("logs", 1, false, false, false)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "Yes.",
            "Why can't you sleep, what's wrong?",
            "Can I help at all?",
            "Is there anything else I can help with?",
            "Who's Brother Cedric?",
            "Where should I look?",
            "Yes, I'd be happy to!"
    ));
    private void prep() {
        Rs2Walker.walkTo(2653, 3284, 0, 3);
        BankHandler.withdrawQuestItems(items, true, true);
    }

    public void completeQuest() {
        prep();
        Rs2Walker.walkTo(2606, 3210, 0, 3);
        DialogueHandler.talkToNPC("brother omad", dialogue, 3);
        Rs2Walker.walkTo(2566, 9605, 0, 3);
        sleepUntil(() -> Rs2GroundItem.exists(90, 10), 90000);
        Rs2GroundItem.take(90);
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Walker.walkTo(2561, 9620, 0, 3);
        Rs2Walker.walkTo(2606, 3210, 0, 3);
        DialogueHandler.talkToNPC("brother omad", dialogue, 3);
        DialogueHandler.talkToNPC("brother omad", dialogue, 3);
        Rs2Walker.walkTo(2621, 3257, 0, 3);
        DialogueHandler.talkToNPC("brother cedric", dialogue, 3);
        DialogueHandler.talkToNPC("brother cedric", dialogue, 3);
        DialogueHandler.talkToNPC("brother cedric", dialogue, 3);
        Rs2Walker.walkTo(2606, 3210, 0, 3);
        DialogueHandler.talkToNPC("brother omad", dialogue, 45);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
