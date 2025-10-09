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
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.slf4j.event.Level;

import java.util.*;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

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
    }

    // cata
    // mind 4354
    // body 4358
    // cosmic 4359
    // chaos 4360
    // nat 4361
    // law 4362
    // death 4363
    // blood 4364
    //elem
    // air 4353
    // water 4355
    //fire 4357
    // earth 4356

    final Map<Integer, AltarInformation> altarInformations = Map.of(
            4353, new AltarInformation(
                    "air", 4353, 43701, 4, -1, 1, new WorldPoint(1, 1, 1)
            )
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
                () -> Rs2Inventory.itemQuantity("uncharged cell") == 10,
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

    /*private int findAltar(int cata, int elem) {

    }

    private craftRunes() {
        // cata
        // mind 4354
        // body 4358
        // cosmic 4359
        // chaos 4360
        // nat 4361
        // law 4362
        // death 4363
        // blood 4364
        Rs2Widget.getWidget(746, 20).getSpriteId();
        //elem
        // air 4353
        // water 4355
        //fire 4357
        // earth 4356
        Rs2Widget.getWidget(746, 23).getSpriteId();
        // run to 3615, 9500, 0 in center of room to see all pillars to enter
        // find which pillar active
        // enter it
        // craft rune
        // exit
        // charge guardian
        // place the charged cell
        // deposit runes
        // restart
    }*/

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
                if (guardianPowerLevel == -1) {
                    Microbot.log("Game ended unexpectedly.", Level.WARN);
                    break;
                }
                if (guardianPowerLevel > 91) {
                    Microbot.log("Game is nearly over - waiting for the end.");
                    break;
                }

                if (!makeEssence()) continue;
            }

            //guardian's power: 10%
            //746,5 ganme up
            //
        }
    }
}
