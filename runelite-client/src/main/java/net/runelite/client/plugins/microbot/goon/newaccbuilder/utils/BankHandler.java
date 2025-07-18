package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import lombok.Value;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;

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
        Rs2Bank.openBank();
        if (dumpInv) Rs2Bank.depositAll();
        if (dumpEquip) Rs2Bank.depositEquipment();

        for (QuestItem item : items) {
            if (item.noted) Rs2Bank.setWithdrawAsNote();

            if (item.withdrawAndEquip) Rs2Bank.withdrawAndEquip(item.name);
            else if (item.withdrawAll) Rs2Bank.withdrawAll(item.name);
            else if (item.quantity == 1) Rs2Bank.withdrawOne(item.name);
            else Rs2Bank.withdrawX(item.name, item.quantity);

            if (item.noted) Rs2Bank.setWithdrawAsItem();
        }

        Rs2Bank.closeBank();
        System.out.println("RS2bank: " + Rs2Bank.isOpen());
    }
}
