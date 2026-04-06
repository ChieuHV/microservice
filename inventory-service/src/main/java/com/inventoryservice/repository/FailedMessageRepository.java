package com.inventoryservice.repository;

import com.inventoryservice.model.FailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessageRepository extends JpaRepository<FailedMessage, String> {
}
