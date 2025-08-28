package net.runelite.client.plugins.microbot.goon.mainHandler;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Goon + "Main Gooner Loop",
        description = "goon out and make bank",
        tags = {"goon", "money", "main", "money"},
        enabledByDefault = false
)
@Slf4j
public class MainHandler extends Plugin {
    @Inject
    MainHandlerScript mainHandlerScript;
    @Override
    protected void startUp() throws AWTException {
        mainHandlerScript.run();
    }

    protected void shutDown() { mainHandlerScript.shutdown(); }
}
