package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.seaslug;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.grounditem.Rs2GroundItem;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class SeaSlug {
    ArrayList<BankHandler.QuestItem> slugItems = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("swamp paste", 1, false, false, false),
                    new BankHandler.QuestItem("torch", 1, false, false, false),
                    new BankHandler.QuestItem("black pickaxe", 1, false, false, false),
                    new BankHandler.QuestItem("iron bar", 1, false, false, false),
                    new BankHandler.QuestItem("ardougne teleport", 1, false, false, false),
                    new BankHandler.QuestItem("redberry pie", 1, false, false, false),
                    new BankHandler.QuestItem("prayer potion", 3, false, false, false),
                    new BankHandler.QuestItem("varrock teleport", 3, false, false, false),
                    new BankHandler.QuestItem("falador teleport", 3, false, false, false),
                    new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                    new BankHandler.QuestItem("monk's robe", 1, false, false, true)
            )
    );

    List<String> dialogue = new ArrayList<>(List.of(
            "I suppose so, how do I get there?",
            "Yes.",
            "Will you take me there?"
    ));

    private void prep() {
        Rs2Walker.walkTo(3161, 3489, 0);
        BankHandler.withdrawQuestItems(slugItems, true, true);
    }

    public void completeQuest() {
        prep();
        Rs2Walker.walkTo(2710, 3306, 0);
        DialogueHandler.talkToNPC("caroline", dialogue, 5);
        DialogueHandler.talkToNPC("holgart", dialogue, 15);
        DialogueHandler.talkToNPC("holgart", dialogue, 15);
        Rs2Walker.walkTo(2782,3286,0);
        Rs2GameObject.interact(18324, "climb-up");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 1);
        Rs2Walker.walkTo(2767,3285,1);
        Rs2GameObject.interact(18168, "open");
        sleep(3000);
        Rs2Walker.walkTo(2765,3286,1, 0);
        DialogueHandler.talkToNPC("kennith", dialogue, 5);
        Rs2Walker.walkTo(2782,3288,1, 0);
        Rs2GameObject.interact(18325, "climb-down");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 0);
        Rs2Walker.walkTo(2776, 3281,0);
        DialogueHandler.talkToNPC("holgart", dialogue, 15);
        DialogueHandler.talkToNPC("kent", dialogue, 20);
        DialogueHandler.talkToNPC("holgart", dialogue, 15);
        Rs2Walker.walkTo(2769, 3288, 0);
        Rs2GameObject.interact(18168, "open");
        sleep(3000);
        Rs2GroundItem.interact(1469, "take", 8);
        sleepUntil(() -> Rs2Inventory.hasItem(1469));
        Rs2Walker.walkTo(2781, 3290, 0);
        Rs2GroundItem.interact(1467, "take", 8);
        sleepUntil(() -> Rs2Inventory.hasItem(1467));
        Rs2Inventory.interact(1467, "use");
        Rs2Inventory.interact(1469, "use");
        sleepUntil(() -> Rs2Inventory.hasItem(1468));
        Rs2Inventory.interact(1468, "rub-together");
        sleepUntil(() -> Rs2Inventory.hasItem("lit torch"));
        Rs2Walker.walkTo(2782,3286,0);
        Rs2GameObject.interact(18324, "climb-up");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 1);
        Rs2Walker.walkTo(2767,3285,1);
        Rs2GameObject.interact(18168, "open");
        sleep(3000);
        Rs2Walker.walkTo(2765,3286,1, 0);
        DialogueHandler.talkToNPC("kennith", dialogue, 5);
        Rs2Walker.walkTo(2769,3289,1,0);
        Rs2GameObject.interact(18251, "kick");
        sleep(10000);
        Rs2Walker.walkTo(2767,3285,1);
        Rs2GameObject.interact(18168, "open");
        sleep(3000);
        Rs2Walker.walkTo(2765,3286,1, 0);
        DialogueHandler.talkToNPC("kennith", dialogue, 5);
        Rs2Walker.walkTo(2772,3291,1);
        Rs2GameObject.interact("crane", "rotate");
        DialogueHandler.handleConversation(List.of(), 30);
        Rs2Walker.walkTo(2782,3288,1, 0);
        Rs2GameObject.interact(18325, "climb-down");
        sleepUntil(() -> Rs2Player.getWorldLocation().getPlane() == 0);
        Rs2Walker.walkTo(2776, 3281,0);
        DialogueHandler.talkToNPC("holgart", dialogue, 25);
        DialogueHandler.talkToNPC("caroline", dialogue, 5);
        MiscellaneousUtilities.waitForQuestFinish();
    }
}
