package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.vampireslayer;

import net.runelite.api.Actor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.CombatHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class VampireSlayer {
    ArrayList<String> dialogue = new ArrayList<>(List.of(
            "Ok, I'm up for an adventure.", "Yes.", "Morgan needs your help!"
    ));
    public void startQuest() {
        Rs2Walker.walkTo(3099, 3268, 0, 2);
        DialogueHandler.talkToNPCCutscene("morgan", dialogue, 5);
    }

    public void drHarlow() {
        Rs2Walker.walkTo(3220, 3399, 0, 3);
        DialogueHandler.talkToNPCCutscene("dr harlow", dialogue, 5);
        DialogueHandler.talkToNPCCutscene("dr harlow", dialogue, 5);
    }
    public void completeQuest() {
        Microbot.log("Starting vampyre slayer.");
        startQuest();
        drHarlow();
        Rs2Walker.walkTo(3077, 9773, 0, 2);
        Microbot.log("clicking coffin");
        Rs2GameObject.interact("coffin", "Open");
        do {
            Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
            Actor a = Rs2Player.getInteracting();
            if (a != null && a.isDead()) {
                System.out.println(a.getName() + " is dead");
                break;
            } else if (a != null && !a.isDead()) System.out.println(a.getName() + " is still alive");

        } while (true);
        Microbot.log("fight is over");
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        MiscellaneousUtilities.waitForQuestFinish();
        Microbot.log("Vampire slayer complete.");
    }
}
