package net.runelite.client.plugins.microbot.goon.utils;

import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.MenuAction;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.util.grandexchange.*;
import net.runelite.client.plugins.microbot.util.inventory.Rs2Inventory;
import net.runelite.client.plugins.microbot.util.keyboard.Rs2Keyboard;
import net.runelite.client.plugins.microbot.util.menu.NewMenuEntry;
import net.runelite.client.plugins.microbot.util.widget.Rs2Widget;
import org.apache.commons.lang3.tuple.Pair;

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

        Predicate<GrandExchangeRequest> DEFAULT_PREDICATE = gxr -> gxr.getItemName() != null && !gxr.getItemName().isBlank() && request.getQuantity() > 0;
        Predicate<GrandExchangeRequest> PRICE_PREDICATE = gxr -> gxr.getPrice() > 0;

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
        return processOfferGoon(request);
    }

    /**
     * Creates and processes a {@link GrandExchangeRequest} to buy an item on the Grand Exchange.
     * <p>
     * This method constructs a {@code BUY} type request using the specified item name, price, and quantity,
     * and delegates the logic to execute the buy action.
     *
     * @param itemName the name of the item to buy
     * @param price the price per item in coins
     * @param quantity the number of items to buy
     * @param toBank withdraw to bank or not
     * @return {@code true} if the buy offer was successfully placed; {@code false} otherwise
     */
    public static boolean buyItem(String itemName, int price, int quantity, boolean toBank)
    {
        GrandExchangeRequest request = GrandExchangeRequest.builder()
                .action(GrandExchangeAction.BUY)
                .itemName(itemName)
                .price(price)
                .quantity(quantity)
                .toBank(toBank)
                .build();
        return Rs2GrandExchange.processOffer(request);
    }


    /**
     * Creates and processes a {@link GrandExchangeRequest} to buy an item on the Grand Exchange.
     * <p>
     * This method constructs a {@code BUY} type request using the specified item name, price, and quantity,
     * and delegates the logic to  to execute the buy action.
     *
     * @param itemName the name of the item to buy
     * @param priceAboveAsk the percent above current ask price per item in coins
     * @param quantity the number of items to buy
     * @return {@code true} if the buy offer was successfully placed; {@code false} otherwise
     */
    public static boolean buyItemDynamic(String itemName, int priceAboveAsk, int quantity, boolean buyAndCollect, boolean toBank)
    {
        int offerPrice = (int)(getOfferPrice() * priceAboveAsk) / 100;
        GrandExchangeRequest request = GrandExchangeRequest.builder()
                .action(GrandExchangeAction.BUY)
                .itemName(itemName)
                .price(offerPrice)
                .quantity(quantity)
                .buyAndCollect(buyAndCollect)
                .toBank(toBank)
                .build();
        return Rs2GrandExchange.processOffer(request);
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
        return processOfferGoon(request);
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

    private static boolean collectGoonV2(GrandExchangeRequest request) {
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

    public static boolean processOfferGoon(GrandExchangeRequest request) {
        if (!isValidRequest(request) || !useGrandExchange()) {
            return false;
        }

        boolean success = false;

        switch (request.getAction()) {
            case COLLECT:
                success = collectGoonV2(request);
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
                System.out.println("price: " + request.getPrice());
                System.out.println("percent: " + request.getPercent());
                int defaultOffer = getOfferPrice();
                System.out.println("default offer: " + defaultOffer);
                float offerMod = 1 + (float) request.getPercent() / 100;
                System.out.println("offer mod: " + 1 + request.getPercent() / 100);
                purchasePrice = request.getPrice() != 0 ? request.getPrice() : (int) (defaultOffer * offerMod);
                System.out.println("Setting purchase price: " + purchasePrice);
                doUntil(
                        () -> Objects.requireNonNull(Rs2Widget.getWidget(465, 26).getChild(41)).getText().contains(addCommasToNumberString(String.valueOf(purchasePrice))),
                        () -> setPrice(purchasePrice),
                        3000,
                        100000
                );
                System.out.println("setting purchase quantity");
                setQuantity(request.getQuantity());
                System.out.println("confirming");
                confirm();
                success = sleepUntil(() -> !isOfferScreenOpen());
                if (request.isBuyAndCollect()) {
                    System.out.println("collecting item");
                    success = collectGoonV2(request);
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
                    //adjustPriceByPercent(request.getPercent());
                    System.out.println("need to implement this still");
                }
                if (request.getQuantity() > 0) {
                    setQuantity(request.getQuantity());
                }

                confirm();
                success = sleepUntil(() -> !isOfferScreenOpen());
                break;
        }

        if (success && request.isCloseAfterCompletion()) {
            closeExchange();
        }

        return success;
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
