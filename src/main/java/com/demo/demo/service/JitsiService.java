package com.demo.demo.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class JitsiService {

    public String createMeetingRoom(String username) {
        // Chuyển username về định dạng không dấu cách, thêm UUID
        String roomName = username.replaceAll("\\s+", "_") + "_" + UUID.randomUUID().toString().substring(0, 8);
        return "https://meet.jit.si/" + roomName;
    }
}
