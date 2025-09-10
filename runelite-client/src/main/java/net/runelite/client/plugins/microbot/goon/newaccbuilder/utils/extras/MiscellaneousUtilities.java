package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras;

import net.runelite.api.GameObject;
import net.runelite.api.Skill;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;
import static net.runelite.client.plugins.microbot.util.antiban.Rs2Antiban.WOODCUTTING_ANIMS;
import static net.runelite.client.plugins.microbot.util.player.Rs2Player.isMember;

public class MiscellaneousUtilities {
    static public void setSpell(String spell) {
        Rs2Widget.clickWidget("combat options");
        sleepUntil(() -> Rs2Widget.hasWidget("choose spell"));
        Rs2Widget.clickWidget("choose spell");
        sleepUntil(() -> Rs2Widget.hasWidget(spell));
        Rs2Widget.clickWidget(spell);
    }

    static public boolean waitForQuestFinish(String quest) {
        boolean res = sleepUntil(() -> Rs2Widget.hasWidget("congratulations"), 10000);
        Rs2Widget.clickWidget(153, 16);
        return res;
    }
    static public boolean waitForQuestFinish() {
        boolean res = sleepUntil(() -> Rs2Widget.hasWidget("congratulations"), 10000);
        Rs2Widget.clickWidget(153, 16);
        return res;
    }

    static public void helpFemi() {
        Rs2GameObject.interact(190, "open");
        System.out.println("waiting for dialogue");
        sleepUntil(Rs2Dialogue::isInDialogue);
        System.out.println("handling conversation");
        DialogueHandler.handleConversation(List.of("Okay then."), 5);
        System.out.println("done helping femi");
        sleep(3000);
        System.out.println("opening door");
        Rs2GameObject.interact(190, "open");
        sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 3384);
    }

    static public void gnomeAgil() {
        while (true) {

            if (Rs2Player.isMoving()) {
                System.out.println("Player in motion.");
                continue;
            }

            int x = Rs2Player.getWorldLocation().getX();
            int y = Rs2Player.getWorldLocation().getY();
            int z = Rs2Player.getWorldLocation().getPlane();

            if (z == 0) {
                // at start / end - do first obstacle
                if (y >= 3436) {
                    //only exit this logic if we are at the beginning of the course
                    if (Rs2Player.getRealSkillLevel(Skill.AGILITY) >= 25) {
                        return;
                    }

                    Rs2GameObject.interact(23145, "Walk-across");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getY() <= 3429 && !Rs2Player.isMoving(), 15000);
                    sleep(600);
                }
                //climb up nets
                else if (x < 2480 && y <= 3429) {
                    Rs2GameObject.interact(new WorldPoint(2473,3425, 0), "climb-over");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 1 && !Rs2Player.isMoving());
                    sleep(600);
                }
                //climb nets
                else if (x > 2480 && y < 3426) {
                    Rs2GameObject.interact(new WorldPoint(2487,3426, 0), "climb-over");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 3427 && !Rs2Player.isMoving());
                    sleep(600);
                }
                //squeeze tube
                else if (x > 2480 && y <= 3430) {
                    Rs2GameObject.interact(new WorldPoint(2487,3431, 0), "squeeze-through");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getY() >= 3437 && !Rs2Player.isMoving(), 15000);
                    sleep(600);
                }
            }

            else if (z == 1) {
                if (x <= 2476) {
                    Rs2GameObject.interact(23559, "climb");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 2 && !Rs2Player.isMoving());
                    sleep(600);
                }
            }

            else if (z == 2) {
                if (x <= 2477) {
                    Rs2GameObject.interact(23557, "walk-on");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getX() >= 2483 && !Rs2Player.isMoving());
                    sleep(600);
                }

                else {
                    Rs2GameObject.interact(23560, "climb-down");
                    sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 0 && !Rs2Player.isMoving());
                    sleep(600);
                }
            }

        }
    }

    public static void hopWorlds(int world) {
        int randomWorld = world == -1 ? Login.getRandomWorld(isMember()) : world;
        System.out.println("dddd: " + randomWorld);
        Microbot.hopToWorld(randomWorld);
    }

    public static void walkToGE() {
        Rs2Walker.walkTo(3164, 3485, 0);
    }

    public static void cowMagerAndRanger() {
        walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("staff of air", 1, false, false, true),
                new BankHandler.QuestItem("mind rune", 1, false, true, false),
                new BankHandler.QuestItem("fire rune", 1, false, true, false),
                new BankHandler.QuestItem("iron dart", 1, false, true, false),
                new BankHandler.QuestItem("lumbridge teleport", 1, false, false, false),
                new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
        ), true, true);
        setSpell("fire strike");
        Rs2Walker.walkTo(3259, 3288, 0);
        while (Rs2Inventory.itemQuantity("mind rune") >= 1 && Rs2Inventory.itemQuantity("fire rune") >= 3) {
            if (Rs2Player.getInteracting() != null) System.out.println("Currently fighting.");
            else Rs2Npc.attack("cow");
        }
        Rs2Inventory.wield("iron dart");
        sleep(1200);
        while (Rs2Player.hasPlayerEquippedItem(Rs2Player.getLocalPlayer(), "iron dart")) {
            if (Rs2Player.getInteracting() != null) System.out.println("Currently fighting.");
            else Rs2Npc.attack("cow");
        }
    }

    public static void getPOH() {
        walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("coins", 10000, false, false, false)
        ), true, true);
        Rs2Walker.walkTo(3240, 3475, 0);
        DialogueHandler.talkToNPC("estate agent", List.of("How can I get a house?", "Yes please!"), 5);
    }

    public static void bankAtFerox(List<BankHandler.QuestItem> itemsToWithdraw, boolean drinkRejuvPool, boolean dumpInv, boolean dumpEquip) {
        Rs2Walker.walkTo(3132, 3628, 0);
        BankHandler.withdrawQuestItems(itemsToWithdraw, dumpInv, dumpEquip);
        if (drinkRejuvPool) {
            Rs2GameObject.interact(39651);
            sleepUntil(() -> Rs2Player.getRunEnergy() >= 95, 10000);
        }
    }

    public static void makeAirRunes() {
        walkToGE();
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("teleport to house", 1, false, true, false),
                new BankHandler.QuestItem("air tiara", 1, false, false, true),
                new BankHandler.QuestItem("dueling", 1, false, false, true),
                new BankHandler.QuestItem("pure essence", 1, false, true, false)
        ), true, true);
        sleepUntil(() -> Rs2Inventory.hasItem("pure essence"));
        while (true) {
            if (Rs2Inventory.hasItem("pure essence")) {
                System.out.println("making air runes");
                Rs2Walker.walkTo(2983, 3288, 0);
                Rs2GameObject.interact(34813);
                sleepUntil(() -> Rs2Player.getWorldLocation().getY() > 4500, 5000);
                Rs2GameObject.interact(34760);
                sleepUntil(() -> !Rs2Inventory.hasItem("pure essence"), 5000);
            }

            else {
                ArrayList<BankHandler.QuestItem> items = new ArrayList<>();
                if (!Rs2Player.hasPlayerEquippedItem(Rs2Player.getLocalPlayer(), "dueling")) items.add(
                        new BankHandler.QuestItem("ring of dueling", 1, false, false, true)
                );
                items.add(new BankHandler.QuestItem("pure essence", 1, false, true, false));
                Rs2Bank.openBank();
                Rs2Bank.depositAll("air rune");
                bankAtFerox(items, Rs2Player.getRunEnergy() < 50, false, false);
                boolean success = sleepUntil(() -> Rs2Inventory.hasItem("pure essence"), 5000);
                if (!success) return;
            }
        }
    }

    public static void sardineCooker() {
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("games necklace", 1, false, false, true),
                new BankHandler.QuestItem("raw sardine", 1, false, true, false)
        ), true, true);
        Rs2Walker.walkTo(2909, 3540, 0);
        Rs2Walker.walkTo(3042, 4975, 1);
        while (true) {
            if (Microbot.isGainingExp) continue;
            else if (Rs2Inventory.hasItem("raw sardine")) {
                Rs2GameObject.interact(43475);
                sleepUntil(() -> Rs2Widget.hasWidget("how many"));
                Rs2Keyboard.keyPress(KeyEvent.VK_SPACE);
                sleep(Rs2Inventory.count("raw sardine") * 2000);
            }
            else {
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("raw sardine", 1, false, true, false)
                ), true, false);
                boolean result = sleepUntil(() -> Rs2Inventory.hasItem("raw sardine"), 5000);
                if (!result) return;
            }
        }
    }

    public static void wtPrep() {
        final List<ItemBuyer.ItemToBuy> items = List.of(
                new ItemBuyer.ItemToBuy("polar camo top", 1, 25000, false),
                new ItemBuyer.ItemToBuy("polar camo legs", 1, 25000, false),
                new ItemBuyer.ItemToBuy("fire tiara", 1, 2500, false),
                new ItemBuyer.ItemToBuy("cake", 1000, -1, true),
                new ItemBuyer.ItemToBuy("grey gloves", 1, 10000, false)
        );
        walkToGE();
        ItemBuyer.buyItems(items);
        ItemBuyer.ensureAllOffersCollected(true);
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("polar camo top", 1, false, false, true),
                new BankHandler.QuestItem("polar camo legs", 1, false, false, true),
                new BankHandler.QuestItem("fire tiara", 1, false, false, true),
                new BankHandler.QuestItem("grey gloves", 1, false, false, true),
                new BankHandler.QuestItem("games necklace", 1, false, false, true),
                new BankHandler.QuestItem("adamant axe", 1, false, false, true),
                new BankHandler.QuestItem("knife", 1, false, false, false),
                new BankHandler.QuestItem("tinderbox", 1, false, false, false),
                new BankHandler.QuestItem("falador teleport", 1, false, false, false)
        ), true, true);
        Rs2Walker.walkTo(3053, 3247, 0);
        List<String> dialogue = List.of(
                "That's great, can you take me there please?",
                "Goodbye."
        );
        DialogueHandler.talkToNPC("veos", dialogue, 15);
        Rs2Walker.walkTo(1634, 3938, 0);
    }

    public static void levelWc() {
        walkToGE();
        ItemBuyer.buyItems(List.of(
                new ItemBuyer.ItemToBuy("steel axe", 1, 5000, false),
                new ItemBuyer.ItemToBuy("mithril axe", 1, 5000, false),
                new ItemBuyer.ItemToBuy("adamant axe", 1, 5000, false)
        ));
        BankHandler.withdrawQuestItems(List.of(
                new BankHandler.QuestItem("steel axe", 1, false, false, true),
                new BankHandler.QuestItem("mithril axe", 1, false, false, false),
                new BankHandler.QuestItem("adamant axe", 1, false, false, false)
        ), true, true);
        Rs2Walker.walkTo(3160, 3454, 0);
        while (Microbot.getClient().getRealSkillLevel(Skill.WOODCUTTING) < 15) {
            if (WOODCUTTING_ANIMS.contains(Rs2Player.getAnimation())) continue;
            else if (Rs2Inventory.isFull()) Rs2Inventory.dropAll("logs");
            GameObject tree = Rs2GameObject.getGameObject("tree", true);
            if (tree != null) Rs2GameObject.interact(tree, "chop down");
            else Rs2Walker.walkTo(3160, 3454, 0);
        }
        Rs2Inventory.dropAll("logs");
        Rs2Walker.walkTo(3165, 3416, 0);
        while (Microbot.getClient().getRealSkillLevel(Skill.WOODCUTTING) < 31) {
            if (WOODCUTTING_ANIMS.contains(Rs2Player.getAnimation())) continue;
            else if (Rs2Inventory.isFull()) Rs2Inventory.dropAll("oak logs");
            GameObject tree = Rs2GameObject.getGameObject("oak tree", true);
            if (tree != null) Rs2GameObject.interact(tree, "chop down");
            else Rs2Walker.walkTo(3165, 3416, 0);
        }
    }
}
