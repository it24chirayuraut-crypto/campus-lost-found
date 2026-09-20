package com.campus.lostfound;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    private ItemService itemService;

    @BeforeEach
    void setup() {
        itemService = new ItemService();
    }

    @Test
    void addingItemMakesItRetrievableFromListing() {
        Item item = new Item();
        item.setName("Black Umbrella");
        item.setType("Lost");
        item.setCategory("Other");
        item.setLocation("Canteen");

        itemService.addItem(item);

        assertEquals(1, itemService.getAllItems().size());
        assertEquals("Black Umbrella", itemService.getAllItems().get(0).getName());
    }

    @Test
    void addingItemAssignsAnId() {
        Item item = new Item();
        item.setName("Silver Watch");

        Item saved = itemService.addItem(item);

        assertNotNull(saved.getId());
    }

    @Test
    void getItemByIdReturnsCorrectItem() {
        Item item = new Item();
        item.setName("Red Backpack");
        Item saved = itemService.addItem(item);

        var found = itemService.getItemById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Red Backpack", found.get().getName());
    }
}