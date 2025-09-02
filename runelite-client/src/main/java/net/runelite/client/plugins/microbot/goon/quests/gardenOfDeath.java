package net.runelite.client.plugins.microbot.goon.quests;

import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;

public class gardenOfDeath {
    final ArrayList<BankHandler.QuestItem> items = new ArrayList<>(
            List.of(
                    new BankHandler.QuestItem("stamina potion", 3, false, false, false),
                    new BankHandler.QuestItem("secateurs", 1, false, false, false),
                    new BankHandler.QuestItem("antidote++", 1, false, false, false)

            )
    );

    private static void prep() {
        ItemBuyer.buyItems(List.of(
                //new ItemBuyer.ItemToBuy("stamina potion(4)", 3, -1, true),
                //new ItemBuyer.ItemToBuy("antidote++(4)", 1, -1, true),
                //new ItemBuyer.ItemToBuy("secateurs", 1, 5000, true),
                new ItemBuyer.ItemToBuy("egg", 1, 5000, true)
                //new ItemBuyer.ItemToBuy("secateurs", 1, 5000, true)
        ));
        sleep(222222222);
        BankHandler.withdrawQuestItems(
                List.of(
                        //new BankHandler.QuestItem("stamina potion(4)", 3, false, false, false),
                        //new BankHandler.QuestItem("antidote++(4)", 1, false, false, false),
                        new BankHandler.QuestItem("secateurs", 1, false, false, false),
                        new BankHandler.QuestItem("falador teleport", 1, false, false, false),
                        new BankHandler.QuestItem("varrock teleport", 1, false, false, false)
                ), true, true
        );
    }

    public static void completeQuest() {
        prep();
    }
}
