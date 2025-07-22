package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras;

import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GlassBlower {
    private static void prep() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("glassblowing pipe", 1, false, false, false)
        ), true, true);
    }

    private static char determineItem(int craftingLevel) {
        if (craftingLevel >= 87) {
            return '8'; // Crystal flask (level 87, menu position 8)
        } else if (craftingLevel >= 54) {
            return '7'; // Orb (level 54, menu position 7)
        } else if (craftingLevel >= 49) {
            return '6'; // Fishbowl (level 49, menu position 6)
        } else if (craftingLevel >= 46) {
            return '5'; // Unpowered orb (level 46, menu position 5)
        } else if (craftingLevel >= 42) {
            return '4'; // Lantern lens (level 42, menu position 4)
        } else if (craftingLevel >= 33) {
            return '3'; // Vial (level 33, menu position 3)
        } else if (craftingLevel >= 4) {
            return '2'; // Oil lamp (level 4, menu position 2)
        } else {
            return '1'; // Beer glass (level 1, menu position 1)
        }
    }

    public static void run() {
        prep();
        while (true) {
            if (Microbot.isGainingExp) continue;
            else if (Rs2Inventory.hasItem("molten glass")) {
                Rs2Inventory.use("glassblowing pipe");
                Rs2Inventory.interact("molten glass", "use");
                sleepUntil(() -> Rs2Widget.hasWidget("how many"));
                Rs2Keyboard.keyPress(determineItem(Rs2Player.getRealSkillLevel(Skill.CRAFTING)));
                sleep(2000);
            }
            else {
                Rs2Bank.openBank();
                Rs2Bank.depositAllExcept("glassblowing pipe");
                sleepUntil(() -> Rs2Inventory.size() <= 1);
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("molten glass", 1, false, true, false)
                ), false, false);
                boolean result = sleepUntil(() -> Rs2Inventory.hasItem("molten glass"), 5000);
                if (!result) return;
            }
        }
    }
}
