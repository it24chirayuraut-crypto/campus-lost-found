package com.campus.lostfound.service;

import com.campus.lostfound.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemService {

    private final List<Item> items = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public Item addItem(Item item) {
        item.setId(idCounter.incrementAndGet());
        items.add(0, item); // add to front so newest shows first
        return item;
    }

    public List<Item> getAllItems() {
        return items;
    }

    public Optional<Item> getItemById(Long id) {
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public List<Item> filterItems(String type, String category) {
        return items.stream()
                .filter(item -> type == null || type.isBlank() || item.getType().equalsIgnoreCase(type))
                .filter(item -> category == null || category.isBlank() || item.getCategory().equalsIgnoreCase(category))
                .toList();
    }
}