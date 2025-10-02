package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import net.runelite.api.Actor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.MicrobotConfig;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;

public class CombatHandler {
    public static void killMonsterWithPrayer(String monsterName, Rs2PrayerEnum prayer, int minPrayerPoints, int fightTimeout) {
        long lastSeenDemon = System.currentTimeMillis();
        System.out.println("starting");
        while (System.currentTimeMillis() - lastSeenDemon < fightTimeout) {
            Rs2Prayer.toggle(prayer, true);
            Rs2NpcModel monster = Rs2Npc.getNpc(monsterName);
            if (monster != null) lastSeenDemon = System.currentTimeMillis();

            Rs2Player.drinkPrayerPotionAt(minPrayerPoints);

            if (Rs2Player.getInteracting() != null) System.out.println("Currently fighting.");
            else if (monster != null) {
                Rs2Npc.interact(monster, "attack");
            }
        }
    }

    public static boolean killMonsterWithPrayerV2(String monsterName, Rs2PrayerEnum prayer, int minPrayerPoints, int fightTimeout) {
        long start = System.currentTimeMillis();
        Rs2NpcModel targetMonster = null;
        Microbot.log("Starting fight with " + monsterName);
        while (true) {
            if (targetMonster != null && targetMonster.isDead()) {
                Microbot.log("Successfully killed " + monsterName);
                return true;
            }

            else if (System.currentTimeMillis() - start > fightTimeout) {
                Microbot.log("Failed to kill " + monsterName + " in " + fightTimeout + " ms. Exiting unsuccessfully.");
                return false;
            }
            Rs2Prayer.toggle(prayer, true);
            Rs2Player.drinkPrayerPotionAt(minPrayerPoints);

            if (targetMonster == null && Rs2Player.getLocalPlayer().getInteracting() == null) {
                Microbot.log("Do not have a target and not interacting - searching for " + monsterName);
                Rs2NpcModel potentialTarget = Rs2Npc.getNpc(monsterName);
                if (potentialTarget != null) {
                    Microbot.log("Found " + monsterName + ". Setting target.");
                    targetMonster = potentialTarget;
                }
            }

            else if (targetMonster != null && Rs2Player.getLocalPlayer().getInteracting() == null) {
                Microbot.log("Found target " + monsterName + " but not in combat. Attacking.");
                Rs2Npc.interact(targetMonster);
            }

            else if (Rs2Player.getLocalPlayer().getInteracting() != null) {
                Microbot.log("Currently in combat with " + monsterName + ". Health: " + targetMonster.getHealthPercentage());
            }
        }
    }
}
