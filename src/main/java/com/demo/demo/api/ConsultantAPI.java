package com.demo.demo.api;

import com.demo.demo.dto.ConsultantRequest;
import com.demo.demo.dto.ConsultantResponse;
import com.demo.demo.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultants")
public class ConsultantAPI {

    @Autowired
    private ConsultantService consultantService;

    @PostMapping("/{accountId}")
    public ResponseEntity<ConsultantResponse> create(@PathVariable long accountId, @RequestBody ConsultantRequest request) {
        return ResponseEntity.ok(consultantService.create(accountId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultantResponse> update(@PathVariable long id, @RequestBody ConsultantRequest request) {
        return ResponseEntity.ok(consultantService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultantResponse> getById(@PathVariable long id) {
        return ResponseEntity.ok(consultantService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultantResponse>> getAll() {
        return ResponseEntity.ok(consultantService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        consultantService.delete(id);
        return ResponseEntity.ok().build();
    }
}