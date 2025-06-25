package com.demo.demo.service;

import com.demo.demo.dto.ConsultantRequest;
import com.demo.demo.dto.ConsultantResponse;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Consultant;
import com.demo.demo.enums.Role;
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.repository.AuthenticationRepository;
import com.demo.demo.repository.ConsultantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    public ConsultantResponse create(long accountId, ConsultantRequest request) {
        Account account = authenticationRepository.findById(accountId)
                .orElseThrow(() -> new BadRequestException("Tài khoản không tồn tại"));

        if (!account.getRole().equals(Role.CONSULTANT)) {
            throw new BadRequestException("Tài khoản không phải là CONSULTANT");
        }

        if (account.getConsultant() != null) {
            throw new BadRequestException("Tài khoản đã có consultant");
        }

        Consultant consultant = new Consultant();
        mapRequestToConsultant(request, consultant);
        consultant.setAccount(account);

        Consultant saved = consultantRepository.save(consultant);
        account.setConsultant(saved);
        authenticationRepository.save(account);

        return toResponse(saved);
    }

    public ConsultantResponse update(long id, ConsultantRequest request) {
        Consultant consultant = consultantRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Consultant không tồn tại"));

        mapRequestToConsultant(request, consultant);
        return toResponse(consultantRepository.save(consultant));
    }

    public ConsultantResponse getById(long id) {
        return consultantRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new BadRequestException("Consultant không tồn tại"));
    }

    public List<ConsultantResponse> getAll() {
        return consultantRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void delete(long id) {
        Consultant consultant = consultantRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Consultant không tồn tại"));
        consultantRepository.delete(consultant);
    }

    private void mapRequestToConsultant(ConsultantRequest request, Consultant consultant) {
        consultant.setFieldOfStudy(request.getFieldOfStudy());
        consultant.setDegreeLevel(request.getDegreeLevel());
        consultant.setIssuedDate(request.getIssuedDate());
        consultant.setExpiryDate(request.getExpiryDate());
        consultant.setOrganization(request.getOrganization());
        consultant.setSpecialities(request.getSpecialities());
        consultant.setExperience(request.getExperience());
        consultant.setBio(request.getBio());
    }

    private ConsultantResponse toResponse(Consultant c) {
        ConsultantResponse dto = new ConsultantResponse();
        dto.setId(c.getId());
        dto.setFieldOfStudy(c.getFieldOfStudy());
        dto.setDegreeLevel(c.getDegreeLevel());
        dto.setIssuedDate(c.getIssuedDate());
        dto.setExpiryDate(c.getExpiryDate());
        dto.setOrganization(c.getOrganization());
        dto.setSpecialities(c.getSpecialities());
        dto.setExperience(c.getExperience());
        dto.setRating(c.getRating());
        dto.setConsultations(c.getConsultations());
        dto.setBio(c.getBio());
        dto.setAccountId(c.getAccount().getId());
        return dto;
    }
    }
