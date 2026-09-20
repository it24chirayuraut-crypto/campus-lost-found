package com.campus.lostfound.controller;

import com.campus.lostfound.model.Item;
import com.campus.lostfound.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Home page — list all items, optionally filtered
    @GetMapping("/")
    public String home(@RequestParam(required = false) String type,
                        @RequestParam(required = false) String category,
                        Model model) {
        model.addAttribute("items", itemService.filterItems(type, category));
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedCategory", category);
        return "home";
    }

    // Show the "report an item" form
    @GetMapping("/post")
    public String showPostForm(Model model) {
        model.addAttribute("item", new Item());
        return "post";
    }

    // Handle form submission
    @PostMapping("/post")
    public String submitItem(@ModelAttribute Item item) {
        itemService.addItem(item);
        return "redirect:/";
    }

    // Item detail page
    @GetMapping("/item/{id}")
    public String itemDetail(@PathVariable Long id, Model model) {
        return itemService.getItemById(id)
                .map(item -> {
                    model.addAttribute("item", item);
                    return "item-detail";
                })
                .orElse("redirect:/");
    }
}