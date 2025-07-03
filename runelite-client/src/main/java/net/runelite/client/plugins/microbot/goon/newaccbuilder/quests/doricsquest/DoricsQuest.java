package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.doricsquest;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.ArrayList;
import java.util.List;

public class DoricsQuest {
    public void completeQuest() {
        List<String> dialogue = new ArrayList<>(List.of(
                "I wanted to use your anvils.",
                "Yes, I will get you the materials.",
                "Yes."
        ));
        Rs2Walker.walkTo(2952, 3450, 0, 2);
        DialogueHandler.talkToNPC("doric", dialogue, 5);
        boolean complete = MiscellaneousUtilities.waitForQuestFinish("Doric's Quest");
        System.out.println("Quest complete status: " + complete);
    }
}
