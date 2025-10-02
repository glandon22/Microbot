package net.runelite.client.plugins.microbot.goon;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.questhelper.helpers.quests.legendsquest.LegendsQuest;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.logging.Level;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GoonUtils {
    public long breakHandler(long start) {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        DayOfWeek day = now.getDayOfWeek();

        if (System.currentTimeMillis() - start > 1000 * 60 * 55) {
            Rs2Player.logout();
            Microbot.log("Logged out for regular break");
            // Check if it's Monday through Thursday and between 9 PM and 5 AM
            boolean isLogoutPeriod = (day.getValue() >= DayOfWeek.MONDAY.getValue() &&
                    day.getValue() <= DayOfWeek.THURSDAY.getValue()) &&
                    (hour >= 21 || hour < 5);

            if (isLogoutPeriod) {
                Microbot.log("Taking extended over night break.");
                // Calculate milliseconds until 5 AM
                LocalDateTime nextFiveAm = now.withHour(5).withMinute(0).withSecond(0).withNano(0);
                if (hour >= 21) {
                    // If it's after 9 PM, set to 5 AM the next day
                    nextFiveAm = nextFiveAm.plusDays(1);
                }
                long millisUntilFiveAm = java.time.Duration.between(now, nextFiveAm).toMillis();

                // Sleep until 5 AM
                sleepUntil(() -> false, millisUntilFiveAm);
            }
            // Time out after 8 mins
            sleepUntil(() -> false, 1000 * 60 * 8);
            Microbot.log("Logging in");
            new Login();
            sleepUntil(() -> Rs2Widget.hasWidget("click here to play") && Microbot.isLoggedIn(), 100000);
            Microbot.log("Found click to play button");
            Rs2Widget.clickWidget("click here to play");
            Microbot.log("Done handling break");
            return System.currentTimeMillis();
        } else {
            Microbot.log("Play percent: %.2f%%%n", ((double)(System.currentTimeMillis() - start) / (1000 * 60 * 55) * 100));
            return start;
        }
    }

    public long breakHandler() {
        return breakHandler(System.currentTimeMillis() - 1000 * 60 * 55 * 2);
    }
}
