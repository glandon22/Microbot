package net.runelite.client.plugins.microbot.goon.utils;

import net.runelite.api.Player;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.bank.Rs2Bank;
import net.runelite.client.plugins.microbot.util.gameobject.Rs2GameObject;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.runelite.client.plugins.microbot.util.Global.sleepUntil;

public class GoonBank {
    public static boolean isOpen() {
        if (!Rs2Bank.handleBankPin()) return false;
        return Rs2Widget.hasWidgetText("Rearrange mode", 12, 18, false, true);
    }

    public static boolean openBank() {
        Microbot.status = "Opening bank";
        try {
            if (Microbot.getClient().isWidgetSelected()) {
                Microbot.getMouse().click();
            }

            if (isOpen()) return true;

            final Player player = Microbot.getClient().getLocalPlayer();
            if (player == null) return false;
            WorldPoint anchor = player.getWorldLocation();

            List<TileObject> candidates = Stream.of(
                            Rs2GameObject.findBank(),
                            Rs2GameObject.findGrandExchangeBooth()
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Optional<TileObject> nearestObj = Rs2GameObject.pickClosest(
                    candidates,
                    TileObject::getWorldLocation,
                    anchor
            );

            if (nearestObj.isPresent()) {
                if (!Rs2GameObject.interact(nearestObj.get(), "Bank")) return false;
            } else {
                final Rs2NpcModel banker = Rs2Npc.getBankerNPC();
                if (banker == null || !Rs2Npc.interact(banker, "Bank")) return false;
            }

            return sleepUntil(Rs2Bank::isOpen, 5_000);
        } catch (Exception ex) {
            Microbot.logStackTrace("Rs2Bank", ex);
            return false;
        }
    }
}
