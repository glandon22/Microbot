package net.runelite.client.plugins.microbot.goon.plankmaker;

import lombok.Value;
import net.runelite.api.World;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.MicrobotConfig;
import net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.misc.Rs2UiHelper;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.tile.Rs2Tile;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.slf4j.event.Level;

import java.util.*;

import static java.util.Map.entry;
import static net.runelite.client.plugins.microbot.util.Global.*;

public class GOTR {

    @Value
    private class AltarInformation {
        String name;
        int runeIconWidgetID;
        int GOTRPillarID;
        int priority;
        int altarID;
        int altarExitId;
        WorldPoint altarExitTile;
        // where to run to upon altar entry to actually see the altar to craft runes, cosmic and fire altars
        // are so large need this and exit tile
        WorldPoint altarViewTile;
    }

    // cata
    // nat 4361
    // law 4362
    // death 4363
    // blood 4364

    final Map<Integer, AltarInformation> altarInformations = Map.ofEntries(
            entry(4353, new AltarInformation(
                    "air", 4353, 43701, 4, 34760, 34748, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4354, new AltarInformation(
                    "mind", 4354, 43705, 4, 34761, 34749, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4355, new AltarInformation(
                    "water", 4355, 43702, 3, 34762, 34750, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4356, new AltarInformation(
                    "earth", 4356, 43703, 2, 34763, 34751, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4357, new AltarInformation(
                    "fire", 4357, 43704, 1, 34764, 34752, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4358, new AltarInformation(
                    "body", 4358, 43709, 4, 34765, 34753, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4359, new AltarInformation(
                    "cosmic", 4359, 43710, 3, 34766, 34754, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4360, new AltarInformation(
                    "chaos", 4360, 43706, 3, 34769, 34757, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4361, new AltarInformation(
                    "nature", 4361, 43711, 2, 34768, 34756, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4362, new AltarInformation(
                    "law", 4362, 43712, 2, 34767, 34755, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            )),
            entry(4363, new AltarInformation(
                    "death", 4363, 43707, 1, 34770, 34758, new WorldPoint(1, 1, 1), new WorldPoint(1, 1, 1)
            ))/*,
            entry(4364, new AltarInformation(
                    "blood", 4364, 43708, 1, -1, 1, new WorldPoint(1, 1, 1)
            ))*/
    );

    private boolean enterGameArena() {
        if (Rs2Player.getLocalPlayer().getWorldLocation().getY() < 9484) {
            Microbot.log("Outside of GOTR arena - entering.");
            Rs2Walker.walkTo(3615, 9482, 0, 2);
            Rs2GameObject.interact(43700, "quick-pass");
            sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 9484);
        }

        return Rs2Player.getLocalPlayer().getWorldLocation().getY() >= 9484;
    }

    private boolean gameActive() {
        return !Objects.equals(Rs2Widget.getWidget(746, 5).getText(), "");
    }

    private boolean grabCells() {
        return doUntil(
                () -> Rs2Inventory.itemQuantity("uncharged cell") == 10 || (Rs2Inventory.hasItem("uncharged cell") && gameActive()),
                () -> {
                    Microbot.log("Grabbing uncharged cells.");
                    Rs2Walker.walkTo(3619, 9486, 0);
                    Rs2GameObject.interact(43732, "take-10");
                },
                1200,
                500000
        );
    }

    private boolean enterLargeFragmentArea() {
        if (Rs2Inventory.hasItem("guardian fragments")) Microbot.log("Already have fragments - skipping mining step.");
        WorldPoint playerLoc = Rs2Player.getLocalPlayer().getWorldLocation();
        if (playerLoc.getX() <= 3633) {
            Microbot.log("entering large fragment mining area to start game.");
            Rs2Walker.walkTo(3633, 9503, 0);
            doUntil(
                    () -> Rs2Player.getWorldLocation().getX() >= 3637,
                    () -> Rs2GameObject.interact(43724),
                    3000,
                    500000
            );
            Rs2Walker.walkTo(3639,9500, 0, 0);
        }
        playerLoc = Rs2Player.getLocalPlayer().getWorldLocation();
        return playerLoc.getX() >= 3637 && playerLoc.getY() >= 9500;
    }

    private boolean mineFragmentsStart() {
        return doUntil(
                () -> Rs2Inventory.itemQuantity("guardian fragments") >= 120,
                () -> {
                    if (Rs2Antiban.isMining()) Microbot.log("Mining fragments.");
                    else {
                        Microbot.log("Click large remains to mine frags.");
                        Rs2GameObject.interact(43719, "mine");
                    }
                },
                3000,
                500000
        );
    }

    private boolean leaveLargeRemains() {
        WorldPoint playerLoc = Rs2Player.getLocalPlayer().getWorldLocation();
        if (playerLoc.getX() >= 3637) {
            Microbot.log("Leaving large remains area.");
            Rs2Walker.walkTo(3639, 9502, 0);
            doUntil(
                    () -> Rs2Player.getWorldLocation().getX() <= 3633,
                    () -> Rs2GameObject.interact(43726, "climb"),
                    3000,
                    500000
            );
            playerLoc = Rs2Player.getLocalPlayer().getWorldLocation();
        }
        return playerLoc.getX() <= 3633 && playerLoc.getY() >= 9500;
    }

    private boolean makeEssence() {
        return doUntil(
                () -> !Rs2Inventory.hasItem("guardian fragments") || Rs2Inventory.isFull() && Rs2Inventory.hasItem("guardian essence"),
                () -> {
                    if (Rs2Player.getLocalPlayer().getAnimation() != 9365) {
                        Rs2Walker.walkTo(3612, 9488, 0);
                        Rs2GameObject.interact(43754, "work-at");
                    }
                    else Microbot.log("making essence.");
                },
                5000,
                500000
        );
    }

    private AltarInformation determineAltar() {
        AltarInformation catalyticAltar = altarInformations.get(Rs2Widget.getWidget(746, 20).getSpriteId());
        AltarInformation elementalAltar = altarInformations.get(Rs2Widget.getWidget(746, 23).getSpriteId());
        if (catalyticAltar != null && elementalAltar != null) {
            if (catalyticAltar.priority < elementalAltar.priority) return catalyticAltar;
            else return elementalAltar;
        }

        else if (catalyticAltar != null) return catalyticAltar;
        else if (elementalAltar != null) return elementalAltar;
        else return null;
    }

    private boolean craftRunes() {
        AltarInformation destinationAltar = determineAltar();
        System.out.println("sssss " + destinationAltar);
        if (destinationAltar == null) {
            Microbot.log("No altar found.", Level.WARN);
            return false;
        }
        Rs2Walker.walkTo(3615, 9500, 0);
        boolean enteredAltar = doUntil(
                () -> Rs2GameObject.exists(destinationAltar.altarID),
                () -> {
                    if (destinationAltar.altarViewTile.getX() != 1) {
                        if (Rs2Walker.canReach(destinationAltar.altarViewTile)) {
                            Microbot.log("clicking an intermediary tile upon entry to " + destinationAltar.name + " altar.");
                            Rs2Walker.walkTo(destinationAltar.altarViewTile);
                            return;
                        }

                        else Microbot.log(
                                "Have an intermediary tile to arrive at " + destinationAltar.name + " altar, but tile not in view yet."
                        );
                    }

                    Rs2GameObject.interact(destinationAltar.GOTRPillarID, "enter");
                },
                1000,
                15000
        );
        if (!enteredAltar) return false;
        boolean madeRunes = doUntil(
                () -> !Rs2Inventory.hasItem("guardian essence"),
                () -> {
                    Microbot.log("Clicking altar to make " + destinationAltar.name + " runes.");
                    Rs2GameObject.interact(destinationAltar.altarID);
                },
                2000,
                60000
        );
        if (!madeRunes) Microbot.log("Failed to make " + destinationAltar.name +" runes.", Level.WARN);
        doUntilSuccess(
                () -> {
                    WorldPoint playerLoc = Rs2Player.getLocalPlayer().getWorldLocation();
                    return playerLoc.getX() >= 3587 && playerLoc.getX() <= 3643 && playerLoc.getY() >= 9483 && playerLoc.getY() <= 9519;
                },
                () -> {
                    if (destinationAltar.altarExitTile.getX() != 1) {
                        if (Rs2Walker.canReach(destinationAltar.altarExitTile)) {
                            Microbot.log("clicking an intermediary tile upon exit from " + destinationAltar.name + " altar.");
                            Rs2Walker.walkTo(destinationAltar.altarExitTile);
                        }

                        else Microbot.log(
                                "Have an intermediary tile to exit from " + destinationAltar.name + " altar, but tile not in view yet."
                        );
                    }

                    Rs2GameObject.interact(destinationAltar.altarExitId, "enter");
                },
                3000
        );
        return true;
        // charge guardian
        // place the charged cell
        // deposit runes
        // restart
    }

    private void chargeGuardian() {
        doUntilSuccess(
                () -> !gameActive() || (!Rs2Inventory.hasItem("elemental guardian stone") && !Rs2Inventory.hasItem("catalytic guardian stone")),
                () -> {
                    Rs2Npc.interact(11403);
                },
                2000
        );
    }

    public void playMinigame() {
        while (true) {
            if (!enterGameArena()) continue;
            if (!grabCells()) continue;
            if (!enterLargeFragmentArea()) continue;
            if (!sleepUntil(this::gameActive, 10000)) continue;
            Microbot.log("Game has started.");
            if (!mineFragmentsStart()) continue;
            if (!leaveLargeRemains()) continue;
            while (true) {
                int guardianPowerLevel = Rs2UiHelper.extractNumber(Rs2Widget.getWidget(746, 18).getText());
                Microbot.log("Guardian Power Level: " + guardianPowerLevel);
                if (guardianPowerLevel == -1) {
                    Microbot.log("Game ended unexpectedly.", Level.WARN);
                    break;
                }
                if (guardianPowerLevel > 91) {
                    Microbot.log("Game is nearly over - waiting for the end.");
                    break;
                }

                if (!makeEssence()) continue;
                craftRunes();
                Microbot.log("Finished crafting runes");
                chargeGuardian();
                Microbot.log("Finished charging guardian");
            }

            //guardian's power: 10%
            //746,5 ganme up
            //
        }
    }
}
