package net.runelite.client.plugins.microbot.goon;

import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GoonUtils {
    public long breakHandler(long start) {
        // break every 55 mins
        if (System.currentTimeMillis() - start > 1000 * 60 * 55) {
            Rs2Player.logout();
            long breakStart = System.currentTimeMillis();
            sleepUntil(() -> System.currentTimeMillis() - breakStart > 9000, 100000);
            new Login();
            sleepUntil(() -> Rs2Widget.hasWidget("click here to play"), 100000);
            Rs2Widget.clickWidget("click here to play");
            return System.currentTimeMillis();
        }

        else {
            System.out.printf("play percent: %.2f%%%n", ((double)(System.currentTimeMillis() - start) / (1000 * 60 * 55) * 100));
            return start;
        }
    }
}
