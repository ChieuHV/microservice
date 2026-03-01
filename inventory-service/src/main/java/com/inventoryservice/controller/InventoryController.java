package com.inventoryservice.controller;

import com.inventoryservice.model.Inventory;
import com.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories/")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("save")
    public ResponseEntity<String> save(@RequestBody Inventory order) {
        try {
            inventoryService.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }

    @GetMapping("find-all")
    public ResponseEntity<List<Inventory>> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(inventoryService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
