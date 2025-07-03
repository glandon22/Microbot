package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witchespotion;

import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class WitchesPotion {
    ArrayList<String> dialogue1 = new ArrayList<>(List.of(
            "I am in search of a quest.", "Yes."
    ));
    ArrayList<String> dialogue2 = new ArrayList<>(List.of(
            "Yes, help me become one with my darker side."
    ));
    private void withdrawQuestItems() {
        Rs2Bank.openBank();
        Rs2Bank.depositAll();
        Rs2Bank.depositEquipment();
        Rs2Bank.setWithdrawAsNote();
        Rs2Bank.withdrawAll("dragon bones");
        Rs2Bank.setWithdrawAsItem();
        Rs2Bank.withdrawAndEquip("iron scimitar");
        Rs2Bank.withdrawOne("eye of newt");
        Rs2Bank.withdrawOne("raw beef");
        Rs2Bank.withdrawOne("onion");
        Rs2Bank.withdrawX("coins", 100000);
        Rs2Bank.closeBank();
    }

    private void doDialogue(List<String> dialogueOptions) {
        Rs2Npc.interact("Hetty", "Talk-to");
        DialogueHandler.handleConversation(dialogueOptions, 5);
    }

    public boolean completeQuest() {
        //Walk to the bank in draynor village
        Rs2Walker.walkTo(3093, 3242, 0);
        withdrawQuestItems();
        //make sure the inventory tab is open
        Rs2Keyboard.keyPress(27);
        Rs2Walker.walkTo(2968, 3210, 0, 3);
        Rs2GameObject.interact("range", "cook");
        sleepUntil(() -> Rs2Widget.hasWidget("like to cook"));
        Rs2Widget.clickWidget(270,14);
        Rs2Inventory.waitForInventoryChanges(5000);
        if (!Rs2Inventory.hasItem("burnt meat")) {
            Rs2GameObject.interact("range", "cook");
            sleepUntil(() -> Rs2Widget.hasWidget("what would you like to cook"));
            Rs2Widget.clickWidget(270,14);
            Rs2Inventory.waitForInventoryChanges(5000);
        }

        Rs2Walker.walkTo(new WorldPoint(2968, 3205, 0), 2);
        doDialogue(dialogue1);
        Rs2Walker.walkTo(new WorldPoint(2956, 3203, 0), 2);
        Rs2Npc.interact("rat", "attack");
        sleepUntil(() -> Rs2GroundItem.exists("rat's tail", 5), 30000);
        System.out.println("found a rat tail!");
        Rs2GroundItem.pickup("rat's tail", 5);
        Rs2Walker.walkTo(new WorldPoint(2968, 3205, 0), 2);
        doDialogue(dialogue2);
        Rs2GameObject.interact("cauldron", "Drink-from");
        DialogueHandler.handleConversation(dialogue2, 5);
        sleepUntil(() -> Rs2Widget.hasWidget("Total Quest Points"));
        Rs2Keyboard.keyPress(KeyEvent.VK_ESCAPE);
        return true;
    }
}
