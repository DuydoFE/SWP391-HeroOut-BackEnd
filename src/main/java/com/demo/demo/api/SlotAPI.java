package com.demo.demo.api;

import com.demo.demo.dto.RegisterSlotDTO;

import com.demo.demo.entity.Schedule;
import com.demo.demo.entity.Slot;
import com.demo.demo.service.SlotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slot")
@SecurityRequirement(name = "api")
public class SlotAPI {

    @Autowired
    SlotService slotService;

    @PostMapping
    public void generateSlot(){
        slotService.generateSlot();
    }

    @GetMapping
    public ResponseEntity getSlot(){
        List<Slot> slots = slotService.get();
        return ResponseEntity.ok(slots);
    }

    @PostMapping("register")
    public ResponseEntity registerSlot(@RequestBody RegisterSlotDTO registerSlotDTO){
        List<Schedule> schedules = slotService.registerSlot(registerSlotDTO);
        return ResponseEntity.ok(schedules);
    }
}
