package net.runelite.client.plugins.microbot.goon.accounttrainer;

import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2ObjectModel;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class Farming {

    private static void plantDeadTrees() {
        if (Rs2Inventory.hasUnNotedItem("bagged dead tree") && Rs2Inventory.hasItem("watering can(")) {
            if (Microbot.getClient().getTopLevelWorldView().isInstance()) {
                //15362 unplanted
                //build
                System.out.println("ready to plant trees");
                // check for interfaces first for removing or planting
                // check if tree is planted, it so remove
            }
            else {
                Rs2Walker.walkTo(2955, 3224, 0, 0);
                doUntil(
                        () -> Microbot.getClient().getTopLevelWorldView().isInstance(),
                        () -> Rs2GameObject.interact("portal", "build mode"),
                        5000,
                        1000000
                );

            }
        }

        else {
            if (Microbot.getClient().getTopLevelWorldView().isInstance()) System.out.println("need to leave house");
            else if (!Rs2Inventory.hasItem("watering can(")) {
                Rs2Walker.walkTo(2967, 3210, 0, 0);
                doUntil(
                        () -> !Rs2Inventory.hasItem("watering can", true),
                        () -> {
                            Rs2Inventory.use("watering can");
                            Rs2GameObject.interact(9684, "use");
                        },
                        1000,
                        100000
                );
                Rs2Walker.walkTo(2954, 3210, 0);
            }

            else if (!Rs2Inventory.hasUnNotedItem("bagged dead tree") && Rs2Inventory.hasNotedItem("bagged dead tree")) {
                Rs2Walker.walkTo(2950,3213,0,0);
                Rs2Inventory.use("bagged dead tree");
                sleepUntil(Rs2Inventory::isItemSelected);
                Rs2Npc.interact(Rs2Npc.getNpc("phials"));
                sleepUntil(() -> Rs2Widget.hasWidget("Exchange All:"));
                Rs2Widget.clickWidget("Exchange All:");
                Rs2Inventory.waitForInventoryChanges(2000);
            }
        }

    }
    public static void baggedPlants() {
        System.out.println("starting bagged plants");
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
        ), true, true);
        MiscellaneousUtilities.walkToGE();
        /*ItemBuyer.buyItems(List.of(
                new ItemBuyer.ItemToBuy("bagged dead tree", 298, -1, true),
                new ItemBuyer.ItemToBuy("watering can", 3, 5000, true)
        ));*/
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("coins", 100000, false, false, false),
                new BankHandler.QuestItem("bagged dead tree", 1, true, true, false),
                new BankHandler.QuestItem("watering can", 3, false, true, false),
                new BankHandler.QuestItem("teleport to house", 1, false, false, false),
                new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
        ), false, false);
        doUntil(
                () -> {
                    WorldPoint wp = Rs2Player.getWorldLocation();
                    return wp.getX() >= 2945 && wp.getX() <= 2960 && wp.getY() >= 3212 && wp.getY() <= 3235;
                },
                () -> Rs2Inventory.interact("teleport to house", "outside"),
                10000,
                1000000
        );
        Rs2Walker.walkTo(2956, 3213, 0);
        while (Rs2Inventory.hasItem("bagged dead tree")) {
            plantDeadTrees();
        }
    }
}
