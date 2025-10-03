package net.runelite.client.plugins.microbot.goon;

import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.mainHandler.LootItemNames;
import net.runelite.client.plugins.microbot.goon.mainHandler.MainHandlerConfig;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleep;

public class WT {
    int MIN_WARMTH = 45;
    GoonUtils goonUtils = new GoonUtils();
    @Setter
    public boolean resetActions = false;
    private final List<Integer> expectedAnims = List.of(
            879, 877, 875, 873, 871, 869, 867, 8303, 2846, 24, 2117, 7264, 8324, 8778, 10071, 733
    );

    private static void returnToWintertodt() {
        MiscellaneousUtilities.walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("knife", 1, false, false, false),
                new BankHandler.QuestItem("hammer", 1, false, false, false),
                new BankHandler.QuestItem("tinderbox", 1, false, false, false),
                new BankHandler.QuestItem("polar camo top", 1, false, false, true),
                new BankHandler.QuestItem("polar camo legs", 1, false, false, true),
                new BankHandler.QuestItem("fire tiara", 1, false, false, true),
                new BankHandler.QuestItem("grey gloves", 1, false, false, true),
                new BankHandler.QuestItem("games necklace", 1, false, false, true),
                new BankHandler.QuestItem("adamant axe", 1, false, false, true)
        ), true, true);
        Rs2Walker.walkTo(1634, 3938, 0);
        doUntil(
                () -> Rs2Player.getWorld() == 307,
                () -> MiscellaneousUtilities.hopWorlds(307),
                10000,
                300000
        );
        sleep(10000);
    }

    private int foodBites() {
        return Rs2Inventory.itemQuantity(1891) * 3 +
                Rs2Inventory.itemQuantity(1893) * 2 +
                Rs2Inventory.itemQuantity(1895);
    }

    private boolean inArena() {
        WorldPoint currentLocation = Rs2Player.getWorldLocation();
        return currentLocation.getY() >= 3968 && currentLocation.getY() <= 4026
                && currentLocation.getX() >= 1611 && currentLocation.getX() <= 1648;
    }
    private int parseTimeToNextGame() {
        Widget information = Rs2Widget.findWidget("The Wintertodt returns in:", false);
        if (information == null) return -1;
        String fullString = information.getText();
        // Extract the time portion after the colon and space
        String timePart = fullString.substring(fullString.indexOf(":") + 2).trim();

        // Split the time string into minutes and seconds
        String[] parts = timePart.split(":");
        if (parts.length != 2) {
            return -1;
        }

        // Parse minutes and seconds
        int minutes = Integer.parseInt(parts[0].trim());
        int seconds = Integer.parseInt(parts[1].trim());

        // Calculate total seconds
        return minutes * 60 + seconds;
    }

    private int parseEnergyAndWarmth(Widget input) {
        if (input == null) return -1;
        String information = input.getText();
        return Integer.parseInt(information.substring(information.indexOf(":") + 2).replace("%", "").trim());
    }

    private void fletch() {
        Microbot.log("Fletching bruma kindling.");
        long lastAnimating = System.currentTimeMillis() - 10000;
        while (Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma root")) > 0) {
            int wintertodtEnergy = parseEnergyAndWarmth(Rs2Widget.findWidget("wintertodt's energy"));
            int brumaCount = Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma"));
            int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
            //exit early, game almost over
            if (brumaCount > wintertodtEnergy) return;

            if (playerWarmth < MIN_WARMTH) {
                increasePlayerWarmth();
                resetActions = true;
            }
            else if (Rs2Player.getAnimation() == 1248) lastAnimating = System.currentTimeMillis();
            else if (resetActions || System.currentTimeMillis() - lastAnimating > 2000) {
                Microbot.log("Timed out fletching. Click knife and logs again.");
                Rs2Inventory.interact("knife", "use");
                Rs2Inventory.interact("bruma root", "use");
                resetActions = false;
                lastAnimating = System.currentTimeMillis();
            }
        }
    }

    private void loadBrazier() {
        fletch();
        Microbot.log("Done fletching, loading brazier.");
        long lastAnimating = System.currentTimeMillis() - 10000;
        while (Rs2Inventory.hasItem("bruma")) {
            GameObject litBrazier = Rs2GameObject.getGameObject(29314, new WorldPoint(1621, 3998, 0), true);
            GameObject unlitBrazier = Rs2GameObject.getGameObject(29312, new WorldPoint(1621, 3998, 0), true);
            GameObject brokenBrazier = Rs2GameObject.getGameObject(29313, new WorldPoint(1621, 3998, 0), true);
            int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
            if (playerWarmth < MIN_WARMTH) {
                increasePlayerWarmth();
                resetActions = true;
            }

            // can refine this if i want. just exits loading if im going to get hit by snow attack
            if (Rs2GameObject.getGameObject(26690, Rs2Player.getWorldLocation(), true) != null) {
                Microbot.log("incoming falling snow - exiting");
                return;
            }
            else if (Rs2Player.getAnimation() == 832) {
                lastAnimating = System.currentTimeMillis();
                Microbot.log("currently loading brazier");
            }
            else if (brokenBrazier != null) {
                Rs2GameObject.interact(brokenBrazier, "fix");
                Microbot.log("fixing broken brazier");
            }
            else if (unlitBrazier != null) {
                Rs2GameObject.interact(unlitBrazier, "light");
                Microbot.log("lighting extinguished brazier");
            }
            else if (litBrazier != null) {
                if (System.currentTimeMillis() - lastAnimating > 2000 || resetActions) {
                    Microbot.log("too long without loading - click brazier again");
                    Rs2GameObject.interact(litBrazier, "feed");
                    lastAnimating = System.currentTimeMillis();
                    resetActions = false;
                }
            }

            else if (!Microbot.isLoggedIn()) return;
        }
    }

    private void collectRewards() {
        Rs2Walker.walkTo(1634, 3942, 0);
        while (
                parseRewards("You are owed ([\\d,]+) more rewards") == -1
                        || parseRewards("You are owed ([\\d,]+) more rewards") > 100
        ) {
            if (Rs2Player.getLocalPlayer().getAnimation() == 11758) continue;
            else if (Rs2Inventory.emptySlotCount() < 2) {
                BankHandler.withdrawQuestItems(List.of(), true, false);
            }
            else {
                Rs2GameObject.interact(new WorldPoint(1636, 3942, 0), "big-search");
                sleep(1200);
            }
        }
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("varrock teleport", 1,false, false,false)
        ), true, false);
        MiscellaneousUtilities.walkToGE();
        sellRewards();
    }

    private int parseRewards(String regex) {
        final Map<Integer, ChatLineBuffer> lineBuffer = Microbot.getClient().getChatLineMap();
        if (lineBuffer != null) {
            ChatLineBuffer gameMessages = lineBuffer.get(ChatMessageType.GAMEMESSAGE.getType());
            if (gameMessages == null) return -1;
            final MessageNode[] lines = gameMessages.getLines().clone();
            for (final MessageNode line : lines) {
                if (line != null) {
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(line.getValue());
                    if (matcher.find()) {
                        String numberStr = matcher.group(1); // Extract the captured number
                        // Remove commas if present
                        numberStr = numberStr.replace(",", "");
                        return Integer.parseInt(numberStr);
                    }
                }
            }

        }
        return -1;
    }

    public void activityTransition() {
        Rs2Walker.walkTo(1630, 3968, 0);
        doUntil(
                () -> Rs2Player.getLocalPlayer().getWorldLocation().getY() <= 3963,
                () -> {
                    if (Rs2Dialogue.isInDialogue()) DialogueHandler.handleConversation(List.of("Leave and lose all progress."), 1);
                    else Rs2GameObject.interact(29322, "enter");
                },
                5000,
                100000
        );
        Rs2Walker.walkTo(1639, 3944, 0);
    }

    public static boolean sellRewards() {
        Microbot.log("Sellings rewards from wintertodt.");
        final int INVENTORY_LIMIT = 28;
        int startIndex = 0;

        while (startIndex < LootItemNames.LOOT_ITEM_NAMES.size()) {
            // Create a batch of up to 28 items
            List<BankHandler.QuestItem> itemsWithdraw = new ArrayList<>();
            List<ItemBuyer.ItemToSell> itemsSell = new ArrayList<>();
            int endIndex = Math.min(startIndex + INVENTORY_LIMIT, LootItemNames.LOOT_ITEM_NAMES.size());

            // Build the batch
            for (int i = startIndex; i < endIndex; i++) {
                String name = LootItemNames.LOOT_ITEM_NAMES.get(i);
                Microbot.log("Adding " + name + " to current sales batch.");
                itemsWithdraw.add(new BankHandler.QuestItem(name, 1, true, true, false));
                itemsSell.add(new ItemBuyer.ItemToSell(name, 1, -1, -50, true, true, true, true));
            }

            // Withdraw and sell the batch
            Microbot.log("Withdrawing all batch items to sell.");
            BankHandler.withdrawQuestItems(itemsWithdraw, true, false);
            Microbot.log("Starting to sell batch items.");
            ItemBuyer.sellItems(itemsSell);
            Microbot.log("Done selling items.");

            // Move to the next batch
            startIndex += INVENTORY_LIMIT;
        }
        returnToWintertodt();
        return true; // Return true to indicate successful processing of all items
    }

    private void makeRejuvPots() {
        doUntil(
                () -> Rs2Inventory.hasItem("rejuvenation potion (4)"),
                () -> {
                    Microbot.log("Walking to bruma root to make rejuv pots.");
                    Rs2Walker.walkTo(1611, 4007, 0);
                    Microbot.log("Picking bruma herbs.");
                    boolean pickedHerbs = doUntil(
                            () -> Rs2Inventory.itemQuantity("bruma herb") >= 1,
                            () -> {
                                if (Rs2Player.getLocalPlayer().getAnimation() != 2282) Rs2GameObject.interact(29315, "pick");
                            },
                            1200,
                            100000
                    );
                    if (!pickedHerbs) {
                        Microbot.log("Failed to pick bruma herbs.", Level.WARN);
                        return;
                    }
                    Rs2Walker.walkTo(1627, 3988, 0);
                    boolean gotUnfs = doUntil(
                            () -> Rs2Inventory.itemQuantity("rejuvenation potion (unf)") >= 1,
                            () -> Rs2GameObject.interact(29320),
                            1200,
                            100000
                    );
                    if (!gotUnfs) {
                        Microbot.log("Failed to get unfinished pots.", Level.WARN);
                        return;
                    }
                    boolean usedSupps = doUntil(
                            () -> !Rs2Inventory.hasItem("rejuvenation potion (unf)") || !Rs2Inventory.hasItem("bruma herb"),
                            () -> {
                                Rs2Inventory.interact("rejuvenation potion (unf)", "use");
                                Rs2Inventory.interact("bruma herb", "use");
                            },
                            1000,
                            50000
                    );
                    if (!usedSupps) {
                        Microbot.log("Failed to use all pot supplies.", Level.WARN);
                        sleep(600);
                    }
                },
                1000,
                500000
        );
    }

    private void increasePlayerWarmth() {
        Microbot.log("Warmth low - drinking pot.");
        for (String pot : List.of(
                "rejuvenation potion (1)", "rejuvenation potion (2)", "rejuvenation potion (3)", "rejuvenation potion (4)"
        )) {
            if (Rs2Inventory.hasItem(pot)) {
                Rs2Inventory.interact(pot, "drink");
                sleep(200);
            }
        }

        Microbot.log("Warmth is low and out of pots - making more.");
        makeRejuvPots();
    }

    public AtomicLong run(AtomicLong start, MainHandlerConfig config) {
        Microbot.log("Current rewards threshold: " + config.rewardCollectionThresholdWT());
        if (parseRewards("You're now owed (\\d+) rewards\\.$") > config.rewardCollectionThresholdWT()) collectRewards();

        try {
            if (!Rs2Widget.hasWidget("Wintertodt's Energy")) {
                int timeToNextGame = parseTimeToNextGame();
                if (!Rs2Inventory.hasItem("rejuvenation potion (4)")) makeRejuvPots();
                else if (!inArena()) Rs2Walker.walkTo(1629, 3982, 0);
                else if (timeToNextGame > 0) Rs2Walker.walkTo(1621, 3996, 0, 0);
                else if (timeToNextGame == 0) Rs2GameObject.interact(29312);

                start.set(goonUtils.breakHandler(start.get()));
            }

            else {
                int wintertodtEnergy = parseEnergyAndWarmth(Rs2Widget.findWidget("wintertodt's energy"));
                int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
                int brumaCount = Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma"));
                if (playerWarmth < MIN_WARMTH) increasePlayerWarmth();
                else if (Rs2Inventory.isFull() || brumaCount * 2 >= wintertodtEnergy) loadBrazier();
                else if (!expectedAnims.contains(Rs2Player.getAnimation())){
                    Rs2Walker.walkTo(1622, 3988, 0, 0);
                    Rs2GameObject.interact(29311);
                }
            }

            return start;

        } catch (Exception e) {
            System.err.println("Error in wintertodt task: " + e.getMessage());
            e.printStackTrace();
            return start;
        }
    }
}
