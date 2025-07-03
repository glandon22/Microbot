package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.impcatcher;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;

public class ImpCatcher {
    DialogueHandler dialogueHandler;
    private void goToWizardsTower() {
        Rs2Walker.walkTo(3105, 3163, 2);
    }

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

    private boolean travelToGE() {
        System.out.println("Returning to GE.");
        Rs2Walker.walkTo(new WorldPoint(3164, 3484, 0), 3);
        return true;
    }

    public void doQuest() {
        goToWizardsTower();
        doDialogue();
        travelToGE();
    }
}
