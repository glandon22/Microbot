package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.impcatcher;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

public class ImpCatcher {

    List<String> dialogue = new ArrayList<>(List.of(
            "Yes.",
            "Give me a quest please."
    ));

    public void completeQuest() {
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("yellow bead", 1, false, false, false),
                new BankHandler.QuestItem("white bead", 1, false, false, false),
                new BankHandler.QuestItem("black bead", 1, false, false, false),
                new BankHandler.QuestItem("red bead", 1, false, false, false)
        ), true, true);
        Rs2Walker.walkTo(3105, 3163, 2);
        DialogueHandler.talkToNPC("Wizard Mizgog", dialogue, 15);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
