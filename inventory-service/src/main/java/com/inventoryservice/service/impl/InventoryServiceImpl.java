package com.inventoryservice.service.impl;

import com.inventoryservice.model.Inventory;
import com.inventoryservice.repository.InventoryRepository;
import com.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void save(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
}
