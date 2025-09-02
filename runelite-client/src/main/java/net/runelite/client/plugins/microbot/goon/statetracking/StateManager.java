package net.runelite.client.plugins.microbot.goon.statetracking;

import com.google.gson.Gson;

import java.io.*;

public class StateManager {
    private static final String DATA_DIR = "./bot_data/";
    private static final Gson GSON = new Gson();

    public static AccountState loadState(String accountId) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        System.out.println("input: " + DATA_DIR + accountId + ".json");
        File file = new File(DATA_DIR + accountId + ".json");
        if (!file.exists()) {
            System.out.println("creating new file for " + accountId);
            return new AccountState(); // New account, defaults to main
        }

        try (FileReader reader = new FileReader(file)) {
            AccountState state = GSON.fromJson(reader, AccountState.class);
            System.out.println("successfully read in state");
            return state;
        } catch (IOException e) {
            System.out.println("creating a new state file for " + accountId + " after error");
            // Log error, return default
            return new AccountState();
        }
    }

    public static void saveState(String accountId, AccountState state) {
        System.out.println("sacing state data to " + DATA_DIR + accountId + ".json");
        state.updateActivityRunTime();
        File file = new File(DATA_DIR + accountId + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(state, writer);
            state.resetAccumulatedTime();
            state.forceSave = false;
        } catch (IOException e) {
            // Log error
        }
    }
}
