package net.runelite.client.plugins.microbot.goon.wintertodt;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.microbot.Microbot;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Goon + "GoonerTodt",
        description = "goon out at the wintertodt",
        tags = {"goon", "fm", "wt", "money"},
        enabledByDefault = false
)
@Slf4j
public class Wintertodt extends Plugin {
    @Inject
    WintertodtScript wintertodtScript;

    @Override
    protected void startUp() throws AWTException {
        wintertodtScript.run();
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
    {
        Actor actor = hitsplatApplied.getActor();

        if (actor != Microbot.getClient().getLocalPlayer()) {
            return;
        }

        wintertodtScript.setResetActions(true);
    }

    protected void shutDown() {
        wintertodtScript.shutdown();
    }
}
