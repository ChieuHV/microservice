package com.inventoryservice.service;

import com.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryService {
    void save(Inventory inventory);

    List<Inventory> findAll();
}
