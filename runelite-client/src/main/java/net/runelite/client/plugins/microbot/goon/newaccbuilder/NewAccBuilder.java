package net.runelite.client.plugins.microbot.goon.newaccbuilder;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Goon + "New Acc Builder",
        description = "",
        tags = {"goon", "new acc", "quester", "prep"},
        enabledByDefault = false
)
@Slf4j
public class NewAccBuilder extends Plugin {
    @Inject
    NewAccBuilderConfig newAccBuilderConfig;
    @Provides
    NewAccBuilderConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(NewAccBuilderConfig.class);
    }
    @Inject
    NewAccBuilderScript newAccBuilderScript;
    @Override
    protected void startUp() throws AWTException {
        newAccBuilderScript.run(newAccBuilderConfig);
    }

    protected void shutDown() {
        newAccBuilderScript.shutdown();
    }
}
