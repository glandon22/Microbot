package net.runelite.client.plugins.microbot.goon.newaccbuilder;

import net.runelite.client.config.*;
import net.runelite.client.plugins.microbot.gabplugs.sandminer.GabulhasSandMinerInfo;

@ConfigGroup("NewAccBuilderConfig")
@ConfigInformation(
        "Get new accs ready to goon"
)
public interface NewAccBuilderConfig extends Config {
    @ConfigSection(
            name="Initial Test",
            description = "test",
            position = 1
    )
    String startingStateSection = "startingStateSection";
    @ConfigItem(
            keyName = "myTest",
            name = "Test config",
            description = "just testing",
            position = 1,
            section = startingStateSection
    )
    default int myTest() {return 1;}
}
