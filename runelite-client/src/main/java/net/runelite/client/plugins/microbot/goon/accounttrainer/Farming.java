package net.runelite.client.plugins.microbot.goon.accounttrainer;

import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2ObjectModel;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.*;

public class Farming {

    private static void plantDeadTrees() {
        if (Rs2Inventory.hasUnNotedItem("bagged dead tree") && Rs2Inventory.hasItem("watering can(")) {
            if (Microbot.getClient().getTopLevelWorldView().isInstance()) {
                if (Rs2GameObject.exists(15362)) {
                    System.out.println("found empty tree spot");
                    doUntil(
                            () -> Rs2Widget.hasWidget("furniture creation menu"),
                            () -> Rs2GameObject.interact(15362, "build"),
                            3000,
                            15000
                    );
                    System.out.println("selecting dead tree");
                    doUntil(
                            () -> Rs2Player.getLocalPlayer().getAnimation() == 2293,
                            () -> Rs2Keyboard.typeString("1"),
                            3000,
                            15000
                    );
                    System.out.println("waiting for tree to be planted");
                    sleepUntil(() -> Rs2GameObject.exists(4531), 10000);
                    //2293 anim
                }

                else if (Rs2GameObject.exists(4531)) {
                    System.out.println("found planted tree");
                    doUntil(
                            Rs2Dialogue::isInDialogue,
                            () -> Rs2GameObject.interact(4531, "remove"),
                            3000,
                            30000
                    );
                    doUntil(
                            () -> !Rs2GameObject.exists(4531),
                            () -> DialogueHandler.handleConversation(List.of("Yes", "Yes."), 1),
                            1000,
                            30000
                    );
                    System.out.println("tree removed");
                }
            }
            else {
                Rs2Walker.walkTo(2955, 3224, 0);
                doUntil(
                        () -> Microbot.getClient().getTopLevelWorldView().isInstance(),
                        () -> Rs2GameObject.interact(15478, "build mode"),
                        1000,
                        1000000
                );
                sleep(3000);
            }
        }

        else {
            if (Microbot.getClient().getTopLevelWorldView().isInstance()) Rs2GameObject.interact(4525, "leave");
            else {
                Rs2Walker.walkTo(2950,3213,0,0);
                Rs2Inventory.use("bagged dead tree");
                sleepUntil(Rs2Inventory::isItemSelected);
                Rs2Npc.interact(Rs2Npc.getNpc("phials"));
                sleepUntil(() -> Rs2Widget.hasWidget("Exchange All:"));
                Rs2Widget.clickWidget("Exchange All:");
                Rs2Inventory.waitForInventoryChanges(2000);

                Rs2Walker.walkTo(2967, 3210, 0, 0);
                doUntil(
                        () -> !Rs2Inventory.hasItem("watering can", true),
                        () -> {
                            Rs2Inventory.use(Rs2Inventory.get("watering can", false));
                            Rs2GameObject.interact(9684, "use");
                        },
                        1000,
                        100000
                );
                Rs2Walker.walkTo(2954, 3210, 0);
            }
        }

    }
    public static void baggedPlants() {
        System.out.println("starting bagged plants");
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
        ), true, true);
        MiscellaneousUtilities.walkToGE();
        ItemBuyer.buyItems(List.of(
                new ItemBuyer.ItemToBuy("bagged dead tree", 298, -1, true),
                new ItemBuyer.ItemToBuy("watering can", 3, 5000, true)
        ));
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
        doUntil(
                () -> !Microbot.getClient().getTopLevelWorldView().isInstance(),
                () -> Rs2GameObject.interact(4525, "leave"),
                5000,
                1000000
        );
    }
}
