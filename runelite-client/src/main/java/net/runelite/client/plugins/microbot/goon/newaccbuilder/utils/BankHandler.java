package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import lombok.Value;
import net.runelite.client.plugins.microbot.goon.utils.GoonBank;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class BankHandler {
    @Value
    public static class QuestItem {
        String name;
        int quantity;
        boolean noted;
        boolean withdrawAll;
        boolean withdrawAndEquip;
    }
    public static void withdrawQuestItems(List<QuestItem> items, boolean dumpInv, boolean dumpEquip) {
        GoonBank.openBank();
        if (dumpInv) {
            Rs2Bank.depositAll();
            sleepUntil(Rs2Inventory::isEmpty);
        }
        if (dumpEquip) {
            Rs2Bank.depositEquipment();
            sleep(900);
        }

        for (QuestItem item : items) {
            if (item.noted) {
                Rs2Bank.setWithdrawAsNote();
                sleep(600);
            }

            if (item.withdrawAndEquip) Rs2Bank.withdrawAndEquip(item.name);
            else if (item.withdrawAll) Rs2Bank.withdrawAll(item.name);
            else if (item.quantity == 1) Rs2Bank.withdrawOne(item.name);
            else Rs2Bank.withdrawX(item.name, item.quantity);

            if (item.noted) {
                Rs2Bank.setWithdrawAsItem();
                sleep(600);
            }
            sleep(100);
        }
        Rs2Bank.closeBank();
    }
}
