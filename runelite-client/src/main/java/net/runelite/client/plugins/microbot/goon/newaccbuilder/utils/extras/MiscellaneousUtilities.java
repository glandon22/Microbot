package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras;

import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class MiscellaneousUtilities {
    static public void setSpell(String spell) {
        Rs2Widget.clickWidget("combat options");
        sleepUntil(() -> Rs2Widget.hasWidget("choose spell"));
        Rs2Widget.clickWidget("choose spell");
        sleepUntil(() -> Rs2Widget.hasWidget(spell));
        Rs2Widget.clickWidget(spell);
    }

    static public boolean waitForQuestFinish(String quest) {
        boolean res = sleepUntil(() -> Rs2Widget.hasWidget(quest), 90000);
        Rs2Widget.clickWidget(153, 16);
        return res;
    }
}
