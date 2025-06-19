package com.demo.demo.service;

import com.demo.demo.entity.Chapter;
import com.demo.demo.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;

    public List<Chapter> findAll() {
        return chapterRepository.findAll();
    }

    public Chapter findById(Long id) {
        return chapterRepository.findById(id).orElseThrow();
    }

    public Chapter create(Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    public Chapter update(Long id, Chapter chapter) {
        chapter.setId(id);
        return chapterRepository.save(chapter);
    }

    public void delete(Long id) {
        chapterRepository.deleteById(id);
    }
}
