package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.goblindiplomacy;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GoblinDiplomacy {
    List<String> dialogue = new ArrayList<>(List.of(
            "So how is life for the goblins?",
            "Yes.",
            "Yes, Wartface looks fat",
            "Yes, he looks fat!",
            "Yes, he looks fat",
            "Do you want me to pick an armour colour for you?",
            "What about a different colour?",
            "I have some orange armour here.",
            "I have some blue armour here.",
            "I have some brown armour here.",
            "Okay, I'll be back soon.",
            "Yes, he looks fat."
    ));

    private void dyeMail() {
        Rs2Inventory.combine("red dye", "yellow dye");
        Rs2Inventory.waitForInventoryChanges(3000);
        Rs2Inventory.combine("blue dye", "goblin mail");
        Rs2Inventory.waitForInventoryChanges(3000);
        // this needs to target by id bc "goblin mail" is not search exactly
        // and it will apply the dye to a mail that is already dyed blue
        Rs2Inventory.combine(1769, 288);
        Rs2Inventory.waitForInventoryChanges(3000);
    }

    public void completeQuest() {
        Rs2Walker.walkTo(2957,3512, 0 ,2);
        DialogueHandler.talkToNPC("general wartface", dialogue, 15);
        dyeMail();
        DialogueHandler.talkToNPC("general wartface", dialogue, 15);
        DialogueHandler.talkToNPC("general wartface", dialogue, 15);
        DialogueHandler.talkToNPC("general wartface", dialogue, 15);
        MiscellaneousUtilities.waitForQuestFinish("Goblin Diplomacy");
    }
}
