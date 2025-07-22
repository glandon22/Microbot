package net.runelite.client.plugins.microbot.goon.newaccbuilder;

import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.cooksassistant.CooksAssistant;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.doricsquest.DoricsQuest;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.druidicritual.DruidicRitual;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.fightarena.FightArena;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.goblindiplomacy.GoblinDiplomacy;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.grandtree.GrandTree;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.impcatcher.ImpCatcher;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.knightssword.KnightsSword;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.monksfriend.MonksFriend;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.naturalhistoryquiz.NaturalHistoryQuiz;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.plaguecity.PlagueCity;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.rfd.RFDStart;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.romeoandjuliet.RomeoAndJuliet;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.runemysteries.RuneMysteries;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.seaslug.SeaSlug;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.sheepshearer.SheepShearer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.treegnomevillage.TreeGnomeVillage;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.vampireslayer.VampireSlayer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.waterfallquest.WaterfallQuest;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witcheshouse.WitchesHouse;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.witchespotion.WitchesPotion;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.BankHandler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.ItemBuyer;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.FmLeveler;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras.PrayerLeveler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewAccBuilderScript extends Script {
    FmLeveler fmLeveler = new FmLeveler();
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
    GrandTree grandTree = new GrandTree();
    TreeGnomeVillage treeGnomeVillage = new TreeGnomeVillage();
    MonksFriend monksFriend = new MonksFriend();
    PlagueCity plagueCity = new PlagueCity();
    SeaSlug seaSlug = new SeaSlug();
    KnightsSword knightsSword = new KnightsSword();
    FightArena fightArena = new FightArena();

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
        ArrayList<BankHandler.QuestItem> grandTreeItems = new ArrayList<>(
                List.of(
                        new BankHandler.QuestItem("games necklace", 1, false, false, false),
                        new BankHandler.QuestItem("necklace of passage", 1, false, false, false),
                        new BankHandler.QuestItem("prayer potion(4)", 3, false, false, false),
                        new BankHandler.QuestItem("varrock teleport", 3, false, false, false),
                        new BankHandler.QuestItem("rope", 3, false, false, false),
                        new BankHandler.QuestItem("mind rune", 1000, false, false, false),
                        new BankHandler.QuestItem("fire rune", 1000, false, false, false),
                        new BankHandler.QuestItem("staff of air", 1, false, false, true),
                        new BankHandler.QuestItem("ring of dueling", 1, false, false, true)
                )
        );

        ArrayList<ItemBuyer.ItemToBuy> necessaryAccountItems = new ArrayList<>();
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("egg", 1, 5000, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("pot of flour", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("bucket of milk", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("red bead", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("ball of wool", 20, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("yellow bead", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("white bead", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("black bead", 1, 5000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("staff of air", 1, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("amulet of glory(6)", 1, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("necklace of passage(5)", 3, -1, false));
        //necessaryAccountItems.add(new ItemBuyer.ItemToBuy("dragon bones", 215, -1));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron scimitar", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("eye of newt", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("onion", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("garlic", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("hammer", 1, 1000, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("cadava berries", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("beer", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("earth rune", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("air rune", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("fire rune", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("water rune", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("mind rune", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("varrock teleport", 100, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("goblin mail", 3, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("red dye", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("blue dye", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("yellow dye", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("clay", 6, -1, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron ore", 2, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("copper ore", 4, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("falador teleport", 100, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("teleport to house", 100, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw chicken", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw beef", 2, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw bear meat", 1, 3000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw rat meat", 1, 3000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("leather gloves", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("cheese", 1, 1000, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("lumbridge teleport", 100, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("rope", 10, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("games necklace(8)", 3, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("skills necklace(4)", 1, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("ring of dueling(8)", 11, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("khazard teleport", 5, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("dwellberries", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("spade", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("chocolate dust", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("snape grass", 1, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("bucket of milk", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("bucket of water", 10, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("logs", 75, -1, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("oak logs", 200, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("willow logs", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("jug of water", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("swamp paste", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("unlit torch", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("black pickaxe", 1, 10000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("redberry pie", 1, 10000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron bar", 2, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("ardougne teleport", 10, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("arrow shaft", 7000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("feather", 7000, -1, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("guam potion (unf)", 887, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("molten glass", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("iron dart", 2000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("raw sardine", 600, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("steel nails", 2000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("saw", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("tinderbox", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("plank", 1, -1, true));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("pure essence", 1000, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("air tiara", 1, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("glassblowing pipe", 1, 1000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("trout", 100, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("prayer potion(4)", 10, -1, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe top", 1, 3000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe", 1, 3000, false));
        // add some random junk on the end in case i fail to withdraw everything from ge bc of delays etc
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe", 1, 3000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe", 1, 3000, false));
        necessaryAccountItems.add(new ItemBuyer.ItemToBuy("monk's robe", 1, 3000, false));


        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                //gonna have to fix this and set exact names for shit
                ItemBuyer.buyItems(necessaryAccountItems);
                //should change this to buy everything first then go down and do quests
                /*cooksAssistant.completeQuest();
                sheepShearer.completeQuest();
                Rs2Walker.walkTo(3293, 3379, 0);
                Rs2Walker.walkTo(3278, 3428, 0);
                Rs2Walker.walkTo(3164, 3483, 0);
                itemBuyer.buyItems(necessaryAccountItems);
                itemBuyer.ensureAllOffersCollected(true);
                impCatcher.completeQuest();
                witchesPotion.completeQuest();
                Rs2Walker.walkTo(3093, 3243, 0, 2);
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("dragon bones", 1, true, true, false),
                        new BankHandler.QuestItem("coins", 100000, false, false, false)
                ), true, true);
                Rs2Walker.walkTo(2954, 3219, 0);
                while (Rs2Player.getWorld() != 330) {
                    MiscellaneousUtilities.hopWorlds(330);
                }
                prayerLeveler.levelTo43();
                System.out.println("done with prayer training");
                while (Rs2Player.getWorld() == 330) {
                    System.out.println("hopping out of 330");
                    MiscellaneousUtilities.hopWorlds(-1);
                }
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
                goblinDiplomacy.completeQuest();
                Rs2Walker.walkTo(2945, 3370, 0, 2);
                BankHandler.withdrawQuestItems(witchesHouseAndDruidicItems, true, true);
                MiscellaneousUtilities.setSpell("earth strike");
                witchesHouse.completeQuest();
                druidicRitual.completeQuest();
                Rs2Walker.walkTo(2944, 3370, 0, 2);
                BankHandler.withdrawQuestItems(runeMysteriesAndNatQuiz, true, true);
                runeMysteries.completeQuest();
                naturalHistoryQuiz.completeQuest();
                Rs2Walker.walkTo(3168, 3489, 0, 2);
                BankHandler.withdrawQuestItems(waterfallPt1, true, true);
                waterfallQuest.completeQuest();
                Rs2Walker.walkTo(2533, 3571, 0, 3);
                BankHandler.withdrawQuestItems(grandTreeItems, true, true);
                MiscellaneousUtilities.setSpell("fire strike");
                Rs2Walker.walkTo(2461, 3379, 0, 3);
                MiscellaneousUtilities.helpFemi();
                Rs2Walker.walkTo(2474, 3438, 0, 3);
                MiscellaneousUtilities.gnomeAgil();
                Rs2Walker.walkTo(2465, 3489, 0, 3);
                grandTree.completeQuest();
                treeGnomeVillage.completeQuest();
                monksFriend.completeQuest();
                plagueCity.completeQuest();
                Rs2Walker.walkTo(3161, 3489, 0);
                fmLeveler.levelUp();
                seaSlug.completeQuest();
                knightsSword.completeQuest();
                fightArena.completeQuest();
                TeaStallStealFletch.run();
                PotTrainer.run();
                GlassBlower.run();
                MiscellaneousUtilities.cowMagerAndRanger();
                MiscellaneousUtilities.getPOH();
                MiscellaneousUtilities.makeAirRunes();
                MiscellaneousUtilities.sardineCooker();*/
                BankHandler.withdrawQuestItems(List.of(
                        new BankHandler.QuestItem("lumbridge teleport", 1, false, false, false),
                        new BankHandler.QuestItem("varrock teleport", 2, false, false, false)
                ), true, true);
                RFDStart.completeQuest();
                System.out.println("shutting down");
                shutdown();
            } catch (Exception ex) {
                shutdown();
                System.out.println("this broke");
                System.out.println(ex.getMessage());
                throw(ex);

            }
        },0, 600, TimeUnit.MILLISECONDS);
        return false;
    }
}
