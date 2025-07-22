package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class TeaStallStealFletch {
    private static void prep() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("arrow shaft", 1, false, true, false),
                new BankHandler.QuestItem("feather", 1, false, true, false)
        ), true, true);
    }

    private static void core() {
        if (Rs2Inventory.isFull()) Rs2Inventory.dropAll("cup of tea");
        else if (Rs2GameObject.exists(635)) {
            Rs2GameObject.interact(635, "steal-from");
            Rs2Inventory.waitForInventoryChanges(3000);
        }
        else {
            Rs2Inventory.use("feather");
            Rs2Inventory.interact("shaft", "use");
            sleepUntil(() -> Rs2Widget.hasWidget("sets of 15"), 2000);
            Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);
            sleepUntil(() -> Rs2GameObject.exists(635), 5000);
        }
    }
    public static void run() {
        /*prep();
        Rs2Walker.walkTo(3268, 3412, 0);*/
        while (Rs2Inventory.hasItem("feather") && Rs2Inventory.hasItem("arrow shaft")) {
            core();
        }
    }
}
