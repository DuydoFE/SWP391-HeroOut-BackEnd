package com.demo.demo.api;

import com.demo.demo.dto.ChapterDTO;
import com.demo.demo.entity.Chapter;
import com.demo.demo.service.ChapterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class ChapterAPI {
    private final ChapterService service;

    @GetMapping
    public List<Chapter> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public Chapter create(@RequestBody Chapter chapter) {
        return service.create(chapter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chapter> update(@PathVariable Long id, @RequestBody ChapterDTO dto) {
        Chapter updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}