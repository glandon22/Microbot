package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.treegnomevillage;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.CombatHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class TreeGnomeVillage {
    ArrayList<BankHandler.QuestItem> treeGnomeItems = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("games necklace", 1, false, false, false),
                    new BankHandler.QuestItem("necklace of passage", 1, false, false, false),
                    new BankHandler.QuestItem("prayer potion(4)", 3, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 3, false, false, false),
                    new BankHandler.QuestItem("mind rune", 1000, false, false, false),
                    new BankHandler.QuestItem("fire rune", 1000, false, false, false),
                    new BankHandler.QuestItem("staff of air", 1, false, false, true),
                    new BankHandler.QuestItem("ring of dueling", 1, false, false, false),
                    new BankHandler.QuestItem("logs", 6, false, false, false),
                    new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                    new BankHandler.QuestItem("monk's robe", 1, false, false, true)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "Can I help at all?",
            "I would be glad to help.",
            "Yes.",
            "Ok, I'll gather some wood.",
            "I'll try my best.",
            "0001",
            "0002",
            "0003",
            "0004",
            "I will find the warlord and bring back the orbs."
    ));

    private void prep() {
        Microbot.log("Prepping for tree gnome village");
        Rs2Walker.walkTo(3164, 3484, 0, 3);
        BankHandler.withdrawQuestItems(treeGnomeItems, true, true);
        MiscellaneousUtilities.setSpell("fire strike");
    }

    private Runnable searchChest() {
        return () -> {
            if (Rs2GameObject.exists(2183)) Rs2GameObject.interact(2183, "open");
            else if (Rs2GameObject.exists(2182)) Rs2GameObject.interact(2182, "search");

        };
    }
    public void completeQuest() {
        prep();
        Microbot.log("Walking to quest start.");
        Rs2Walker.walkTo(2536, 3168, 0, 3);
        DialogueHandler.talkToNPC("king bolren", dialogue, 5);
        Microbot.log("Leaving gnome maze");
        Rs2Walker.walkTo(2515, 3158, 0, 1);
        DialogueHandler.talkToNPC("king bolren", List.of(), 5);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 3180);
        Rs2Walker.walkTo(2524, 3210, 0, 3);
        DialogueHandler.talkToNPC("commander montai", dialogue, 5);
        DialogueHandler.talkToNPC("commander montai", dialogue, 5);
        DialogueHandler.talkToNPC("commander montai", dialogue, 5);
        Rs2Walker.walkTo(2531, 3223, 0, 3);
        Rs2Walker.walkTo(2524, 3238, 0, 3);
        Rs2Walker.walkTo(2495, 3260, 0, 3);
        DialogueHandler.talkToNPC("tracker gnome 1", dialogue, 5);
        Rs2Walker.walkTo(2524,3256,0, 2);
        DialogueHandler.talkToNPC("tracker gnome 2", dialogue, 5);
        Rs2Walker.walkTo(2495, 3235,0, 2);
        DialogueHandler.talkToNPC("tracker gnome 3", dialogue, 5);
        Rs2Walker.walkTo(2507, 3210, 0, 2);
        for (String input : List.of("0001", "0002", "0003", "0004")) {
            Rs2GameObject.interact(2181, "fire");
            sleepUntil(Rs2Dialogue::isInDialogue);
            DialogueHandler.handleConversation(List.of(input), 5);
        }
        Rs2Walker.walkTo(2508, 3252, 0, 2);
        Rs2GameObject.interact(2185, "climb-over");
        sleepUntil(Rs2Dialogue::isInDialogue);
        DialogueHandler.handleConversation(List.of(), 2);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 3254);
        Rs2Walker.walkTo(2502, 3255, 0, 2);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2GameObject.interact(16683, "climb-up");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 1);
        //everything is working except getting the orb from the chest
        Rs2Inventory.waitForItemInInventory(searchChest(),"orb",  800, 10000);
        sleepUntil(() -> Rs2Inventory.hasItem(587));
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        // i think it fell thru down here but broke before actually talking to the tracker gnomes
        Rs2Walker.walkTo(2501, 3192, 0, 3);
        DialogueHandler.talkToNPC("elkoy", List.of("Yes please."), 5);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() <= 3177);
        sleep(1000);
        Rs2Walker.walkTo(2536, 3168, 0, 3);
        DialogueHandler.talkToNPC("king bolren", dialogue, 5);
        Rs2Walker.walkTo(2486, 3262, 0, 3);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(2457, 3297, 0, 3);
        Rs2Player.drinkPrayerPotionAt(20);
        DialogueHandler.talkToNPC("khazard warlord", dialogue, 2);
        CombatHandler.killMonsterWithPrayer(
                "khazard warlord", Rs2PrayerEnum.PROTECT_MELEE, 10, 5000
        );
        sleepUntil(() -> Rs2GroundItem.exists(588, 10000));
        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2GroundItem.pickup(588);
        }, 588, 600, 5000);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        Rs2Walker.walkTo(2501, 3192, 0, 3);
        DialogueHandler.talkToNPC("elkoy", List.of("Yes please."), 5);
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() <= 3177);
        sleep(1000);
        Rs2Walker.walkTo(2536, 3168, 0, 3);
        DialogueHandler.talkToNPC("king bolren", dialogue, 30);
        MiscellaneousUtilities.waitForQuestFinish();
        System.out.println("completed tree gnome village");
    }
}
