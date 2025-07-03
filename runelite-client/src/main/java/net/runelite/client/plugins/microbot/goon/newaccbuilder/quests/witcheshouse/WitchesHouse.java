package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witcheshouse;

import net.runelite.api.Actor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.player.Rs2PlayerModel;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class WitchesHouse {
    List<String> dialogue = new ArrayList<>(List.of("What's the matter?", "Ok, I'll see what I can do.", "Yes."));

    private void entranceToFountain() {
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2904) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        System.out.println("witch is on tile.");
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2915) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        Rs2Walker.walkTo(2916, 3460, 0 , 0);
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2915) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        Rs2Walker.walkTo(2933, 3465, 0 , 0);
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2930) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2922) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        Rs2Walker.walkTo(2910, 3469, 0 , 1);
    }

    private void fountainToShed() {
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2916) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        while (true) {
            Rs2NpcModel witch = Rs2Npc.getNpc("nora t. hagg");
            if (witch != null && witch.getWorldLocation().getX() == 2915) break;
            else if (witch != null) System.out.println("Witch is visible but not in position.");
        }
        Rs2Walker.walkTo(2933, 3466, 0 , 0);
    }

    private void killExperiments() {
        System.out.println("Starting to kill experiments.");
        while (true) {
            if (Rs2Inventory.hasItem("ball")) return;
            Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
            System.out.println("Debug - Cycling.");
            List<Rs2NpcModel> npcList = Rs2Npc.getAttackableNpcs(true)
                    .filter(npc -> {
                        String name = npc.getName();
                        return name != null && name.contains("experiment");
                    })
                    .collect(Collectors.toList());
            Rs2PlayerModel myPlayer = Rs2Player.getLocalPlayer();
            Actor myTarget = myPlayer.getInteracting();
            if (myTarget != null) System.out.println("I am currently in combat.");

            else if (!npcList.isEmpty()) {
                System.out.println("No longer in combat and an NPC is present to attack.");
                for (Rs2NpcModel npc : npcList) {
                    if (npc.getName() == null) System.out.println("found npc with null name");
                    if (npc.getName().contains("experiment")) {
                        System.out.println("found an npc to attack: " + npc.getName());
                        Rs2Npc.interact(npc, "attack");
                        break;
                    }
                }
            }

            else Rs2GroundItem.interact(2407, "take", 10);

            Rs2Player.drinkPrayerPotionAt(10);

        }
    }

    private void startQuest() {
        Rs2Walker.walkTo(2927, 3455, 0, 3);
        DialogueHandler.talkToNPC("boy", dialogue, 5);
        Rs2Walker.walkTo(2896, 3472, 0, 3);
        // click the plant pot
        Rs2GameObject.interact(2867);
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Walker.walkTo(2904, 3472, 0, 2);
        Rs2Walker.walkTo(2899, 9874, 0, 2);
        Rs2GameObject.interact("cupboard", "open");
        sleep(3000);
        Rs2GameObject.interact("cupboard", "search");
        Rs2Inventory.waitForInventoryChanges(5000);
        Rs2Walker.walkTo(2904, 3472, 0, 2);
        Rs2Walker.walkTo(2901, 3466, 0, 1);
        Rs2Inventory.drop("cheese");
        while (true) {
            Rs2NpcModel m = Rs2Npc.getNpc("mouse");
            if (m != null) {
                System.out.println("found a mouse");
                break;
            }
        }
        Rs2Inventory.use("magnet");
        Rs2Npc.interact("mouse", "use");
        sleepUntil(() -> Rs2Npc.getNpc("mouse") == null);
        System.out.println("disactivated the alarm");
        Rs2Walker.walkTo(2902, 3460, 0, 1);
        entranceToFountain();
        Rs2GameObject.interact("fountain", "check");
        Rs2Dialogue.sleepUntilHasContinue();
        Rs2Dialogue.clickContinue();
        Rs2Inventory.waitForInventoryChanges(5000);
        fountainToShed();
        Rs2Inventory.use(2411);
        Rs2GameObject.interact(2863, "use");
        sleepUntil(() -> Rs2Player.getWorldLocation().getX() >= 2934, 7000);
        Rs2GroundItem.interact(2407, "take", 10);
        killExperiments();
        Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, false);
        System.out.println("Finished killing experiments. Waiting to take ball");
        Rs2Walker.walkTo(2965, 3390, 0, 1);
        Rs2Walker.walkTo(2928, 3454, 0, 1);
        DialogueHandler.talkToNPC("boy", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish("Witch's House");
        System.out.println("Finished witches house.");
    }

    public void completeQuest() {
        startQuest();
    }
}
