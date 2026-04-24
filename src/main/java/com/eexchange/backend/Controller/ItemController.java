package com.eexchange.backend.Controller;

import com.eexchange.backend.DTO.UpdateItemRequest;
import com.eexchange.backend.Entity.items;
import com.eexchange.backend.Service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @PostMapping("/add")
    public ResponseEntity<items> addItem(@RequestBody items item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); //gets email from JWT token
        items saved = itemService.addItem(item, userEmail);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<items> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/my")
    public List<items> getMyItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return itemService.getMyItems(userEmail);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<items> editItem(@PathVariable String id, @RequestBody UpdateItemRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        items updated = itemService.editItem(id, userEmail, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
