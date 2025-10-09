package net.runelite.client.plugins.microbot.goon.plankmaker;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Goon + "Plank Maker",
        description = "make mahogany planks",
        tags = {"goon", "money", "main", "money"},
        enabledByDefault = false
)
@Slf4j
public class PlankMaker extends Plugin {
    @Inject
    PlankMakerScript plankMakerScript;

    @Override
    protected void startUp() throws AWTException {
        plankMakerScript.run();
    }
}
