package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.knightssword;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class KnightsSword {
    ArrayList<BankHandler.QuestItem> items = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("black pickaxe", 1, false, false, false),
                    new BankHandler.QuestItem("iron bar", 2, false, false, false),
                    new BankHandler.QuestItem("redberry pie", 1, false, false, false),
                    new BankHandler.QuestItem("prayer potion", 3, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 5, false, false, false),
                    new BankHandler.QuestItem("falador teleport", 5, false, false, false),
                    new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                    new BankHandler.QuestItem("monk's robe", 1, false, false, true)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "And how is life as a squire?",
            "Yes.",
            "I can make a new sword if you like...",
            "So would these dwarves make another one?",
            "Ok, I'll give it a go.",
            "What do you know about the Imcando dwarves?",
            "Would you like a redberry pie?",
            "Can you make a special sword for me?",
            "About that sword...",
            "Can you make that replacement sword now?"
    ));
    private void prep() {
        Rs2Walker.walkTo(2945, 3369, 0, 1);
        BankHandler.withdrawQuestItems(items, true, true);
    }

    private boolean findWorldNoVyvin() {
        Rs2NpcModel vyvin = Rs2Npc.getNpc("sir vyvin");
        return vyvin != null && vyvin.getWorldLocation().getY() >= 3339;
    }

    public void completeQuest() {
        prep();
        Rs2Walker.walkTo(2972, 3343, 0, 1);
        DialogueHandler.talkToNPC("squire", dialogue, 5);
        Rs2Walker.walkTo(3209,3493,0);
        DialogueHandler.talkToNPC("reldo", dialogue, 5);
        Rs2Walker.walkTo(2994, 3144, 0);
        DialogueHandler.talkToNPC("thurgo", dialogue, 5);
        Rs2Walker.walkTo(2972, 3343, 0, 1);
        DialogueHandler.talkToNPC("squire", dialogue, 5);
        Rs2Walker.walkTo(2996,3341,0);
        Rs2Walker.walkTo(2982,3336,1);
        Rs2Walker.walkTo(2982,3339,2);
        Rs2Walker.walkTo(2984,3336,2, 0);
        while (!Rs2Inventory.hasItem("portrait")) {
            boolean isSafe = findWorldNoVyvin();
            System.out.println("issafe : " + isSafe);
            if (isSafe) {
                Rs2GameObject.interact(2271, "open");
                sleepUntil(() -> Rs2GameObject.exists(2272), 5000);
                Rs2Inventory.waitForItemInInventory(() -> {
                    if (Rs2Dialogue.isInDialogue()) Rs2Dialogue.clickContinue();
                    else Rs2GameObject.interact(2272, "search");
                }, "portrait", 600, 9000);
                if (!Rs2Inventory.hasItem("portrait")) {
                    System.out.println("hopping");

                }
            }

            else MiscellaneousUtilities.hopWorlds(-1);
        }
        Rs2Keyboard.keyPress(KeyEvent.VK_ESCAPE);
        Rs2Walker.walkTo(2994, 3144, 0);
        DialogueHandler.talkToNPC("thurgo", dialogue, 5);
        Rs2Walker.walkTo(3019, 9581, 0);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
        Rs2Walker.walkTo(3049, 9567, 0);
        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2Player.drinkPrayerPotionAt(10);
            Rs2GameObject.interact(11378, "mine");
        },"blurite ore", 6000, 120000);
        Rs2Player.drinkPrayerPotionAt(10);
        Rs2Walker.walkTo(3002, 9548, 0);
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        Rs2GameObject.interact(17385, "climb-up");
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() < 5000, 10000);
        Rs2Walker.walkTo(2994, 3144, 0);
        DialogueHandler.talkToNPC("thurgo", dialogue, 5);
        Rs2Walker.walkTo(2972, 3343, 0, 1);
        DialogueHandler.talkToNPC("squire", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
