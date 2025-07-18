package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.romeoandjuliet;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class RomeoAndJuliet {
    List<String> dialogue = new ArrayList<>(List.of("Yes, I have seen her actually!",
            "Yes, ok, I'll let her know.",
            "Ok, thanks.",
            "Talk about something else.",
            "Talk about Romeo & Juliet.",
            "Yes.",
            "Ok, thanks."));

    private void talkToNPC(String name, List<String> lines, int timeout) {
        Rs2Npc.interact(name, "talk-to");
        DialogueHandler.handleConversation(lines, timeout);
    }

    public void completeQuest() {
        talkToNPC("romeo", dialogue, 5);
        Rs2Walker.walkTo(3160, 3425, 1, 1);
        talkToNPC("juliet", dialogue,5 );
        Rs2Walker.walkTo(3212, 3423, 0, 2);
        talkToNPC("romeo", dialogue,5 );
        Rs2Walker.walkTo(3248, 3479, 0, 2);
        talkToNPC("father lawrence", dialogue,5 );
        Rs2Walker.walkTo(3190, 3403, 0, 2);
        talkToNPC("apothecary", dialogue,5 );
        Rs2Walker.walkTo(3160, 3425, 1, 1);
        // long cutscene
        talkToNPC("juliet", dialogue,10);
        Rs2Walker.walkTo(3212, 3423, 0, 2);
        // long cutscene
        talkToNPC("romeo", dialogue, 30);
        MiscellaneousUtilities.waitForQuestFinish();
        System.out.println("Finished romeo and juliet.");
    }
}
