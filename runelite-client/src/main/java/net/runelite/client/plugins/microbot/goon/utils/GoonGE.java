package net.runelite.client.plugins.microbot.goon.utils;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.MenuAction;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.dialogues.Rs2Dialogue;
import net.runelite.client.plugins.microbot.util.grandexchange.*;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.menu.NewMenuEntry;
import net.runelite.client.plugins.microbot.util.misc.Rs2UiHelper;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.event.Level;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.runelite.client.plugins.microbot.util.Global.*;
import static net.runelite.client.plugins.microbot.util.Global.sleep;
import static net.runelite.client.plugins.microbot.util.grandexchange.Rs2GrandExchange.*;

public class GoonGE {
    private static boolean useGrandExchange()
    {
        return isOpen() || openExchange() || walkToGrandExchange();
    }

    private static boolean isValidRequest(GrandExchangeRequest request)
    {
        if (request == null || request.getAction() == null)
        {
            return false;
        }

        Predicate<GrandExchangeRequest> DEFAULT_PREDICATE = gxr -> gxr.getItemName() != null && !gxr.getItemName().isBlank();
        Predicate<GrandExchangeRequest> PRICE_PREDICATE = gxr -> gxr.getPrice() > 0 || gxr.getPercent() != 0;

        switch (request.getAction())
        {
            case BUY:
                return DEFAULT_PREDICATE.test(request);
            case SELL:
                Predicate<GrandExchangeRequest> combined = DEFAULT_PREDICATE.and(PRICE_PREDICATE);
                return combined.test(request);
            case COLLECT:
                return true;
            default:
                return false;
        }
    }

    static Widget getSlot(GrandExchangeSlots slot)
    {
        switch (slot)
        {
            case ONE:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 7);
            case TWO:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 8);
            case THREE:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 9);
            case FOUR:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 10);
            case FIVE:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 11);
            case SIX:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 12);
            case SEVEN:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 13);
            case EIGHT:
                return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 14);
            default:
                return null;
        }
    }

    static Widget getOfferBuyButton(GrandExchangeSlots slot)
    {
        return getSlotChild(slot, 0);
    }

    private static Widget getSlotChild(GrandExchangeSlots slot, int childIndex)
    {
        return getWidgetChild(getSlot(slot), childIndex);
    }

    private static Widget getWidgetChild(Widget parent, int childIndex)
    {
        return Optional.ofNullable(parent)
                .map(p -> p.getChild(childIndex))
                .orElse(null);
    }

    public static boolean buyItem(String itemName, int price, int quantity, boolean buyAndCollect, boolean toBank, boolean exact)
    {
        GrandExchangeRequest request = GrandExchangeRequest.builder()
                .action(GrandExchangeAction.BUY)
                .itemName(itemName)
                .price(price)
                .quantity(quantity)
                .buyAndCollect(buyAndCollect)
                .toBank(toBank)
                .exact(exact)
                .build();
        return processOffer(request);
    }

    public static boolean buyItemDynamic(String itemName, int priceAboveAsk, int quantity, boolean buyAndCollect, boolean toBank, boolean exact)
    {
        GrandExchangeRequest request = GrandExchangeRequest.builder()
                .action(GrandExchangeAction.BUY)
                .itemName(itemName)
                .percent(priceAboveAsk)
                .quantity(quantity)
                .buyAndCollect(buyAndCollect)
                .toBank(toBank)
                .exact(exact)
                .build();
        return processOffer(request);
    }

    private static void viewOffer(Widget widget)
    {
        if (widget == null)
        {
            return;
        }
        // MenuEntryImpl(getOption=View offer, getTarget=, getIdentifier=1, getType=CC_OP, getParam0=2, getParam1=30474247, getItemId=-1, isForceLeftClick=false, getWorldViewId=-1, isDeprioritized=false)
        NewMenuEntry menuEntry = new NewMenuEntry("View offer", "", 1, MenuAction.CC_OP, 2, widget.getId(), false);
        Rectangle bounds = widget.getBounds();
        Microbot.doInvoke(menuEntry, bounds);
    }

    private static boolean collect(GrandExchangeRequest request) {
        GrandExchangeOffer[] offers = Microbot.getClient().getGrandExchangeOffers();

        while (true) {
            GrandExchangeSlots[] slots = GrandExchangeSlots.values();
            for (int i = 0; i < slots.length; i++) {
                GrandExchangeSlots slot = slots[i];
                if (request.getSlot() == null || request.getSlot() == slot) {
                    Widget offerSlot = getSlot(slot);
                    if (offerSlot == null) continue;
                    Widget itemNameWidget = offerSlot.getChild(19);
                    if (itemNameWidget == null || itemNameWidget.getText() == null) continue;
                    String geItemName = itemNameWidget.getText().toLowerCase();
                    System.out.println("probably erroring out here");
                    String parsedTargetItemName = request.getItemName().length() > 10 ? request.getItemName().substring(0, 10).toLowerCase() + "..." : request.getItemName().toLowerCase();
                    System.out.println(geItemName + " " + parsedTargetItemName + " " + request.getItemName().toLowerCase());
                    boolean doesItemMatch =
                            // Check for exact match
                            (request.isExact() && (geItemName.equalsIgnoreCase(parsedTargetItemName) || geItemName.equalsIgnoreCase(request.getItemName().toLowerCase())))
                                    ||
                                    // Check for partial match
                                    (!request.isExact() && (geItemName.contains(parsedTargetItemName) || geItemName.contains(request.getItemName().toLowerCase())));
                    System.out.println("doesit " + doesItemMatch);
                    System.out.println(request.isExact());
                    System.out.println(geItemName.equalsIgnoreCase(parsedTargetItemName));
                    System.out.println(geItemName.equalsIgnoreCase(request.getItemName().toLowerCase()));
                    System.out.println(geItemName.contains(parsedTargetItemName));
                    System.out.println(geItemName.contains(request.getItemName().toLowerCase()));
                    System.out.println("my ge item: " + geItemName);
                    System.out.println(parsedTargetItemName);
                    System.out.println(request.getItemName().toLowerCase());
                    if (!doesItemMatch) continue;
                    if (offers[i].getState() != GrandExchangeOfferState.BOUGHT) continue;
                    return collectAll(request.isToBank());
                }
            }
        }
    }

    static Widget getOfferContainer()
    {
        return Rs2Widget.getWidget(InterfaceID.GE_OFFERS, 26);
    }

    static Widget getPricePerItemButton_X()
    {
        return getOfferChild(12);
    }

    private static Widget getOfferChild(int childIndex)
    {
        return getWidgetChild(getOfferContainer(), childIndex);
    }

    static int getOfferPrice()
    {
        return Microbot.getVarbitValue(4398);
    }

    private static void setPrice(int price)
    {
        if (price != getOfferPrice())
        {
            Widget pricePerItemButtonX = getPricePerItemButton_X();
            Microbot.getMouse().click(pricePerItemButtonX.getBounds());
            sleepUntil(() -> Rs2Widget.getWidget(InterfaceID.Chatbox.MES_TEXT2) != null); //GE Enter Price
            sleep(600, 1000);
            setChatboxValue(price);
            sleep(500, 750);
            Rs2Keyboard.enter();
            sleep(1000);
        }
    }

    static int getOfferQuantity()
    {
        return Microbot.getVarbitValue(4396);
    }

    static Widget getQuantityButton_X()
    {
        return getOfferChild(7);
    }

    private static void setQuantity(int quantity)
    {
        if (quantity != getOfferQuantity())
        {
            Widget quantityButtonX = getQuantityButton_X();
            Microbot.getMouse().click(quantityButtonX.getBounds());
            sleepUntil(() -> Rs2Widget.getWidget(InterfaceID.Chatbox.MES_TEXT2) != null); //GE Enter Price/Quantity
            sleep(600, 1000);
            setChatboxValue(quantity);
            sleep(500, 750);
            Rs2Keyboard.enter();
            sleep(1000);
        }
    }
    static Widget getConfirm()
    {
        var parent = getOfferContainer();

        return Rs2Widget.findWidget("Confirm", Arrays.stream(parent.getDynamicChildren()).collect(Collectors.toList()), true);
    }


    private static void confirm()
    {
        Rs2Widget.clickWidget(getConfirm());
        sleepUntil(() -> Rs2Widget.hasWidget("Your offer is much"), 2000);
        if (Rs2Widget.hasWidget("Your offer is much"))
        {
            Rs2Widget.clickWidget("Yes");
        }
    }

    static boolean isOfferTextVisible()
    {
        return Rs2Widget.isWidgetVisible(ComponentID.GRAND_EXCHANGE_OFFER_DESCRIPTION);
    }

    public static boolean processOffer(GrandExchangeRequest request) {
        if (!isValidRequest(request) || !useGrandExchange()) {
            return false;
        }

        boolean success = false;

        switch (request.getAction()) {
            case COLLECT:
                success = collect(request);
                break;

            case BUY:
                Widget buyOffer = getOfferBuyButton(
                        request.getSlot() != null ? request.getSlot() : getAvailableSlot());
                if (buyOffer == null) break;
                System.out.println("starting a buy offer");
                doUntil(
                        () -> Rs2Widget.hasWidgetText("Start typing the name of an item to search for it", 162, 51, false),
                        () -> Rs2Widget.clickWidgetFast(buyOffer),
                        1500, 100000
                );

                String searchName = request.getItemName().substring(0, Math.min(25, request.getItemName().length())); // Grand Exchange item names are limited to 25 characters.
                System.out.println("searching for: " + searchName);
                doUntil(
                        () -> Rs2Widget.hasWidgetText(searchName, 162, 43, false),
                        () -> Rs2Keyboard.typeString(request.getItemName()),
                        3000,
                        100000
                );
                System.out.println("waiting for search result");
                sleepUntil(() -> getSearchResultWidget(request.getItemName(), request.isExact()) != null, 2200);

                Pair<Widget, Integer> itemResult = getSearchResultWidget(request.getItemName(), request.isExact());
                if (itemResult == null) break;

                System.out.println("clicking search result");
                doUntil(
                        () -> !Rs2Widget.hasWidgetText("choose an item...", 465, 26, false),
                        () -> Rs2Widget.clickWidgetFast(itemResult.getLeft(), itemResult.getRight(), 1),
                        3000,
                        100000
                );

                int purchasePrice;
                int defaultOffer = getOfferPrice();
                float offerMod = 1 + (float) request.getPercent() / 100;
                purchasePrice = request.getPrice() != 0 ? request.getPrice() : (int) (defaultOffer * offerMod);
                doUntil(
                        () -> Objects.requireNonNull(Rs2Widget.getWidget(465, 26).getChild(41)).getText().contains(addCommasToNumberString(String.valueOf(purchasePrice))),
                        () -> setPrice(purchasePrice),
                        3000,
                        100000
                );
                setQuantity(request.getQuantity());
                confirm();
                success = sleepUntil(() -> !isOfferScreenOpen());
                if (request.isBuyAndCollect()) {
                    System.out.println("collecting item");
                    success = collect(request);
                }
                break;

            case SELL:
                if (!Rs2Inventory.hasItem(request.getItemName(), request.isExact())) break;
                if (getAvailableSlots().length == 0) break;

                if (!Rs2Inventory.interact(request.getItemName(), "Offer", request.isExact())) break;

                sleepUntil(GoonGE::isOfferTextVisible);

                if (request.getPrice() > 0) {
                    setPrice(request.getPrice());
                }
                if (request.getPercent() != 0) {
                    Widget adjustXWidget = request.getPercent() > 0
                            ? getPricePerItemButton_PlusXPercent()
                            : getPricePerItemButton_MinusXPercent();
                    if (adjustXWidget == null) {
                        Microbot.log("Failed tofind the price change button while selling " + request.getItemName(), Level.WARN);
                        break;
                    }
                    int result = Rs2UiHelper.extractNumber(adjustXWidget.getText());
                    if (result != Math.abs(request.getPercent())) {
                        System.out.println("inside888");
                        NewMenuEntry menuEntry = new NewMenuEntry("Customise", "", 2, MenuAction.CC_OP, request.getPercent() < 0 ? 14 : 15, adjustXWidget.getId(), false);
                        Rectangle bounds = adjustXWidget.getBounds() != null && Rs2UiHelper.isRectangleWithinCanvas(adjustXWidget.getBounds()) ? adjustXWidget.getBounds() : Rs2UiHelper.getDefaultRectangle();
                        Microbot.doInvoke(menuEntry, bounds);
                        sleepUntil(() -> Rs2Widget.hasWidget("Set a percentage to decrease/increase"), 2000);
                        Rs2Keyboard.typeString(Integer.toString(Math.abs(request.getPercent())));
                        Rs2Keyboard.enter();
                        sleepUntil(() -> {
                            Widget updatedWidget = request.getPercent() > 0
                                    ? getPricePerItemButton_PlusXPercent()
                                    : getPricePerItemButton_MinusXPercent();
                            return updatedWidget != null && Rs2UiHelper.extractNumber(updatedWidget.getText()) != result;
                        }, 2000);
                    }
                    Rs2Widget.clickWidget(adjustXWidget);
                }
                if (request.getQuantity() > 0) {
                    setQuantity(request.getQuantity());
                }

                confirm();
                success = sleepUntil(() -> !isOfferScreenOpen());
                if (request.isSellAndCollect()) {
                    Microbot.log("Collecting " + request.getItemName() + " after selling.");
                    collect(request);
                }
                break;
        }

        if (success && request.isCloseAfterCompletion()) {
            closeExchange();
        }

        return success;
    }

    static Widget getPricePerItemButton_PlusXPercent()
    {
        return getOfferChild(15);
    }

    static Widget getPricePerItemButton_MinusXPercent()
    {
        return getOfferChild(14);
    }
    static Widget getItemPriceWidget()
    {
        return getOfferChild(41);
    }
    static int getItemPrice()
    {
        try
        {
            return Integer.parseInt(getItemPriceWidget().getText().replace(" coins", ""));
        }
        catch (NumberFormatException e)
        {
            Microbot.log("Invailid item price format in Grand Exchange: " + getItemPriceWidget().getText());
            return -1;
        }
    }
    static boolean hasOfferPriceChanged(int basePrice)
    {
        return basePrice != getItemPrice();
    }


    private boolean adjustPriceByPercent(int percent) {
        int absPercent = Math.abs(percent);
        int basePrice = Microbot.getVarbitValue(VarbitID.GE_NEWOFFER_TYPE);
        boolean isIncrease = percent > 0;
        Widget adjustXWidget = isIncrease
                ? getPricePerItemButton_PlusXPercent()
                : getPricePerItemButton_MinusXPercent();

        if (adjustXWidget == null)
        {
            Microbot.log("Unable to find +-X% button widget.");
            return false;
        }

        int currentPercent = Rs2UiHelper.extractNumber(adjustXWidget.getText());
        if (currentPercent != absPercent)
        {
            if (currentPercent == -1)
            {
                Rs2Widget.clickWidget(adjustXWidget);
            }
            else
            {
//					MenuEntryImpl(getOption=Customise, getTarget=, getIdentifier=2, getType=CC_OP, getParam0=14, getParam1=30474266, getItemId=-1, isForceLeftClick=false, getWorldViewId=-1, isDeprioritized=false)
//					MenuEntryImpl(getOption=Customise, getTarget=, getIdentifier=2, getType=CC_OP, getParam0=15, getParam1=30474266, getItemId=-1, isForceLeftClick=false, getWorldViewId=-1, isDeprioritized=false)
                NewMenuEntry menuEntry = new NewMenuEntry("Customise", "", 2, MenuAction.CC_OP, isIncrease ? 15 : 14, adjustXWidget.getId(), false);
                Rectangle bounds = adjustXWidget.getBounds() != null && Rs2UiHelper.isRectangleWithinCanvas(adjustXWidget.getBounds()) ? adjustXWidget.getBounds() : Rs2UiHelper.getDefaultRectangle();
                Microbot.doInvoke(menuEntry, bounds);
            }

            sleepUntil(() -> Rs2Widget.hasWidget("Set a percentage to decrease/increase"), 2000);
            Rs2Keyboard.typeString(Integer.toString(absPercent));
            Rs2Keyboard.enter();
            sleepUntil(() -> {
                Widget updatedWidget = isIncrease
                        ? getPricePerItemButton_PlusXPercent()
                        : getPricePerItemButton_MinusXPercent();
                return updatedWidget != null && Rs2UiHelper.extractNumber(updatedWidget.getText()) != currentPercent;
            }, 2000);
        }

        Rs2Widget.clickWidget(adjustXWidget);
        return sleepUntil(() -> hasOfferPriceChanged(basePrice), 2000);
    }

    public static String addCommasToNumberString(String number) {
        // Handle null or empty input
        if (number == null || number.isEmpty()) {
            return number;
        }

        // Remove any existing commas or whitespace
        number = number.replaceAll("[,\\s]", "");

        // Validate input is a valid integer string
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input must represent a valid integer");
        }

        // Handle negative numbers
        boolean isNegative = number.startsWith("-");
        String absNumber = isNegative ? number.substring(1) : number;

        // Add commas every 3 digits from the right
        StringBuilder result = new StringBuilder();
        int length = absNumber.length();

        for (int i = 0; i < length; i++) {
            result.append(absNumber.charAt(i));
            // Add comma if: not the last digit, and position is 3rd from end or every 3 digits after
            if (i < length - 1 && (length - i - 1) % 3 == 0) {
                result.append(",");
            }
        }

        // Add negative sign back if needed
        return isNegative ? "-" + result.toString() : result.toString();
    }
}
