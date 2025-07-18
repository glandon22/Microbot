package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.fightarena;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.CombatHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class FightArena {
    ArrayList<BankHandler.QuestItem> items = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("prayer potion", 3, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 5, false, false, false),
                    new BankHandler.QuestItem("ardougne teleport", 1, false, false, false),
                    new BankHandler.QuestItem("mind rune", 1000, false, false, false),
                    new BankHandler.QuestItem("coins", 1000, false, false, false),
                    new BankHandler.QuestItem("fire rune", 1000, false, false, false),
                    new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                    new BankHandler.QuestItem("monk's robe", 1, false, false, true),
                    new BankHandler.QuestItem("staff of air", 1, false, false, true)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "Can I help you?",
            "Yes.",
            "I'd like a Khali Brew please."
    ));
    private void prep() {
        Rs2Walker.walkTo(2945, 3369, 0, 1);
        BankHandler.withdrawQuestItems(items, true, true);
    }

    public void completeQuest() {
        prep();
        Rs2Walker.walkTo(2565, 3201, 0);
        DialogueHandler.talkToNPC("lady servil", dialogue, 5);
        Rs2Walker.walkTo(2611,3193,0);
        Rs2Inventory.waitForItemInInventory(() -> {
            Rs2GameObject.interact(75);
        }, 74, 1200, 60000);
        Rs2Walker.walkTo(2617, 3173, 0);
        Rs2Inventory.equip(74);
        Rs2Inventory.equip(75);
        Rs2Walker.walkTo(2616,3144,0);
        DialogueHandler.talkToNPC("head guard", dialogue, 5);
        Rs2Walker.walkTo(2566, 3142,0, 1);
        DialogueHandler.talkToNPC("khazard barman", dialogue, 5);
        Rs2Walker.walkTo(2616,3144,0);
        DialogueHandler.talkToNPC("head guard", dialogue, 5);
        Rs2Walker.walkTo(2619, 3165, 0);
        MiscellaneousUtilities.setSpell("fire strike");
        //too much time tanking hits i died
        // need to stop and revisit this
        sleep(100000);
        DialogueHandler.talkToNPC("sammy servil", dialogue, 7);
        sleepUntil(() -> Rs2Npc.getNpc("khazard ogre") != null, 50000);
        CombatHandler.killMonsterWithPrayer("khazard ogre", Rs2PrayerEnum.PROTECT_MELEE, 10, 5000);
        DialogueHandler.handleConversation(dialogue, 5);
        sleepUntil(() -> Rs2Npc.getNpc("khazard scorpion") != null, 50000);
        CombatHandler.killMonsterWithPrayer("khazard scorpion", Rs2PrayerEnum.PROTECT_MELEE, 10, 5000);
        DialogueHandler.handleConversation(dialogue, 3);
        sleepUntil(() -> Rs2Npc.getNpc("bouncer") != null, 50000);
        CombatHandler.killMonsterWithPrayer("bouncer", Rs2PrayerEnum.PROTECT_MELEE, 10, 5000);
        DialogueHandler.handleConversation(dialogue, 3);
        Rs2GameObject.interact(46563, "quick-escape");
        sleep(5000);
        Rs2Walker.walkTo(2610, 3149, 0, 1);
        Rs2Walker.walkTo(2565, 3201, 0);
        DialogueHandler.talkToNPC("lady servil", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
