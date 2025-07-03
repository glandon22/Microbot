package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.runemysteries;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

public class RuneMysteries {
    List<String> dialogue = new ArrayList<>(List.of(
            "Have you any quests for me?",
            "Yes.",
            "I'm looking for the head wizard.",
            "Okay, here you are.",
            "Yes, certainly.",
            "I've been sent here with a package for you.",
            "Go ahead."
    ));
    private void dukeOfLumby() {
        System.out.println("Talking to duke of lumbridge");
        Rs2Walker.walkTo(3211, 3222, 1, 2);
        DialogueHandler.talkToNPC("duke horacio", dialogue, 5);
        System.out.println("Completed conversation with duke of lumbridge.");
    }

    private void archmage() {
        System.out.println("Talking to archmage sedridor");
        Rs2Walker.walkTo(3097,9570,0,2);
        DialogueHandler.talkToNPC("archmage sedridor", dialogue, 5);
        System.out.println("Done talking to archmage sedridor");
    }

    private void aubury() {
        System.out.println("Talking to aubury");
        Rs2Walker.walkTo(3251, 3397,0,2);
        DialogueHandler.talkToNPC("aubury", dialogue, 5);
        System.out.println("Done talking to aubury");
    }

    public void completeQuest() {
        dukeOfLumby();
        archmage();
        aubury();
        archmage();
        MiscellaneousUtilities.waitForQuestFinish("Rune Mysteries");
    }
}
