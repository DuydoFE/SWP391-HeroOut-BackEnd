package com.demo.demo.repository;

import com.demo.demo.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByIsDeletedFalse();
}