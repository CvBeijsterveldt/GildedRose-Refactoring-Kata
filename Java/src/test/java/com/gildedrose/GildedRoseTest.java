package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    public void foo() {
        final Item[] items = new Item[] { new Item("foo", 0, 0) };
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name);
    }

    @Test
    public void updateNormalItemWithinSellIn() {
        final Item[] items = new Item[]{new Item("foo", 1, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo, 0, 9", app.items[0].toString());
    }

    @Test
    public void updateNormalItemPastSellIn() {
        final Item[] items = new Item[]{new Item("foo", 0, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo, -1, 8", app.items[0].toString());
    }

    @Test
    public void updatingNeverReducesQualityBelowZero() {
        final Item[] items = new Item[]{new Item("foo", 0, 0)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo, -1, 0", app.items[0].toString());
    }

    @Test
    public void updateAgingItem() {
        final Item[] items = new Item[]{new Item("Aged Brie", 0, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Aged Brie, -1, 12", app.items[0].toString());
    }

    @Test
    public void updatingNeverIncreasesQualityAboveFifty() {
        final Item[] items = new Item[]{new Item("Aged Brie", 0, 50)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Aged Brie, -1, 50", app.items[0].toString());
    }

    @Test
    public void updateLegendaryItem() {
        final Item[] items = new Item[]{new Item("Sulfuras, Hand of Ragnaros", 0, 80)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Sulfuras, Hand of Ragnaros, 0, 80", app.items[0].toString());
    }

    @Test
    public void updateBackstagePassOverTenDays() {
        final Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 15, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Backstage passes to a TAFKAL80ETC concert, 14, 11", app.items[0].toString());
    }

    @Test
    public void updateBackstagePassBetweenTenAndFiveDays() {
        final Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 10, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Backstage passes to a TAFKAL80ETC concert, 9, 12", app.items[0].toString());
    }

    @Test
    public void updateBackstagePassWithinFiveDays() {
        final Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 5, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Backstage passes to a TAFKAL80ETC concert, 4, 13", app.items[0].toString());
    }

    @Test
    public void updateBackstagePassForPastConcert() {
        final Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 0, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Backstage passes to a TAFKAL80ETC concert, -1, 0", app.items[0].toString());
    }

    @Test
    public void updateConjuredItemWithinSellIn() {
        final Item[] items = new Item[]{new Item("Conjured Mana Cake", 10, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Conjured Mana Cake, 9, 8", app.items[0].toString());
    }

    @Test
    public void updateConjuredItemPastSellIn() {
        final Item[] items = new Item[]{new Item("Conjured Mana Cake", 0, 10)};
        final GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("Conjured Mana Cake, -1, 6", app.items[0].toString());
    }
}
