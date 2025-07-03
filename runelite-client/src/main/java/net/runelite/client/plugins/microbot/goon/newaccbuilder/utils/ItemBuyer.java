package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import lombok.Value;
import net.runelite.client.plugins.microbot.util.grandexchange.GrandExchangeSlots;
import net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class ItemBuyer {
    @Value
    public static class ItemToBuy {
        String name;
        int quantity;
        int customPrice; // -1 to ignore
    }

    public boolean buyItems(List<ItemToBuy> items) {
        Rs2GrandExchange.openExchange();
        for (ItemToBuy item : items) {
            boolean result;
            if (item.customPrice == -1) {
                result = Rs2GrandExchange.buyItemAboveXPercent(item.name, item.quantity, 100);
            }
            else {
                result = Rs2GrandExchange.buyItem(item.name, item.customPrice, item.quantity);
            }
            if (!result) {
                System.out.println("Failed to but " + item.name);
                return false;
            }
            sleepUntil(Rs2GrandExchange::hasFinishedBuyingOffers);
            System.out.println(Rs2GrandExchange.getSlot(GrandExchangeSlots.SEVEN));
            Rs2GrandExchange.collectToBank();
        }
        return true;
    }

    public void ensureAllOffersCollected(boolean closeScreen) {
        sleep(300, 400);
        Rs2GrandExchange.collectToBank();
        if (closeScreen) {
            Rs2Keyboard.keyPress(27);
        }
    }
}
