package net.runelite.client.plugins.microbot.goon;

import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GoonUtils {
    public long breakHandler(long start) {
        // break every 55 mins
        if (System.currentTimeMillis() - start > 1000 * 60 * 55) {
            Rs2Player.logout();
            System.out.println("logged out");
            // time out after 8 mins
            sleepUntil(() -> false, 1000 * 60 * 8);
            System.out.println("logging in");
            new Login();
            sleepUntil(() -> Rs2Widget.hasWidget("click here to play") && Microbot.isLoggedIn(), 100000);
            System.out.println("found ctp button");
            Rs2Widget.clickWidget("click here to play");
            System.out.println("done handling break");
            return System.currentTimeMillis();
        }

        else {
            System.out.printf("play percent: %.2f%%%n", ((double)(System.currentTimeMillis() - start) / (1000 * 60 * 55) * 100));
            return start;
        }
    }
}
