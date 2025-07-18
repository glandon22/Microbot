package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

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
}
