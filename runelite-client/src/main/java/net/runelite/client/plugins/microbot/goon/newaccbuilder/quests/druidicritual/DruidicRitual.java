package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.druidicritual;

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

import static net.runelite.client.plugins.microbot.util.Global.sleep;

public class DruidicRitual {
    List<String> dialogue = new ArrayList<>(List.of("I'm in search of a quest.", "Okay, I will try and help.",
            "I've been sent to help purify the Varrock stone circle.",
            "Ok, I'll do that then.",
            "Yes."
    ));
    private void kaqemeex() {
        System.out.println("Starting druidic ritual.");
        Rs2Walker.walkTo(2918, 3484, 0, 2);
        DialogueHandler.talkToNPC("kaqemeex", dialogue, 5);
        System.out.println("started quest");
    }

    private void sanfew() {
        System.out.println("Talking to sanfew.");
        Rs2Walker.walkTo(2895, 3428, 1, 1);
        DialogueHandler.talkToNPC("sanfew", dialogue, 5);
        System.out.println("Finished talking to sanfew.");
    }

    private Runnable useOnCauldron(String item) {
        return () -> {
            System.out.println("Using " + item + " on cauldron.");
            Rs2Inventory.use(item);
            Rs2GameObject.interact(2142, "Use");
        };
    }

    private void taverleyDungeon() {
        System.out.println("Walking to cauldron entrance.");
        Rs2Walker.walkTo(2883, 9830, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        System.out.println("Walking into cauldron room.");
        Rs2Walker.walkTo(2895, 9830, 0, 2);
        Rs2Player.drinkPrayerPotionAt(12);
        Rs2Inventory.waitForInventoryChanges(useOnCauldron("raw chicken"), 600, 30000);
        Rs2Player.drinkPrayerPotionAt(12);
        Rs2Inventory.waitForInventoryChanges(useOnCauldron("raw beef"), 600, 30000);
        Rs2Player.drinkPrayerPotionAt(12);
        Rs2Inventory.waitForInventoryChanges(useOnCauldron("raw bear meat"), 600, 30000);
        Rs2Player.drinkPrayerPotionAt(12);
        Rs2Inventory.waitForInventoryChanges(useOnCauldron("raw rat meat"), 600, 30000);
        Rs2Walker.walkTo(2883, 9830, 0, 2);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        System.out.println("Finished enchanting meat, exiting cauldron room.");
    }

    public void completeQuest() {
        kaqemeex();
        sanfew();
        taverleyDungeon();
        sanfew();
        kaqemeex();
        MiscellaneousUtilities.waitForQuestFinish("Druidic Ritual");
        System.out.println("completed druidic ritual.");
    }
}
