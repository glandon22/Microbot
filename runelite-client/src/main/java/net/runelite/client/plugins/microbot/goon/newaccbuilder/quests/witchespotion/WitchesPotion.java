package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witchespotion;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class WitchesPotion {
    ArrayList<String> dialogue = new ArrayList<>(List.of(
            "I am in search of a quest.", "Yes.",
            "Yes, help me become one with my darker side."
    ));

    public boolean completeQuest() {
        //Walk to the bank in draynor village
        Rs2Walker.walkTo(3093, 3242, 0);
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("iron scimitar", 1, false, false, true),
                new BankHandler.QuestItem("eye of newt", 1, false, false, false),
                new BankHandler.QuestItem("raw beef", 1, false, false, false),
                new BankHandler.QuestItem("onion", 1, false, false, false)
        ), true, true);
        //make sure the inventory tab is open
        doUntil(
                () -> Rs2Inventory.hasItem("burnt meat", true),
                () -> {
                    Rs2Walker.walkTo(2968, 3210, 0, 3);
                    Rs2GameObject.interact(9682, "cook");
                    sleepUntil(() -> Rs2Widget.hasWidget("like to cook"));
                    Rs2Widget.clickWidget(270,14);
                    Rs2Inventory.waitForInventoryChanges(5000);
                }, 3000, 300000
        );

        Rs2Walker.walkTo(new WorldPoint(2968, 3205, 0), 2);
        DialogueHandler.talkToNPCCutscene("Hetty", dialogue, 5);
        Rs2Walker.walkTo(new WorldPoint(2956, 3203, 0), 2);
        doUntil(
                () -> Rs2Inventory.hasItem("rat's tail"),
                () -> {
                    Rs2GroundItem.take("rat's tail", 10);
                    if (Rs2Player.getInteracting() == null) Rs2Npc.attack("rat");
                },
                600, 300000
        );
        Rs2Walker.walkTo(new WorldPoint(2968, 3205, 0), 2);
        DialogueHandler.talkToNPCCutscene("Hetty", dialogue, 5);
        Rs2GameObject.interact(2024, "Drink-from");
        DialogueHandler.handleConversation(dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
        return true;
    }
}
