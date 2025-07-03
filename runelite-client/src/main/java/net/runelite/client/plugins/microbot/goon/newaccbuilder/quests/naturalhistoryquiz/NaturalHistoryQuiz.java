package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.naturalhistoryquiz;

import lombok.Value;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class NaturalHistoryQuiz {
    @Value
    private static class Exhibit {
        int id;
        int x;
        int y;
    }
     private static final HashMap<String, String> dialogue = new HashMap<>();

     // Static initializer block to populate the HashMap
     static {
     // Lizard facts
     dialogue.put("How does a lizard regulate body heat?", "Sunlight.");
     dialogue.put("Who discovered how to kill lizards?", "The Slayer Masters.");
     dialogue.put("How many eyes does a lizard have?", "Three.");
     dialogue.put("What order do lizards belong to?", "Squamata.");
     dialogue.put("What happens when a lizard becomes cold?", "It becomes sleepy.");
     dialogue.put("Lizard skin is made of the same substance as?", "Hair.");

     // Tortoise facts
     dialogue.put("What is the name of the oldest tortoise ever recorded?", "Mibbiwocket.");
     dialogue.put("What is a tortoise's favourite food?", "Vegetables.");
     dialogue.put("Name the explorer who discovered the world's oldest tortoise.", "Admiral Bake.");
     dialogue.put("How does the tortoise protect itself?", "Hard shell.");
     dialogue.put("If a tortoise had twenty rings on its shell, how old would it be?", "Twenty years.");
     dialogue.put("Which race breeds tortoises for battle?", "Gnomes.");

     // Dragon facts
     dialogue.put("What is considered a delicacy by dragons?", "Runite.");
     dialogue.put("What is the best defence against a dragon's attack?", "Anti dragon-breath shield.");
     dialogue.put("How long do dragons live?", "Unknown.");
     dialogue.put("Which of these is not a type of dragon?", "Elemental.");
     dialogue.put("What is the favoured territory of a dragon?", "Old battle sites.");
     dialogue.put("Approximately how many feet tall do dragons stand?", "Twelve.");

     // Wyvern facts
     dialogue.put("How did the wyverns die out?", "Climate change.");
     dialogue.put("How many legs does a wyvern have?", "Two.");
     dialogue.put("Where have wyvern bones been found?", "Asgarnia.");
     dialogue.put("Which genus does the wyvern theoretically belong to?", "Reptiles.");
     dialogue.put("What are the wyverns' closest relations?", "Dragons.");
     dialogue.put("What is the ambient temperature of wyvern bones?", "Below room temperature.");

     // Snail facts
     dialogue.put("What is special about the shell of the giant Morytanian snail?", "It is resistant to acid.");
     dialogue.put("How do Morytanian snails capture their prey?", "Spitting acid.");
     dialogue.put("Which of these is a snail byproduct?", "Fireproof oil.");
     dialogue.put("What does 'Achatina Acidia' mean?", "Acid-spitting snail.");
     dialogue.put("How do snails move?", "Contracting and stretching.");
     dialogue.put("What is the 'trapdoor', which snails use to cover the entrance to their shells called?", "An operculum.");

     // Snake facts
     dialogue.put("What is snake venom adapted from?", "Stomach acid.");
     dialogue.put("Aside from their noses, what do snakes use to smell?", "Tongue.");
     dialogue.put("If a snake sticks its tongue out at you, what is it doing?", "Seeing how you smell.");
     dialogue.put("If some snakes use venom to kill their prey, what do other snakes use?", "Constriction.");
     dialogue.put("Lizards and snakes belong to the same order - what is it?", "Squamata.");
     dialogue.put("Which habitat do snakes prefer?", "Anywhere.");

     // Slug facts
     dialogue.put("We assume that sea slugs have a stinging organ on their soft skin - what is it called?", "Nematocysts.");
     dialogue.put("Why has the museum never examined a live sea slug?", "The researchers keep vanishing.");
     dialogue.put("What do we think the sea slug feeds upon?", "Seaweed.");
     dialogue.put("What are the two fangs presumed to be used for?", "Defense or display.");
     dialogue.put("Off of which coastline would you find sea slugs?", "Ardougne.");
     dialogue.put("In what way are sea slugs similar to snails?", "They have a hard shell.");

     // Monkey facts
     dialogue.put("Which type of primates do monkeys belong to?", "Simian.");
     dialogue.put("Which have the lighter colour: Karamjan or Harmless monkeys?", "Harmless.");
     dialogue.put("Monkeys love bananas. What else do they like to eat?", "Bitternuts.");
     dialogue.put("There are two known families of monkeys. One is Karamjan, the other is...?", "Harmless.");
     dialogue.put("What colour mohawk do Karamjan monkeys have?", "Red.");
     dialogue.put("What have Karamjan monkeys taken a deep dislike to?", "Seaweed.");

     // Kalphite facts
     dialogue.put("Kalphites are ruled by a...?", "Pasha.");
     dialogue.put("What is the lowest caste in kalphite society?", "Worker.");
     dialogue.put("What are the armoured plates on a kalphite called?", "Lamellae.");
     dialogue.put("Are kalphites carnivores, herbivores or omnivores?", "Carnivores.");
     dialogue.put("What are kalphites assumed to have evolved from?", "Scarab beetles.");
     dialogue.put("Name the prominent figure in kalphite mythology?", "Scabaras.");

     // Terrorbird facts
     dialogue.put("What is a terrorbird's preferred food?", "Anything.");
     dialogue.put("Who use terrorbirds as mounts?", "Gnomes.");
     dialogue.put("Where do terrorbirds get most of their water?", "Eating plants.");
     dialogue.put("How many claws do terrorbirds have?", "Four.");
     dialogue.put("What do terrorbirds eat to aid digestion?", "Stones.");
     dialogue.put("How many teeth do terrorbirds have?", "0.");

     // Penguin facts
     dialogue.put("Which sense do penguins rely on when hunting?", "Sight.");
     dialogue.put("Which skill seems unusual for the penguins to possess?", "Planning.");
     dialogue.put("How do penguins keep warm?", "A layer of fat.");
     dialogue.put("What is the preferred climate for penguins?", "Cold.");
     dialogue.put("Describe the behaviour of penguins?", "Social.");
     dialogue.put("When do penguins fast?", "During breeding.");

     // Mole facts
     dialogue.put("What habitat do moles prefer?", "Subterranean.");
     dialogue.put("Why are moles considered to be an agricultural pest?", "They dig holes.");
     dialogue.put("Who discovered giant moles?", "Wyson the Gardener.");
     dialogue.put("What would you call a group of young moles?", "A labour.");
     dialogue.put("What is a mole's favourite food?", "Insects and other invertebrates.");
     dialogue.put("Which family do moles belong to?", "The Talpidae family.");

     // Camel facts
     dialogue.put("What is produced by feeding chilli to a camel?", "Toxic dung.");
     dialogue.put("If an ugthanki has one, how many does a bactrian have?", "Two.");
     dialogue.put("Camels: herbivore, carnivore or omnivore?", "Omnivore.");
     dialogue.put("What is the usual mood for a camel?", "Annoyed.");
     dialogue.put("Where would you find an ugthanki?", "Al Kharid.");
     dialogue.put("Which camel byproduct is known to be very nutritious?", "Milk.");

     // Leech facts
     dialogue.put("What is the favoured habitat of leeches?", "Water.");
     dialogue.put("What shape is the inside of a leech's mouth?", "'Y'-shaped.");
     dialogue.put("Which of these is not eaten by leeches?", "Apples.");
     dialogue.put("What contributed to the giant growth of Morytanian leeches?", "Environment.");
     dialogue.put("What is special about Morytanian leeches?", "They attack by jumping.");
     dialogue.put("How does a leech change when it feeds?", "It doubles in size.");
     }

    private void orlandoSmith() {
        Rs2Walker.walkTo(1759, 4955, 0, 2);
        DialogueHandler.talkToNPC("orlando smith", List.of("Sure thing."), 5);
    }

    private void handleExhibits() {
        List<Exhibit> exhibits = List.of(
                //west
                new Exhibit(24609, 1737, 4962),
                new Exhibit(24610, 1744, 4962),
                new Exhibit(24611, 1735, 4958),
                new Exhibit(24612, 1742, 4958),

                //north
                new Exhibit(24605, 1743, 4977),
                new Exhibit(24606, 1753, 4977),
                new Exhibit(24607, 1768, 4977),
                new Exhibit(24608, 1778, 4977),

                //east
                new Exhibit(24613, 1776, 4962),
                new Exhibit(24614, 1783, 4962),
                new Exhibit(24615, 1774, 4958),
                new Exhibit(24616, 1781, 4958),

                //south
                new Exhibit(24617, 1755, 4940),
                new Exhibit(24618, 1761, 4938)

        );
        for (Exhibit exhibit : exhibits) {
            Rs2Walker.walkTo(exhibit.x, exhibit.y, 0, 4);
            Rs2GameObject.interact(exhibit.id);
            // he says bonza something something when you get enough questions right
            while (!Rs2Widget.hasWidget("Bonza")) {
                System.out.println("Currently in a dialogue: " + exhibit.id);
                // after answering a question you have some dialogue to go through
                if (Rs2Dialogue.hasContinue()) {
                    Rs2Dialogue.clickContinue();
                    //script may be progrossing too fast and missing this bonza dialogue. keep an eye on it
                    sleepUntil(() -> !Rs2Dialogue.hasContinue() || Rs2Widget.hasWidget("Bonza"), 3000);
                    continue;
                }

                Widget questionWidget = Rs2Widget.getWidget(34930716);
                if (questionWidget == null) continue;
                String question = questionWidget.getText();
                String answer = dialogue.get(question);
                if (answer == null) {
                    System.out.println("Encountered an unexpected question: " + question);
                    continue;
                }
                Widget answerWidget = Rs2Widget.findWidget(answer, false);
                if (answerWidget == null) {
                    System.out.println("could not find configured answer: " + answer);
                }
                Rs2Widget.clickWidget(answerWidget);
                sleep(900);
            }
            System.out.println("Completed a dialogue");
            Rs2Dialogue.clickContinue();
            sleep(600);
        }
    }

    public void completeQuest() {
        orlandoSmith();
        handleExhibits();
        orlandoSmith();
    }
}
