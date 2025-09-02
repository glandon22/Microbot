package net.runelite.client.plugins.microbot.goon.mainHandler;

import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.goon.WT;
import net.runelite.client.plugins.microbot.goon.accounttrainer.Farming;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.goon.quests.gardenOfDeath;
import net.runelite.client.plugins.microbot.goon.statetracking.AccountState;
import net.runelite.client.plugins.microbot.goon.statetracking.StateManager;
import net.runelite.client.plugins.microbot.questhelper.helpers.quests.gardenoftranquility.GardenOfTranquillity;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MainHandlerScript extends Script {
    /**
     *
     * use this as my main bot script
     * this will determine which activity i do
     * how do i persist state, i.e. how long i have been botting wintertodt so i know when to switch things
     *
     * for multi acc logging, it seems like the file here
     * JX_CHARACTER_ID=355199433
     * JX_SESSION_ID=2HPNDxiUj2JfBGenmXf85t
     * JX_REFRESH_TOKEN=
     * JX_DISPLAY_NAME=rimmer1488
     * JX_ACCESS_TOKEN=
     * within the same jagex account the only thing you need to change is the JX_DISPLAY_NAME. JX_SESSION_ID will be
     * the same for each acc, as long as its under the same jagex account
     */
    WT wt = new WT();
    public boolean run() {
        AccountState state = StateManager.loadState(Rs2Player.getLocalPlayer().getName());
        AtomicLong lastLoopTime = new AtomicLong(System.currentTimeMillis());
        AtomicLong start = new AtomicLong(System.currentTimeMillis());
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("starting loop: " + state.nextActivity + " " + state.currentActivity);
            long now = System.currentTimeMillis();
            long delta = now - lastLoopTime.get();
            state.updateAccumulatedTime(delta); // Only add if actively doing the activity

            try {
                state.switchActivityV2();
            } catch (Exception e) {
                System.out.println("exception received:  " + e.toString());
                System.out.println(e.getMessage());
            }

            if (Objects.equals(state.currentActivity, "wintertodt")) {
                if (state.nextActivity != null) {
                    wt.activityTransition();
                    state.updateCurrentActivity();
                    System.out.println("state + " + state.nextActivity);
                }
                else wt.run(start);
            }

            else if (Objects.equals(state.currentActivity, "baggedPlants")) {
                if (state.nextActivity != null) {
                    MiscellaneousUtilities.walkToGE();
                    state.updateCurrentActivity();
                }
                else {
                    Farming.baggedPlants();
                    state.nextActivity = "gardenOfDeath";
                }
            }
            else if (Objects.equals(state.currentActivity, "gardenOfDeath")) {
                if (state.nextActivity != null) {
                    MiscellaneousUtilities.walkToGE();
                    state.updateCurrentActivity();
                }
                else {
                    BankHandler.withdrawQuestItems(List.of(
                            new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
                    ), true, true);
                    gardenOfDeath.completeQuest();
                    state.nextActivity = "titheFarm";
                }
            }
            else if (Objects.equals(state.currentActivity, "titheFarm")) {
                System.out.println("not implemented - tithe");
            }



            lastLoopTime.set(now);

            // Save periodically (e.g., every 5-10 mins) to persist
            if (state.getTotalElapsedMillis() > state.fileSaveInterval || state.forceSave) {
                System.out.println(state.getTotalElapsedMillis() + " " +  state.fileSaveInterval);
                StateManager.saveState(Rs2Player.getLocalPlayer().getName(), state);
                System.out.println("saving start for " + Rs2Player.getLocalPlayer().getName());
            }
        }, 0, 600, TimeUnit.MILLISECONDS);
        return false;
    }
}
