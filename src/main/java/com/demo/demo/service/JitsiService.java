package com.demo.demo.service;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.UUID;

@Service
public class JitsiService {

    public String createMeetingRoom(String username) {
        String roomName = normalizeName(username) + "_" + UUID.randomUUID().toString().substring(0, 8);
        return "https://meet.jit.si/" + roomName;
    }

    private String normalizeName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\w]", "_");
    }
}

