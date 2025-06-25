package com.demo.demo.api;

import com.demo.demo.entity.Consultant;
import com.demo.demo.repository.ConsultantRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@SecurityRequirement(name = "api")

@RequestMapping("/api/consultants")
public class ConsultantAPI {

    // Inject (tiêm) ConsultantRepository để có thể truy cập dữ liệu
    private final ConsultantRepository consultantRepository;

    // Sử dụng constructor injection (hoặc @Autowired trên trường) để tiêm repository
    @Autowired
    public ConsultantAPI(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }


    @GetMapping
    public List<Consultant> getAllConsultants() {

        return consultantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultant> getConsultantById(@PathVariable Long id) {

        Optional<Consultant> consultant = consultantRepository.findById(id);


        if (consultant.isPresent()) {

            return ResponseEntity.ok(consultant.get());
        } else {

            return ResponseEntity.notFound().build();
        }
    }
}