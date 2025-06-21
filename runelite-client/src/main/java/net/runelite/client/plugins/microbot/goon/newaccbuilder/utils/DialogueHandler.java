package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils;

import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;

import java.util.Iterator;
import java.util.List;

public class DialogueHandler {
    public static boolean handleConversation(List<String> questions, int timeout) {
        long startTime = System.currentTimeMillis();
        // doesnt seem like the timeout works
        while (System.currentTimeMillis() - startTime < timeout * 1000L) {
            if (questions.isEmpty() && !Rs2Dialogue.isInDialogue()) return true;

            if (Rs2Dialogue.isInDialogue()) {
                if (Rs2Dialogue.hasSelectAnOption()) {
                    List<Widget> dialogueOptions = Rs2Dialogue.getDialogueOptions();
                    for (Widget option : dialogueOptions) {
                        Iterator<String> iterator = questions.iterator();
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

        return false;
    }
}
