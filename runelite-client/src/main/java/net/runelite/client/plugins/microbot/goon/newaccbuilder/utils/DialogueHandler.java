package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.doUntil;
import static net.runelite.client.plugins.microbot.util.Global.sleep;

public class DialogueHandler {
    public static boolean handleConversation(List<String> questions, int timeout) {
        // might add support to not timeout during cutscenes? should test this
        // dont modify the original
        List<String> questionsCopy = new ArrayList<>(questions);
        long lastDialogue = System.currentTimeMillis();
        while (System.currentTimeMillis() - lastDialogue < timeout * 1000L) {
            if (Rs2Dialogue.isInDialogue()) {
                lastDialogue = System.currentTimeMillis();
                if (Rs2Dialogue.hasSelectAnOption()) {
                    List<Widget> dialogueOptions = Rs2Dialogue.getDialogueOptions();
                    for (Widget option : dialogueOptions) {
                        Iterator<String> iterator = questionsCopy.iterator();
                        while (iterator.hasNext()) {
                            if (option.getText().contains(iterator.next())) {
                                Rs2Dialogue.keyPressForDialogueOption(option.getIndex());
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }

                else Rs2Dialogue.clickContinue();
            }
        }
        if (questionsCopy.isEmpty()) {
            System.out.println("we have completed dialogue");
            return true;
        }
        return false;
    }

    public static boolean handleConversationWithCutscene(List<String> questions, int timeout) {
        // might add support to not timeout during cutscenes? should test this
        // dont modify the original
        List<String> questionsCopy = new ArrayList<>(questions);
        long lastDialogue = System.currentTimeMillis();
        while (System.currentTimeMillis() - lastDialogue < timeout * 1000L) {
            if (Rs2Dialogue.isInDialogue() || Rs2Dialogue.isInCutScene()) {
                lastDialogue = System.currentTimeMillis();
            }

            if (Rs2Dialogue.isInDialogue()) {
                if (Rs2Dialogue.hasSelectAnOption()) {
                    List<Widget> dialogueOptions = Rs2Dialogue.getDialogueOptions();
                    for (Widget option : dialogueOptions) {
                        Iterator<String> iterator = questionsCopy.iterator();
                        while (iterator.hasNext()) {
                            if (option.getText().contains(iterator.next())) {
                                Rs2Dialogue.keyPressForDialogueOption(option.getIndex());
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }

                else Rs2Dialogue.clickContinue();
            }
        }
        if (questionsCopy.isEmpty()) {
            System.out.println("we have completed dialogue");
            return true;
        }
        return false;
    }

    public static void talkToNPC(String name, List<String> lines, int timeout) {
        doUntil(
                Rs2Dialogue::isInDialogue,
                () -> Rs2Npc.interact(name, "talk-to"),
                5000,
                100000
        );
        Microbot.log("Sucessfully started conversation with " + name);
        DialogueHandler.handleConversation(lines, timeout);
    }

    public static void talkToNPCCutscene(String name, List<String> lines, int timeout) {
        Rs2Npc.interact(name, "talk-to");
        DialogueHandler.handleConversationWithCutscene(lines, timeout);
    }

    //last lines is to prevent selecting "goodbye" first and missing everything
    public static void selectAllDialogueOptions(String name, ArrayList<String> lastLines, int timeout) {
        Rs2Npc.interact(name, "talk-to");
        HashSet<String> optionsSeen = new HashSet<>();
        long lastDialogue = System.currentTimeMillis();
        while (System.currentTimeMillis() - lastDialogue < timeout * 1000L) {
            System.out.println("1");
            if (Rs2Dialogue.isInDialogue()) {
                System.out.println("2");
                lastDialogue = System.currentTimeMillis();
                if (Rs2Dialogue.hasSelectAnOption()) {
                    System.out.println("3");
                    List<Widget> dialogueOptions = Rs2Dialogue.getDialogueOptions();
                    Widget exitOption = null;
                    boolean sawNewOption = false;
                    for (Widget option : dialogueOptions) {
                        System.out.println("4");
                        // new dialogue optoin to use
                        if (!optionsSeen.contains(option.getText())) {
                            if (!lastLines.isEmpty() && option.getText().contains(lastLines.get(0))) exitOption = option;
                            Rs2Dialogue.keyPressForDialogueOption(option.getIndex());
                            optionsSeen.add(option.getText());
                            sleep(600);
                            break;
                        }
                    }
                    System.out.println("5");

                    //i have gone through all options and i have an available exit option
                    if (exitOption != null && !sawNewOption) {
                        System.out.println("6");
                        Rs2Dialogue.keyPressForDialogueOption(exitOption.getIndex());
                        optionsSeen.add(exitOption.getText());
                        System.out.println("7" + lastLines.size());
                        lastLines.remove(0);
                        System.out.println("8");
                        sleep(600);

                    }
                }

                else Rs2Dialogue.clickContinue();
            }
        }
    }
}
