package com.eexchange.backend.Service;

import com.eexchange.backend.DTO.UpdateItemRequest;
import com.eexchange.backend.Entity.ItemStatus;
import com.eexchange.backend.Entity.items;
import com.eexchange.backend.Repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public items addItem(items item, String userEmail) {
        item.setStatus(ItemStatus.AVAILABLE);
        item.setUserEmail(userEmail);           //set the logged-in user's email
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return itemRepository.save(item);
    }

    public List<items> getAll() {
        return itemRepository.findAll();
    }

    public List<items> getMyItems(String userEmail) {
        return itemRepository.findByUserEmail(userEmail);
    }

    public items editItem(String itemId, String userEmail, UpdateItemRequest request) {
        items existing = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!existing.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("You are not authorized to edit this item");
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setCategory(request.getCategory());
        return itemRepository.save(existing);
    }
}
