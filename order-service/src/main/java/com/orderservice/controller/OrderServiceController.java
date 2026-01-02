package com.orderservice.controller;

import com.orderservice.dto.OderDTO;
import com.orderservice.dto.UserDTO;
import com.orderservice.model.Order;
import com.orderservice.openfeignclient.UserClient;
import com.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders/")
public class OrderServiceController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserClient userClient;

    @PostMapping("save")
    public ResponseEntity<String> save(@RequestBody Order order) {
        try {
            orderService.save(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }

    @GetMapping("find-all")
    public ResponseEntity<List<Order>> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("find-id/{id}")
    public ResponseEntity<OderDTO> findById(@PathVariable("id") String id) {
        try {
            Order order = orderService.findById(id);
            UserDTO userDTO = userClient.getUserById(order.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(new OderDTO(order.getId(), order.getProductName(), order.getPrice(), userDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
