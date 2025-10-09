package net.runelite.client.plugins.microbot.goon.plankmaker;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.globval.WidgetIndices;
import net.runelite.client.plugins.microbot.goon.GoonUtils;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.magic.Rs2Magic;
import net.runelite.client.plugins.microbot.util.magic.Rs2Spells;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import org.slf4j.event.Level;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class PlankMakerScript extends Script {
    GoonUtils goonUtils = new GoonUtils();
    GOTR gotr= new GOTR();
    public boolean run() {
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            //gotr.playMinigame();
            if (!Rs2Inventory.hasItem("coins")) {
                Microbot.log("Out of coins - exiting", Level.WARN);
                mainScheduledFuture.cancel(true);
            }

            if (!Rs2Inventory.hasItem("mahogany logs")) {
                Microbot.log("Out of mahogany logs - banking for more.");
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("mahogany logs", 1, false, true, false)
                ), true, false, false);
                sleepUntil(() -> Rs2Inventory.hasItem("mahogany logs"), 2000);
            }

            if (Microbot.isGainingExp) {
                start.set(goonUtils.breakHandler(start.get()));
                Microbot.log("Making planks.");
            }
            else {
                Microbot.log("Casting plank make.");
                Rs2Magic.cast(Rs2Spells.PLANK_MAKE);
                Rs2Inventory.interact("mahogany logs");
                sleep(2000);
            }
        }, 0, 600, TimeUnit.MILLISECONDS);
        return false;
    }
}
