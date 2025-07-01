package com.demo.demo.service;

import com.demo.demo.dto.ConsultantResponse; // Import DTO
import com.demo.demo.entity.Consultant; // Import Entity
import com.demo.demo.exception.exceptions.ResourceNotFoundException; // Import Exception
import com.demo.demo.repository.ConsultantRepository; // Import Repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors; // Import Collectors

@Service // Đánh dấu đây là Spring Service
public class ConsultantService {

    private final ConsultantRepository consultantRepository;

    @Autowired // Tiêm Repository
    public ConsultantService(ConsultantRepository consultantRepository) {
        this.consultantRepository = consultantRepository;
    }

    // Phương thức helper để ánh xạ từ Entity sang DTO
    private ConsultantResponse mapToConsultantResponse(Consultant consultant) {
        ConsultantResponse dto = new ConsultantResponse();
        dto.setId(consultant.getId());

        // Lấy thông tin từ Account liên kết
        if (consultant.getAccount() != null) {
            dto.setAccountId(consultant.getAccount().getId());
            dto.setConsultantName(consultant.getAccount().getName());
        } else {
            dto.setAccountId(null);
            dto.setConsultantName("N/A"); // Hoặc một giá trị mặc định khác
        }

        // Ánh xạ các trường khác từ Consultant entity
        dto.setFieldOfStudy(consultant.getFieldOfStudy());
        dto.setDegreeLevel(consultant.getDegreeLevel());
        dto.setIssuedDate(consultant.getIssuedDate());
        dto.setExpiryDate(consultant.getExpiryDate());
        dto.setOrganization(consultant.getOrganization());
        dto.setSpecialities(consultant.getSpecialities());
        dto.setExperience(consultant.getExperience());
        dto.setRating(consultant.getRating());
        dto.setConsultations(consultant.getConsultations());
        dto.setBio(consultant.getBio());

        return dto;
    }

    // Phương thức lấy tất cả Consultant và trả về dưới dạng List<ConsultantResponse>
    public List<ConsultantResponse> getAllConsultants() {
        List<Consultant> consultants = consultantRepository.findAll();
        return consultants.stream()
                .map(this::mapToConsultantResponse) // Ánh xạ từng Entity sang DTO
                .collect(Collectors.toList()); // Thu thập thành List DTO
    }

    // Phương thức lấy Consultant theo ID và trả về ConsultantResponse
    public ConsultantResponse getConsultantById(Long id) {
        Consultant consultant = consultantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultant not found with id " + id)); // Ném exception nếu không tìm thấy

        return mapToConsultantResponse(consultant); // Ánh xạ Entity tìm được sang DTO
    }

    // Có thể thêm các phương thức khác ở đây (create, update, delete...)
}