package com.gildedrose;

import java.util.function.Function;
import java.util.stream.Stream;

class GildedRose {

    private static final int DEGRADATION_RATE = 1;
    private static final int MIN_QUALITY = 0;
    private static final int MAX_QUALITY = 50;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        this.items = Stream.of(items)
            .map(item -> getCorrectFunction(item).apply(item))
            .toArray(Item[]::new);
    }

    private Function<Item, Item> getCorrectFunction(final Item item) {
        switch (item.name) {
            case "Aged Brie":
                return updateAgingItem();
            case "Sulfuras, Hand of Ragnaros":
                return updateLegendaryItem();
            case "Backstage passes to a TAFKAL80ETC concert":
                return updateBackstagePass();
            default:
                return item.name.startsWith("Conjured") ? updateConjuredItem() : updateNormalItem();
        }
    }

    /**
     * A normal item degrades linearly in quality as it is nearing its sell-in date.
     * After said date, it degrades twice as fast.
     * It will, however, never go above or below certain constant bounds.
     */
    private Function<Item, Item> updateNormalItem() {
        return current -> new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, DEGRADATION_RATE));
    }

    /**
     * A legendary item does not have to be sold and does not degrade in quality.
     * It is also allowed to have a quality rating outside the normal ranges.
     */
    private Function<Item, Item> updateLegendaryItem() {
        return current -> current;
    }

    /**
     * An aging item increases in quality the older it gets.
     * It will, however, never go above or below certain constant bounds.
     */
    private Function<Item, Item> updateAgingItem() {
        return current -> new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, -DEGRADATION_RATE));
    }

    /**
     * A backstage pass will increase in quality as its sell-in date comes closer to the date of the concert, but
     * will drop to 0 after the concert.
     * <p>
     * Currently, the following rules apply:
     * - Quality increases by 1 when there are more than 10 days left until the concert
     * - Quality increases by 2 when there are 10 days or less until the concert
     * - Quality increases by 3 when there are 5 days or less until the concert
     * - Quality becomes 0 after the concert
     */
    private Function<Item, Item> updateBackstagePass() {
        return current -> new Item(current.name, current.sellIn - 1, calculateQualityForBackstagePasses(current));
    }

    /**
     * A conjured item works similar to a normal item, but degrades twice as fast.
     */
    private Function<Item, Item> updateConjuredItem() {
        return current -> new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, DEGRADATION_RATE * 2));
    }

    private int calculateQualityLinearly(final Item item, final int degradation) {
        return roundQualityWithinLimits(item.quality - (item.sellIn <= 0 ? 2 : 1) * degradation);
    }

    private int calculateQualityForBackstagePasses(final Item item) {
        final int improvement = item.sellIn <= 0 ? -item.quality :
            item.sellIn <= 5 ? 3 :
                item.sellIn <= 10 ? 2 : 1;
        return roundQualityWithinLimits(item.quality + improvement);
    }

    private int roundQualityWithinLimits(final int x) {
        return x < MIN_QUALITY ? MIN_QUALITY : Math.min(x, MAX_QUALITY);
    }
}