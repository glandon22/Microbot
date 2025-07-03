package net.runelite.client.plugins.microbot.goon.newaccbuilder;

import com.google.inject.Provides;
import net.runelite.api.Skill;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.cooksassistant.CooksAssistant;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.doricsquest.DoricsQuest;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.druidicritual.DruidicRitual;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.goblindiplomacy.GoblinDiplomacy;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.impcatcher.ImpCatcher;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.naturalhistoryquiz.NaturalHistoryQuiz;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.romeoandjuliet.RomeoAndJuliet;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.runemysteries.RuneMysteries;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.sheepshearer.SheepShearer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.vampireslayer.VampireSlayer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.waterfallquest.WaterfallQuest;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witcheshouse.WitchesHouse;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witchespotion.WitchesPotion;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.MiscellaneousUtilities;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.PrayerLeveler;
import net.runelite.client.plugins.microbot.prayer.GildedAltarConfig;
import net.runelite.client.plugins.microbot.prayer.GildedAltarScript;
import net.runelite.client.plugins.microbot.shortestpath.ShortestPathPlugin;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NewAccBuilderScript extends Script {
    String scriptMainTask = "Starting Script";
    String scriptSubTask = "Determining task";
    PrayerLeveler prayerLeveler = new PrayerLeveler();
    ItemBuyer itemBuyer = new ItemBuyer();
    BankHandler bankHandler = new BankHandler();
    CooksAssistant cooksAssistant = new CooksAssistant();
    SheepShearer sheepShearer = new SheepShearer();
    ImpCatcher impCatcher = new ImpCatcher();
    WitchesPotion witchesPotion = new WitchesPotion();
    VampireSlayer vampireSlayer = new VampireSlayer();
    RomeoAndJuliet romeoAndJuliet = new RomeoAndJuliet();
    DoricsQuest doricsQuest = new DoricsQuest();
    GoblinDiplomacy goblinDiplomacy = new GoblinDiplomacy();
    WitchesHouse witchesHouse = new WitchesHouse();
    DruidicRitual druidicRitual = new DruidicRitual();
    RuneMysteries runeMysteries = new RuneMysteries();
    NaturalHistoryQuiz naturalHistoryQuiz = new NaturalHistoryQuiz();
    WaterfallQuest waterfallQuest = new WaterfallQuest();

    public boolean run(NewAccBuilderConfig config) {
        ArrayList<BankHandler.QuestItem> vampAndRomeoJulietItems = new ArrayList<>();
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("staff of air", 1, false, false, true)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("garlic", 1, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("hame", 1, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("cadava berries", 1, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("beer", 1, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("mind rune", 1000, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("earth rune", 1000, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("varrock teleport", 10, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("amulet of glory", 1, false, false, false)
        );
        vampAndRomeoJulietItems.add(
                new BankHandler.QuestItem("hammer", 1, false, false, false)
        );
        ArrayList<BankHandler.QuestItem> doricsAndGoblinDiploItems = new ArrayList<>(
                List.of(
                        new BankHandler.QuestItem("goblin mail", 3, false, false, false),
                        new BankHandler.QuestItem("blue dye", 1, false, false, false),
                        new BankHandler.QuestItem("red dye", 1, false, false, false),
                        new BankHandler.QuestItem("yellow dye", 1, false, false, false),
                        new BankHandler.QuestItem("clay", 6, false, false, false),
                        new BankHandler.QuestItem("iron ore", 2, false, false, false),
                        new BankHandler.QuestItem("copper ore", 4, false, false, false),
                        new BankHandler.QuestItem("falador teleport", 10, false, false, false)
                )
        );
        ArrayList<BankHandler.QuestItem> witchesHouseAndDruidicItems = new ArrayList<>(
                List.of(
                        new BankHandler.QuestItem("prayer potion(4)", 3, false, false, false),
                        new BankHandler.QuestItem("raw chicken", 1, false, false, false),
                        new BankHandler.QuestItem("raw beef", 1, false, false, false),
                        new BankHandler.QuestItem("raw bear meat", 1, false, false, false),
                        new BankHandler.QuestItem("raw rat meat", 1, false, false, false),
                        new BankHandler.QuestItem("leather gloves", 1, false, false, true),
                        new BankHandler.QuestItem("cheese", 1, false, false, false),
                        new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                        new BankHandler.QuestItem("monk's robe", 1, false, false, true),
                        new BankHandler.QuestItem("staff of air", 1, false, false, true),
                        new BankHandler.QuestItem("earth rune", 1000, false, false, false),
                        new BankHandler.QuestItem("mind rune", 1000, false, false, false),
                        new BankHandler.QuestItem("falador teleport", 10, false, false, false)
                )
        );
        ArrayList<BankHandler.QuestItem> runeMysteriesAndNatQuiz = new ArrayList<>(
                List.of(
                        new BankHandler.QuestItem("falador teleport", 10, false, false, false),
                        new BankHandler.QuestItem("varrock teleport", 10, false, false, false),
                        new BankHandler.QuestItem("lumbridge teleport", 5, false, false, false),
                        new BankHandler.QuestItem("necklace of passage", 1, false, false, false)
                )
        );
        ArrayList<BankHandler.QuestItem> waterfallPt1 = new ArrayList<>(
                List.of(
                        new BankHandler.QuestItem("games necklace", 1, false, false, false),
                        new BankHandler.QuestItem("rope", 1, false, false, false),
                        new BankHandler.QuestItem("trout", 10, false, false, false),
                        new BankHandler.QuestItem("prayer potion", 3, false, false, false),
                        new BankHandler.QuestItem("monk's robe top", 1, false, false, true),
                        new BankHandler.QuestItem("monk's robe", 1, false, false, true)

                )
        );
        ArrayList<ItemBuyer.ItemToBuy> necessaryAccountItems = new ArrayList<>();
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("staff of air", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("amulet of glory(6)", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("necklace of passage(5)", 3, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("dragon bones", 215, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron scimitar", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("eye of newt", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("onion", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("garlic", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("hammer", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("cadava berries", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("beer", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("earth rune", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("air rune", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("fire rune", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("water rune", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("mind rune", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("varrock teleport", 100, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("goblin mail", 3, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("red dye", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("blue dye", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("yellow dye", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("clay", 6, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron ore", 2, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("copper ore", 4, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("falador teleport", 100, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw chicken", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw beef", 2, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw bear meat", 1, 3000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw rat meat", 1, 3000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("leather gloves", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("cheese", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("lumbridge teleport", 100, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("rope", 10, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("games necklace(8)", 3, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("skills necklace(4)", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("ring of dueling(8)", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("khazard teleport", 5, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("dwellberries", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("spade", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("chocolate dust", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("snape grass", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("bucket of milk", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("bucket of water", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("logs", 7, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("jug of water", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("swamp paste", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("unlit torch", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("black pickaxe", 1, 10000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("redberry pie", 1, 10000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron bar", 2, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("ardougne teleport", 10, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("arrow shaft", 7000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("feather", 7000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("guam potion(unf)", 887, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("molten glass", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron dart", 2000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw sardine", 600, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("steel nails", 2000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("saw", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("plank", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("pure essence", 1000, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("air tiara", 1, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("glassblowing pipe", 1, 1000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("trout", 100, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("prayer potion(4)", 10, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe top", 1, 3000));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe", 1, 3000));


        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                //cooksAssistant.completeQuest();
                //sheepShearer.completeQuest();
                //impCatcher.doQuest();
                //Rs2Walker.walkTo(3093, 3243, 0);
                //itemBuyer.buyItems(necessaryAccountItems);
                //itemBuyer.ensureAllOffersCollected(true);
                //witchesPotion.completeQuest();
                //prayerLeveler.hopWorlds(330);
                /*Plugin plugin = Microbot.getPluginManager().getPlugins().stream()
                        .filter(p -> Objects.equals(p.getName(), PluginDescriptor.Mocrosoft + "Gilded Altar"))
                        .findFirst()
                        .orElse(null);
                Microbot.getClientThread().runOnSeperateThread(() -> {
                    Microbot.startPlugin(plugin);
                    return false;
                });
                while (Microbot.getClient().getRealSkillLevel(Skill.PRAYER) != 43) {
                    System.out.println(Microbot.getClient().getRealSkillLevel(Skill.PRAYER));
                    sleep(1000, 1100);
                }
                Microbot.getClientThread().runOnSeperateThread(() -> {
                    Microbot.stopPlugin(plugin);
                    return false;
                });
                prayerLeveler.levelTo43();
                sleepUntil(() -> !Microbot.getClient().getTopLevelWorldView().getScene().isInstance());
                Rs2Walker.walkTo(3093, 3242, 0, 3);
                bankHandler.withdrawQuestItems(vampAndRomeoJulietItems, true, true);
                MiscellaneousUtilities.setSpell("earth strike");
                vampireSlayer.completeQuest();
                Rs2Walker.walkTo(3212, 3423, 0, 2);
                romeoAndJuliet.completeQuest();
                Rs2Walker.walkTo(3183, 3437, 0);
                bankHandler.withdrawQuestItems(doricsAndGoblinDiploItems, true, true);
                doricsQuest.completeQuest();
                //low confidence in this script currently working. need to monitor dialogue closely
                goblinDiplomacy.completeQuest();
                Rs2Walker.walkTo(2945, 3370, 0, 2);
                bankHandler.withdrawQuestItems(witchesHouseAndDruidicItems, true, true);
                MiscellaneousUtilities.setSpell("earth strike");
                witchesHouse.completeQuest();
                druidicRitual.completeQuest();
                Rs2Walker.walkTo(2944, 3370, 0, 2);
                bankHandler.withdrawQuestItems(runeMysteriesAndNatQuiz, true, true);
                runeMysteries.completeQuest();
                naturalHistoryQuiz.completeQuest();*/
                Rs2Walker.walkTo(3168, 3489, 0, 2);
                BankHandler.withdrawQuestItems(waterfallPt1, true, true);
                waterfallQuest.completeQuest();
                System.out.println("shutting down");
                shutdown();
            } catch (Exception ex) {
                shutdown();
                System.out.println("this broke");
                throw(ex);

            }
        },0, 600, TimeUnit.MILLISECONDS);
        return false;
    }
}
