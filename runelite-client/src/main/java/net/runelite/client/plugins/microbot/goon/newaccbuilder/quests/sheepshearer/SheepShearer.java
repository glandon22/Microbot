package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.sheepshearer;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

public class SheepShearer {
    final List<String> dialogue = new ArrayList<>(List.of(
            "I'm looking for a quest.",
            "Yes, okay. I can do that.",
            "I need to talk to you about shearing these sheep!",
            "Yes."
    ));
    private void travelToQuestStart() {
        System.out.println("Headed to quest start - Sheep Shearer.");
        Rs2Walker.walkTo(new WorldPoint(3189, 3272, 0), 2);
    }

    private  void prep() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(
                new ArrayList<>(List.of(
                        new BankHandler.QuestItem("ball of wool", 20, false, false, false),
                        new BankHandler.QuestItem("lumbridge teleport", 1, false, false, false),
                        new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
                )), true, true
        );
    }

    private boolean travelToGE() {
        // getting killed by dark wizards entering varrock from the south, need to path thru barb village probably
        System.out.println("Returning to GE.");
        Rs2Walker.walkTo(new WorldPoint(3164, 3484, 0), 3);
        return true;
    }

    public void completeQuest() {
        prep();
        travelToQuestStart();
        DialogueHandler.talkToNPCCutscene("Fred the farmer", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
