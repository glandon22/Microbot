package net.runelite.client.plugins.microbot.goon.newaccbuilder.quests.grandtree;

import java.util.Arrays;
import java.util.List;

public class TileEntry {
    private final int tileX;
    private final int tileY;
    private final int tileZ;
    private final String id;
    private final Integer walkToX; // Nullable for optional walkTo
    private final Integer walkToY;
    private final Integer walkToZ;

    public TileEntry(int tileX, int tileY, int tileZ, String id, Integer walkToX, Integer walkToY, Integer walkToZ) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileZ = tileZ;
        this.id = id;
        this.walkToX = walkToX;
        this.walkToY = walkToY;
        this.walkToZ = walkToZ;
    }

    // Getters
    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
    public int getTileZ() { return tileZ; }
    public String getId() { return id; }
    public Integer getWalkToX() { return walkToX; }
    public Integer getWalkToY() { return walkToY; }
    public Integer getWalkToZ() { return walkToZ; }

    // The immutable ROOT_LIST variable
    public static final List<TileEntry> ROOT_LIST = Arrays.asList(
            new TileEntry(2468, 9897, 0, "1985", null, null, null),
            new TileEntry(2474, 9898, 0, "1985", null, null, null),
            new TileEntry(2482, 9905, 0, "1986", null, null, null),
            new TileEntry(2491, 9890, 0, "1985", 2487, 9891, 0),
            new TileEntry(2486, 9886, 0, "1985", null, null, null),
            new TileEntry(2468, 9873, 0, "1986", 2469, 9875, 0),
            new TileEntry(2458, 9882, 0, "1986", null, null, null),
            new TileEntry(2456, 9875, 0, "1986", null, null, null),
            new TileEntry(2444, 9879, 0, "1985", null, null, null),
            new TileEntry(2440, 9882, 0, "1986", null, null, null),
            new TileEntry(2445, 9894, 0, "1986", null, null, null),
            new TileEntry(2453, 9894, 0, "1985", null, null, null),
            new TileEntry(2457, 9887, 0, "1985", null, null, null),
            new TileEntry(2466, 9892, 0, "1986", null, null, null),
            new TileEntry(2469, 9891, 0, "1986", null, null, null)
    );
}
