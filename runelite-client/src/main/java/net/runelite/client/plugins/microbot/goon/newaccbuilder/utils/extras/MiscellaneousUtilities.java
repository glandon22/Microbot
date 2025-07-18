package net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.extras;

import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.bossing.giantmole.enums.State;
import net.runelite.client.plugins.microbot.goon.newaccbuilder.utils.DialogueHandler;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;
import net.runelite.client.plugins.microbot.util.security.Login;
import net.runelite.client.plugins.microbot.util.walker.Rs2Walker;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.List;

import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;
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
}
