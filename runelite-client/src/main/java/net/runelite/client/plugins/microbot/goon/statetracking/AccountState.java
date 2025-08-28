package net.runelite.client.plugins.microbot.goon.statetracking;

import net.runelite.api.Skill;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

import java.util.List;

public class AccountState {
    public boolean forceSave = false;
    public long fileSaveInterval = 60_000L;
    public long mainActivityMaxConsecutiveTime = 86_400_000L;
    public long breakActivityMaxConsecutiveTime = 14_400_000L;
    public String currentActivity = null;
    private long activityStartTime = System.currentTimeMillis();
    private long accumulatedTimeMillis = 0;
    public long lastUpdateTime = System.currentTimeMillis();

    public String nextActivity = null;

    private long accumulatedActivityRunTime = 0;
    private final List<String> mainActivities = List.of(
            "wintertodt"
    );

    // Getters and setters (or make fields public for simplicity)

    public void updateCurrentActivity() {
        accumulatedActivityRunTime = 0;
        currentActivity = nextActivity;
        nextActivity = null;
    }
    public void updateAccumulatedTime(long deltaMillis) {
        accumulatedTimeMillis += deltaMillis;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void resetAccumulatedTime() {
        accumulatedTimeMillis = 0;
    }

    public void updateActivityRunTime() {
        accumulatedActivityRunTime += accumulatedTimeMillis;
    }

    public long getTotalElapsedMillis() {
        return accumulatedTimeMillis + (System.currentTimeMillis() - lastUpdateTime);
        // Note: This assumes you're calling it during runtime; adjust if needed.
    }

    public void resetTotalElapsedMillis() {
        accumulatedTimeMillis = 0;
        // Note: This assumes you're calling it during runtime; adjust if needed.
    }

    public boolean shouldSwitchToBreak() {
        return mainActivities.contains(currentActivity)
                && getTotalElapsedMillis() >= mainActivityMaxConsecutiveTime;
    }

    public boolean shouldSwitchToMain() {
        return currentActivity.equals("BREAK") && getTotalElapsedMillis() >= 7_200_000L; // 2 hours
    }

    public void switchActivity(String newActivity) {
        currentActivity = newActivity;
        activityStartTime = System.currentTimeMillis();
        accumulatedTimeMillis = 0;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void switchActivityV2() {
        // take a break from main money makers for anti ban
        if (mainActivities.contains(currentActivity)) {
            if (accumulatedActivityRunTime < mainActivityMaxConsecutiveTime) return;
            // level farming up first to improve wintertodt loots
            else if (Rs2Player.getRealSkillLevel(Skill.FARMING) < 20) nextActivity = "baggedPlants";
            else if (Rs2Player.getRealSkillLevel(Skill.FARMING) < 34) nextActivity = "gardenOfDeath";
            else if (Rs2Player.getRealSkillLevel(Skill.FARMING) < 66) nextActivity = "titheFarm";
            accumulatedActivityRunTime = 0;
            forceSave = true;
        }

        else if (accumulatedActivityRunTime > breakActivityMaxConsecutiveTime || currentActivity == null){
            nextActivity = mainActivities.get(0);
            accumulatedActivityRunTime = 0;
            forceSave = true;
        }
    }
}
