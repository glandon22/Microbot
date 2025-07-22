package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class PotTrainer {
    private static void prep() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(List.of(), true, true);

    }

    public static void run() {
        //prep();
        while (true) {
            if (Microbot.isGainingExp) continue;
            else if (Rs2Inventory.hasItem("eye of newt") && Rs2Inventory.hasItem("guam potion (unf)")) {
                Rs2Inventory.use("eye of newt");
                Rs2Inventory.interact("guam potion (unf)", "use");
                sleepUntil(() -> Rs2Widget.hasWidget("how many"));
                Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);
                sleep(1200);
            }
            else {
                // if i have less than 13 of something it will open the bank, fail to withdraw, and just do it over
                // and over again. have to figure out how to chagne the withdrawx function
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("eye of newt", 14, false, false, false),
                        new BankHandler.QuestItem("guam potion (unf)", 14, false, false, false)
                ), true, false);
                boolean result = sleepUntil(() -> Rs2Inventory.hasItem("eye of newt") && Rs2Inventory.hasItem("guam potion (unf)"), 5000);
                if (!result) return;
            }
        }
    }
}
