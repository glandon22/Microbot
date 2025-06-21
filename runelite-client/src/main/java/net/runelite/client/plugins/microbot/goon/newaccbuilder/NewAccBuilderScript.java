package net.runelite.client.plugins.microbot.goon.newaccbuilder;

import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.cooksassistant.CooksAssistant;

import java.util.concurrent.TimeUnit;

public class NewAccBuilderScript extends Script {
    CooksAssistant cooksAssistant = new CooksAssistant();
    public boolean run(NewAccBuilderConfig config) {
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                cooksAssistant.completeQuest();
                System.out.println(cooksAssistant);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        },0, 600, TimeUnit.MILLISECONDS);
        return false;
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
