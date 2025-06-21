package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.cooksassistant;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CooksAssistant {
    public boolean completeQuest() {
        ArrayList<String> dialogue = new ArrayList<>();
        dialogue.add("What's wrong?");
        travelToLumby();
        Rs2Npc.interact("Cook", "Talk-to");
        System.out.println(DialogueHandler.handleConversation(dialogue, 15));
        return true;
    }

    private boolean travelToLumby() {
        System.out.println("im off");
        Rs2Walker.walkTo(new WorldPoint(3209, 3214, 0), 3);
        return true;
    }
}
