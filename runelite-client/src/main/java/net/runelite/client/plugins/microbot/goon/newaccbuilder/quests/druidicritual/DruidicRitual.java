package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.druidicritual;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.runelite.client.plugins.microbot.util.Global.*;

public class DruidicRitual {
    List<String> dialogue = new ArrayList<>(List.of("I'm in search of a quest.", "Okay, I will try and help.",
            "I've been sent to help purify the Varrock stone circle.",
            "Ok, I'll do that then.",
            "Yes."
    ));
    private void kaqemeex() {
        Microbot.log("Starting druidic ritual.");
        Rs2Walker.walkTo(2918, 3484, 0, 2);
        DialogueHandler.talkToNPC("kaqemeex", dialogue, 5);
    }

    private void sanfew() {
        Microbot.log("Talking to sanfew.");
        Rs2Walker.walkTo(2895, 3428, 1, 1);
        DialogueHandler.talkToNPC("sanfew", dialogue, 5);
        Microbot.log("Finished talking to sanfew.");
    }

    private void taverleyDungeon() {
        ArrayList<String> meats = new ArrayList<>(List.of(
                "raw bear meat", "raw rat meat", "raw beef", "raw chicken"
        ));
        Microbot.log("Walking to cauldron entrance.");
        Rs2Walker.walkTo(2883, 9830, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Microbot.log("Walking into cauldron room.");
        Rs2Walker.walkTo(2895, 9830, 0, 0);
        Rs2Player.drinkPrayerPotionAt(12);
        doUntilSuccess(
                () -> Rs2Inventory.hasItem("enchanted bear") && Rs2Inventory.hasItem("enchanted rat") && Rs2Inventory.hasItem("enchanted chicken") && Rs2Inventory.hasItem("enchanted beef"),
                () -> {
                    for (String meat : meats) {
                        if (Rs2Inventory.hasItem(meat)) {
                            Rs2Inventory.use(meat);
                            Rs2GameObject.interact(2142, "use");
                            sleep(300);
                        }
                    }
                },
                1000
        );
        Rs2Walker.walkTo(2883, 9830, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        Microbot.log("Finished enchanting meat, exiting cauldron room.");
    }

    public void completeQuest() {
        kaqemeex();
        sanfew();
        taverleyDungeon();
        sanfew();
        kaqemeex();
        MiscellaneousUtilities.waitForQuestFinish();
        Microbot.log("completed druidic ritual.");
    }
}
