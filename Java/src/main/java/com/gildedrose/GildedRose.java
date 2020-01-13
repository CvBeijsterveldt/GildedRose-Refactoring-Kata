package com.gildedrose;

import java.util.stream.Stream;

class GildedRose {

    private static final int QUALITY_CHANGE_RATE = 1;
    private static final int MIN_QUALITY = 0;
    private static final int MAX_QUALITY = 50;

    Item[] items;

    public GildedRose(final Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        this.items = Stream.of(items)
            .map(GildedRose::updateItem)
            .toArray(Item[]::new);
    }

    private static Item updateItem(final Item item) {
        switch (item.name) {
            case "Aged Brie":
                return updateAgingItem(item);
            case "Sulfuras, Hand of Ragnaros":
                return updateLegendaryItem(item);
            case "Backstage passes to a TAFKAL80ETC concert":
                return updateBackstagePass(item);
            default:
                return item.name.startsWith("Conjured") ?
                    updateConjuredItem(item) :
                    updateNormalItem(item);
        }
    }

    /**
     * A normal item degrades linearly in quality as it is nearing its sell-in date.
     * After said date, it degrades twice as fast.
     * It will, however, never go above or below certain constant bounds.
     */
    private static Item updateNormalItem(final Item current) {
        return new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, -QUALITY_CHANGE_RATE));
    }

    /**
     * A legendary item does not have to be sold and does not degrade in quality.
     * It is also allowed to have a quality rating outside the normal range.
     */
    private static Item updateLegendaryItem(final Item current) {
        return new Item(current.name, current.sellIn, current.quality);
    }

    /**
     * An aging item increases in quality the older it gets.
     * It will, however, never go above or below certain constant bounds.
     */
    private static Item updateAgingItem(final Item current) {
        return new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, QUALITY_CHANGE_RATE));
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
    private static Item updateBackstagePass(final Item current) {
        return new Item(current.name, current.sellIn - 1, calculateQualityForBackstagePasses(current));
    }

    /**
     * A conjured item works similar to a normal item, but degrades twice as fast.
     */
    private static Item updateConjuredItem(final Item current) {
        return new Item(current.name, current.sellIn - 1, calculateQualityLinearly(current, -QUALITY_CHANGE_RATE * 2));
    }

    private static int calculateQualityLinearly(final Item item, final int qualityRate) {
        return roundQualityWithinLimits(item.quality + (qualityChangeFactor(item) * qualityRate));
    }

    /**
     * Items passed their sell-in date degrade or improve twice as fast.
     */
    private static int qualityChangeFactor(final Item item) {
        return item.sellIn <= 0 ? 2 : 1;
    }

    private static int calculateQualityForBackstagePasses(final Item item) {
        if (item.sellIn <= 0) {
            return 0;
        } else {
            return roundQualityWithinLimits(item.quality + qualityImprovementForBackstagePass(item));
        }
    }

    private static int qualityImprovementForBackstagePass(final Item item) {
        if (item.sellIn <= 5) {
            return 3;
        } else if (item.sellIn <= 10) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Returns the given quality rating, rounded to its lower or upper limit if necessary.
     */
    private static int roundQualityWithinLimits(final int quality) {
        return quality < MIN_QUALITY ? MIN_QUALITY : Math.min(quality, MAX_QUALITY);
    }
}