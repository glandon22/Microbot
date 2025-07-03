package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.vampireslayer;

import net.runelite.api.Actor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class VampireSlayer {
    ArrayList<String> dialogue1 = new ArrayList<>(List.of(
            "Ok, I'm up for an adventure.", "Yes."
    ));
    public void startQuest() {
        Rs2Walker.walkTo(3099, 3268, 0, 2);
        Rs2Npc.interact("morgan", "talk-to");
        DialogueHandler.handleConversation(dialogue1, 5);
    }

    public void drHarlow() {
        Rs2Walker.walkTo(3220, 3399, 0, 3);
        Rs2Npc.interact("dr harlow", "talk-to");
        DialogueHandler.handleConversation(new ArrayList<>(List.of("Morgan needs your help!")), 5);
        Rs2Npc.interact("dr harlow", "talk-to");
        DialogueHandler.handleConversation(new ArrayList<>(List.of()), 5);
    }
    public void completeQuest() {
        startQuest();
        drHarlow();
        Rs2Walker.walkTo(3077, 9773, 0, 2);
        System.out.println("clicking coffin");
        Rs2GameObject.interact("coffin", "Open");
        do {
            Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
            Actor a = Rs2Player.getInteracting();
            if (a != null && a.isDead()) {
                System.out.println(a.getName() + " is dead");
                break;
            } else if (a != null && !a.isDead()) System.out.println(a.getName() + " is still alive");

        } while (true);
        System.out.println("fight is over");
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        sleepUntil(() -> Rs2Widget.hasWidget("Vampyre Slayer"));
        System.out.println("Vampire slayer complete.");
        //153,0 is the widget id of the quest complete screen
    }
}
