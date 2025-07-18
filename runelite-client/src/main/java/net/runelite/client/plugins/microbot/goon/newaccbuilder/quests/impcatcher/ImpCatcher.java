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
            "Why can't you sleep, what's wrong?",
            "Can I help at all?",
            "Is there anything else I can help with?",
            "Who's Brother Cedric?",
            "Where should I look?",
            "Yes, I'd be happy to!"
    ));

    private void doDialogue() {
        ArrayList<String> dialogue = new ArrayList<>();
        dialogue.add("Give me a quest please.");
        dialogue.add("Yes.");
        Rs2Npc.interact("Wizard Mizgog", "Talk-to");
        DialogueHandler.handleConversation(dialogue, 15);
        Rs2Npc.interact("Wizard Mizgog", "Talk-to");
        DialogueHandler.handleConversation(dialogue, 15);
        Rs2Keyboard.keyPress(27);
    }

    public void completeQuest() {
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("yellow bead", 1, false, false, false),
                new BankHandler.QuestItem("white bead", 1, false, false, false),
                new BankHandler.QuestItem("black bead", 1, false, false, false),
                new BankHandler.QuestItem("red bead", 1, false, false, false)
        ), true, true);
        Rs2Walker.walkTo(3105, 3163, 2);
        DialogueHandler.talkToNPC("Wizard Mizgog", dialogue, 15);
        DialogueHandler.talkToNPC("Wizard Mizgog", dialogue, 15);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
