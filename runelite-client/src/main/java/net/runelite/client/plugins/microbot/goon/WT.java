package net.runelite.client.plugins.microbot.goon;

import lombok.Setter;
import net.runelite.api.ChatLineBuffer;
import net.runelite.api.ChatMessageType;
import net.runelite.api.GameObject;
import net.runelite.api.MessageNode;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2ObjectModel;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.misc.Rs2UiHelper;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleep;

public class WT {
    GoonUtils goonUtils = new GoonUtils();
    @Setter
    public boolean resetActions = false;
    private final List<Integer> expectedAnims = List.of(
            879, 877, 875, 873, 871, 869, 867, 8303, 2846, 24, 2117, 7264, 8324, 8778, 10071, 733
    );
    private void resuppply() {
        Rs2Walker.walkTo(1639, 3944, 0);
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("knife", 1, false, false, false),
                new BankHandler.QuestItem("hammer", 1, false, false, false),
                new BankHandler.QuestItem("tinderbox", 1, false, false, false),
                new BankHandler.QuestItem("cake", 4, false, false, false)
        ), true, false);
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
        //The Wintertodt returns in: 0:53
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
        long lastAnimating = System.currentTimeMillis() - 10000;
        while (Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma root")) > 0) {
            int wintertodtEnergy = parseEnergyAndWarmth(Rs2Widget.findWidget("wintertodt's energy"));
            int brumaCount = Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma"));
            int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
            //exit early, game almost over
            if (brumaCount > wintertodtEnergy) return;

            if (playerWarmth < 35) {
                Rs2Inventory.interact(new String[]{"cake", "slice of cake", "2/3 cake"}, "eat");
                resetActions = true;
            }
            else if (Rs2Player.getAnimation() == 1248) lastAnimating = System.currentTimeMillis();
            else if (resetActions || System.currentTimeMillis() - lastAnimating > 2000) {
                Rs2Inventory.interact("knife", "use");
                Rs2Inventory.interact("bruma root", "use");
                resetActions = false;
                lastAnimating = System.currentTimeMillis();
            }
        }
    }

    private void loadBrazier() {
        System.out.println("fletching");
        fletch();
        System.out.println("done fletching. total kindling count: " + Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma kindling   ")));
        System.out.println("loading brazier.");
        long lastAnimating = System.currentTimeMillis() - 10000;
        while (Rs2Inventory.hasItem("bruma")) {
            GameObject litBrazier = Rs2GameObject.getGameObject(29314, new WorldPoint(1621, 3998, 0), true);
            GameObject unlitBrazier = Rs2GameObject.getGameObject(29312, new WorldPoint(1621, 3998, 0), true);
            GameObject brokenBrazier = Rs2GameObject.getGameObject(29313, new WorldPoint(1621, 3998, 0), true);
            int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
            if (playerWarmth < 35) {
                Rs2Inventory.interact(new String[]{"cake", "slice of cake", "2/3 cake"}, "eat");
                resetActions = true;
            }

            // can refine this if i want. just exits loading if im going to get hit by snow attack
            if (Rs2GameObject.getGameObject(26690, Rs2Player.getWorldLocation(), true) != null) {
                System.out.println("incoming falling snow - exiting");
                return;
            }
            else if (Rs2Player.getAnimation() == 832) {
                lastAnimating = System.currentTimeMillis();
                System.out.println("currently loading brazier");
            }
            else if (brokenBrazier != null) {
                Rs2GameObject.interact(brokenBrazier, "fix");
                System.out.println("fixing broken brazier");
            }
            else if (unlitBrazier != null) {
                Rs2GameObject.interact(unlitBrazier, "light");
                System.out.println("lighting extinguished brazier");
            }
            else if (litBrazier != null) {
                if (System.currentTimeMillis() - lastAnimating > 2000 || resetActions) {
                    System.out.println("too long without loading - click brazier again");
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
                System.out.println("here777 ");
                Rs2GameObject.interact(new WorldPoint(1636, 3942, 0), "big-search");
                sleep(1200);
            }
        }
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("teleport to varrock", 1,false, false,false)
        ), true, false);
        MiscellaneousUtilities.walkToGE();
        // sell all the loot hre
    }

    private int parseRewards(String regex) {
        System.out.println("invoking");
        final Map<Integer, ChatLineBuffer> lineBuffer = Microbot.getClient().getChatLineMap();
        if (lineBuffer != null) {
            System.out.println("1");
            ChatLineBuffer gameMessages = lineBuffer.get(ChatMessageType.GAMEMESSAGE.getType());
            if (gameMessages == null) return -1;
            System.out.println("2");
            final MessageNode[] lines = gameMessages.getLines().clone();
            for (final MessageNode line : lines) {
                if (line != null) {
                    System.out.println("line val: " + line.getValue());
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

    public AtomicLong run(AtomicLong start) {
        if (parseRewards("You're now owed (\\d+) rewards\\.$") > 5000) collectRewards();

        try {
            if (!Rs2Widget.hasWidget("Wintertodt's Energy")) {
                int timeToNextGame = parseTimeToNextGame();
                if (foodBites() < 6) resuppply();
                else if (!inArena()) Rs2Walker.walkTo(1629, 3982, 0);
                else if (timeToNextGame > 0) Rs2Walker.walkTo(1621, 3996, 0, 0);
                else if (timeToNextGame == 0) Rs2GameObject.interact(29312);

                start.set(goonUtils.breakHandler(start.get()));
            }

            else {
                int wintertodtEnergy = parseEnergyAndWarmth(Rs2Widget.findWidget("wintertodt's energy"));
                int playerWarmth = parseEnergyAndWarmth(Rs2Widget.findWidget("your warmth"));
                int brumaCount = Rs2Inventory.itemQuantity(item -> item.getName().toLowerCase().contains("bruma"));
                if (playerWarmth < 35) Rs2Inventory.interact(new String[]{"cake", "slice of cake", "2/3 cake"}, "eat");
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
}

//you're now owed xxx rewards
