package com.demo.demo.api;

import com.demo.demo.dto.RegisterSlotDTO;
import com.demo.demo.dto.ScheduleResponse;
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
    private SlotService slotService;

    @PostMapping("/generate")
    public ResponseEntity<Void> generateSlot() {
        slotService.generateSlot();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Slot>> getSlot() {
        List<Slot> slots = slotService.get();
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/register")
    public ResponseEntity<List<ScheduleResponse>> registerSlot(@RequestBody RegisterSlotDTO registerSlotDTO) {
        List<ScheduleResponse> responses = slotService.registerSlot(registerSlotDTO);
        return ResponseEntity.ok(responses);
    }
}
