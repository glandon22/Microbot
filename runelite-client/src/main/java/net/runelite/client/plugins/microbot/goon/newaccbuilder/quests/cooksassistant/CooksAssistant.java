package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.cooksassistant;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

public class CooksAssistant {
    private  void prep() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(
                new ArrayList<>(List.of(
                    new BankHandler.QuestItem("egg", 1, false, false, false),
                    new BankHandler.QuestItem("pot of flour", 1, false, false, false),
                    new BankHandler.QuestItem("lumbridge teleport", 1, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 1, false, false, false),
                    new BankHandler.QuestItem("bucket of milk", 1, false, false, false)
                )), true, true
        );
    }
    public boolean completeQuest() {
        prep();
        ArrayList<String> dialogue = new ArrayList<>();
        dialogue.add("What's wrong?");
        dialogue.add("Yes.");
        dialogue.add("I'm always happy to help a cook in distress.");
        dialogue.add("Actually, I know where to find this stuff.");
        Rs2Walker.walkTo(new WorldPoint(3209, 3214, 0), 3);
        Rs2Npc.interact("Cook", "Talk-to");
        System.out.println(DialogueHandler.handleConversation(dialogue, 15));
        MiscellaneousUtilities.waitForQuestFinish();
        return true;
    }

}
