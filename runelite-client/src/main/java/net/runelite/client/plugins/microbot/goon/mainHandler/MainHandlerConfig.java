package net.runelite.client.plugins.microbot.goon.mainHandler;

import net.runelite.client.config.*;

@ConfigGroup("MainHandlerConfig")
@ConfigInformation(
        "Get new accs ready to goon"
)
public interface MainHandlerConfig extends Config {
    /*@ConfigSection(
            name="Initial Test",
            description = "test",
            position = 1
    )
    String startingStateSection = "startingStateSection";*/
    @ConfigItem(
            keyName = "rewardCollectionThresholdWT",
            name = "Wt Reward Cart Collection Threshold",
            description = "How mayn rewards to store at the WT rewards cart before collecting",
            position = 1
            //section = startingStateSection
    )
    default int rewardCollectionThresholdWT() {return 1000;}
}
