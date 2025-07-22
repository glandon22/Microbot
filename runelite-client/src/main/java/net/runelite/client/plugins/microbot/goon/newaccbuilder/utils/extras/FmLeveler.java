package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class FmLeveler {
    public String debug = "";
    public State state = State.BANKING;
    GameObject closestFire = null;
    private static List<Integer> FireIDs = Arrays.asList(26185, 49927);
    private boolean expectingXPDrop = false;
    public enum LogType {
        NORMAL_LOGS("Logs", ItemID.LOGS),
        OAK_LOGS("Oak logs", ItemID.OAK_LOGS),
        WILLOW_LOGS("Willow logs", ItemID.WILLOW_LOGS),
        TEAK_LOGS("Teak logs", ItemID.TEAK_LOGS),
        MAPLE_LOGS("Maple logs", ItemID.MAPLE_LOGS),
        MAHOGANY_LOGS("Mahogany logs", ItemID.MAHOGANY_LOGS),
        YEW_LOGS("Yew logs", ItemID.YEW_LOGS),
        MAGIC_LOGS("Magic logs", ItemID.MAGIC_LOGS),
        REDWOOD_LOGS("Redwood logs", ItemID.REDWOOD_LOGS);

        private final String itemName;
        private final int itemId;

        LogType(final String _itemName,
                final int _itemId) {
            itemName = _itemName;
            itemId = _itemId;
        }

        public String getLogName() {
            return itemName;
        }

        public int getLogID() {
            return itemId;
        }
    }
    enum State {
        FIREMAKING,
        BANKING,
        BANK_FOR_FIRE_SUPPLIES,
        BUILDING_FIRE
    }

    private LogType logType = null;
    private void determineState() {
        int fmLvl = Rs2Player.getRealSkillLevel(Skill.FIREMAKING);
        if (fmLvl < 15) {
            logType = LogType.NORMAL_LOGS;
        } else if (fmLvl < 30) {
            logType = LogType.OAK_LOGS;
        } else logType = LogType.WILLOW_LOGS;


        // Check each fire spot to see if any of them have a fire ID on them
        List<GameObject> fires = Rs2GameObject.getGameObjects();
        closestFire = fires.stream()
                .filter(fire -> fire.getId() == 26185 || fire.getId() == 49927)
                .min(Comparator.comparingInt(fire -> Rs2Player.getWorldLocation().distanceTo2D(fire.getWorldLocation())))
                .orElse(null);
        if (closestFire != null && Rs2Player.getWorldLocation().distanceTo2D(closestFire.getWorldLocation()) < 6) {
            if (FireIDs.contains(closestFire.getId())) {
                debug("Fire found");
                if (Rs2Inventory.hasItem(logType.getLogID())) {
                    debug("Adding logs to fire");
                    state = State.FIREMAKING;
                } else {
                    debug("Out of logs");
                    state = State.BANKING;
                }
                return;
            }
        }

        if (Rs2Inventory.hasItem("Tinderbox") && Rs2Inventory.hasItem(logType.getLogID())) {
            debug("Building fire");
            state = State.BUILDING_FIRE;
        } else {
            debug("Banking for firemaking supplies");
            state = State.BANK_FOR_FIRE_SUPPLIES;
        }
    }

    private void logicHandler() {
        determineState();
        if (state != State.FIREMAKING) {
            expectingXPDrop = false;
        }

        if (Rs2Dialogue.hasContinue()) {
            debug("Click to continue");
            Rs2Dialogue.clickContinue();
            return;
        }

        switch (state) {
            case FIREMAKING:
                if (Rs2Inventory.count(logType.getLogID()) == 0) {
                    debug("Out of logs in inventory");
                    return;
                }

                if (expectingXPDrop && Rs2Player.waitForXpDrop(Skill.FIREMAKING, 7000)) {
                    debug("Firemaking in progress");
                    sleep(256, 789);
                    return;
                }

                debug("Looking for fires to use");

                boolean interacted = false;
                if (closestFire != null) {
                    debug("Using object on fire");
                    Rs2Inventory.useItemOnObject(logType.getLogID(), closestFire.getId());
                    interacted = true;
                }

                if (interacted) {
                    sleepUntil(() -> (!Rs2Player.isMoving() && Rs2Widget.findWidget("How many would you like to burn?", null, false) != null), 5000);
                    sleep(180, 540);
                    Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);
                    expectingXPDrop = true;
                    sleep(2220, 5511);
                }
                break;

            case BANKING:
                debug("Banking");
                //when i level up and begin doing a new log, sometimes the old logs are still in inv. dump them
                boolean dumpInv = Rs2Inventory.hasItem("log");
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem(logType.itemName, 27, false, false ,false)
                ), dumpInv,false);
                sleepUntil(() -> Rs2Inventory.hasItem(logType.itemId), 2000);
                break;

            case BANK_FOR_FIRE_SUPPLIES:
                debug("Banking for fire supplies");
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("tinderbox", 1, false, false ,false),
                        new BankHandler.QuestItem(logType.itemName, 27, false, false ,false)
                ), false,false);
                sleepUntil(() -> Rs2Inventory.hasItem(logType.itemId), 2000);
                break;

            case BUILDING_FIRE:
                WorldPoint fireSpot = new WorldPoint(3161, 3489, 0);
                debug("Building fire");

                if (Rs2Player.isInteracting()) {
                    debug("Interacting");
                    return;
                }

                if (Rs2Player.distanceTo(fireSpot) > 1) {
                    debug("Walking to desired fire location");
                    Rs2Walker.walkTo(fireSpot, 0);
                    sleep(180, 540);
                    return;
                }

                Rs2Inventory.combine("Tinderbox", logType.getLogName());
                Rs2Player.waitForXpDrop(Skill.FIREMAKING, 20000);
                break;

            default:
                break;
        }
    }

    private void debug(String msg) {
        debug = msg;
        System.out.println(msg);
    }

    public void levelUp() {
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("tinderbox", 1, false, false ,false)
        ), true, true);
        //this doesnt work if someone else has a campfire out
        while (Rs2Player.getRealSkillLevel(Skill.FIREMAKING) < 50) {
            logicHandler();
        }
    }
}
