package com.demo.demo.service;

import com.demo.demo.dto.AppointmentRequest;
import com.demo.demo.dto.AppointmentResponse;
import com.demo.demo.dto.CheckInResponse;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Appointment;
import com.demo.demo.entity.Consultant;
import com.demo.demo.entity.Schedule;
import com.demo.demo.enums.AppointmentStatus;
import com.demo.demo.enums.Role; // Import Role if not already
import com.demo.demo.exception.exceptions.BadRequestException;
import com.demo.demo.exception.exceptions.ResourceNotFoundException;
import com.demo.demo.repository.AppointmentRepository;
import com.demo.demo.repository.AuthenticationRepository; // Keep if needed elsewhere
import com.demo.demo.repository.ConsultantRepository; // Keep if needed elsewhere
import com.demo.demo.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Keep if needed elsewhere
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired(required = false) // Keep as is if annotation was intentional
    AuthenticationRepository authenticationRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private JitsiService jitsiService; // Keep as is

    @Autowired
    private EmailService emailService; // Keep as is

    @Autowired
    ConsultantRepository consultantRepository; // Keep as is


    @Transactional
    public Appointment create(AppointmentRequest appointmentRequest) {

        // --- THAY ĐỔI LOGIC TÌM KIẾM SCHEDULE ---
        // Trước đây tìm Schedule dựa trên consultantId, slotId, date
        // scheduleRepository.findScheduleBySlotIdAndConsultantAndDate(...)

        // Bây giờ tìm Schedule trực tiếp bằng scheduleId từ request DTO.
        // Các trường consultantId, appointmentDate, slotId trong request DTO sẽ bị bỏ qua
        // trong bước tìm Schedule này, nhưng vẫn tồn tại trong DTO như yêu cầu.
        Schedule slot = scheduleRepository.findById(appointmentRequest.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule slot not found with id " + appointmentRequest.getScheduleId()));
        // -----------------------------------------


        // Kiểm tra xem Schedule có được liên kết với một Consultant hợp lệ không.
        // Consultant object cần thiết cho các bước sau (email, mapping DTO).
        if (slot.getConsultant() == null || slot.getConsultant().getAccount() == null) {
            // Điều này cho thấy dữ liệu Schedule không hợp lệ
            throw new IllegalStateException("Schedule with ID " + appointmentRequest.getScheduleId() + " is not properly associated with a consultant account.");
        }
        // Optional: Kiểm tra role của consultant nếu cần validation chặt chẽ hơn.
        // Consultant consultant = slot.getConsultant();
        // if (!consultant.getAccount().getRole().equals(Role.CONSULTANT)){
        //     throw new BadRequestException("The associated consultant account for this schedule is not a valid consultant.");
        // }


        //check xem slot da dat hay chua
        // Giả định lại ý nghĩa của isBooked dựa trên logic gốc và cập nhật sau:
        // isBooked = true nghĩa là AVAILABLE (có thể đặt)
        // isBooked = false nghĩa là BOOKED (đã đặt)
        if (!slot.isBooked()) { // Nếu isBooked là FALSE (đã BOOKED)
            throw new BadRequestException("Schedule slot is already booked or unavailable");
        }
        // Nếu isBooked là TRUE (AVAILABLE), tiếp tục xử lý đặt lịch.


        //lay tai khoan hien tai dang dat lich
        Account currentAccount = authenticationService.getCurrentAccount();
        // Optional: Kiểm tra nếu currentAccount là null (nếu không có bảo mật) hoặc nếu account có quyền đặt lịch


        // Tao Appointment moi
        Appointment appointment = new Appointment();
        appointment.setCreateAt(LocalDate.now());
        appointment.setStatus(AppointmentStatus.BOOKED); // Set initial status
        appointment.setAccount(currentAccount); // Link to the account making the appointment
        appointment.setSchedule(slot); // Link to the specific schedule slot found by scheduleId
        appointment.setDescription(appointmentRequest.getDescription()); // Set description from request
        // Các trường consultantId, appointmentDate, slotId từ request DTO không được sử dụng trực tiếp để tạo Appointment entity,
        // vì thông tin này đã có sẵn từ Schedule entity được liên kết.
        Appointment savedAppointment = appointmentRepository.save(appointment); // Save the new appointment


        // Cap nhat trang thai cua slot thanh da dat (set UNAVAILABLE)
        slot.setBooked(false); // Mark the schedule slot as unavailable (assuming false means booked)
        scheduleRepository.save(slot); // Save the updated schedule slot

        // Optional: Trigger email/notifications etc. after saving

        return savedAppointment; // Return the created appointment entity
    }

    // --- Các phương thức khác giữ nguyên ---

    public CheckInResponse checkInAppointment(Long appointmentId) {
        // Logic giữ nguyên
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!appointment.isCheckedIn()) { // Chỉ check-in nếu chưa check-in
            // ... (logic tạo link, gửi email, cập nhật database) ...
            Account member = appointment.getAccount();
            Schedule schedule = appointment.getSchedule();
            Consultant consultant = schedule.getConsultant(); // Lấy consultant từ schedule

            if (consultant == null || consultant.getAccount() == null) {
                throw new IllegalStateException("Consultant account not found for this schedule associated with the appointment");
            }

            Account consultantAccount = consultant.getAccount(); // Lấy account của consultant

            // Tạo link cuộc họp
            String meetingLink = jitsiService.createMeetingRoom("appointment-" + appointmentId);

            // Gửi email
            try {
                emailService.sendMeetingLink(member.getEmail(), member.getName(), meetingLink);
                if (consultantAccount.getEmail() != null && !consultantAccount.getEmail().isEmpty()) {
                    emailService.sendMeetingLink(consultantAccount.getEmail(), consultantAccount.getName(), meetingLink);
                } else {
                    System.err.println("Consultant email is missing for appointment ID: " + appointmentId); // Log cảnh báo
                }
            } catch (Exception e) {
                System.err.println("Failed to send meeting link email for appointment ID " + appointmentId + ": " + e.getMessage());
                // Tùy chọn: ném ngoại lệ hoặc chỉ ghi log tùy thuộc vào yêu cầu
            }

            // Cập nhật Appointment trong DB
            appointment.setMeetingLink(meetingLink);
            appointment.setCheckedIn(true);
            // Tùy chọn: Cập nhật status sang IN_PROGRESS
            // appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            appointmentRepository.save(appointment);

            // Trả về DTO response
            CheckInResponse response = new CheckInResponse();
            response.setAppointmentId(appointment.getId());
            response.setMeetingLink(meetingLink);
            response.setCheckedIn(true);
            return response;

        } else {
            // Nếu đã check-in, trả về link đã có
            CheckInResponse response = new CheckInResponse();
            response.setAppointmentId(appointment.getId());
            response.setMeetingLink(appointment.getMeetingLink());
            response.setCheckedIn(true);
            return response;
        }
    }


    public AppointmentResponse getAppointmentById(Long appointmentId) {
        // Logic giữ nguyên
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        return mapToAppointmentResponse(appointment);
    }


    public List<AppointmentResponse> getAllAppointments() {
        // Logic giữ nguyên
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }


    public List<AppointmentResponse> getAppointmentsByAccountId(Long accountId) {
        // Logic giữ nguyên
        List<Appointment> appointments = appointmentRepository.findByAccountId(accountId);
        return appointments.stream()
                .map(this::mapToAppointmentResponse)
                .collect(Collectors.toList());
    }


    private AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        // Logic giữ nguyên, đảm bảo lấy scheduleId, consultantId, date từ Schedule liên quan
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setCreateAt(appointment.getCreateAt()); // creation date of appointment
        response.setDescription(appointment.getDescription());
        response.setStatus(appointment.getStatus());
        response.setMeetingLink(appointment.getMeetingLink());
        response.setCheckedIn(appointment.isCheckedIn());

        if (appointment.getAccount() != null) {
            response.setAccountId(appointment.getAccount().getId());
        }

        if (appointment.getSchedule() != null) {
            response.setScheduleId(appointment.getSchedule().getId());
            response.setAppointmentDate(appointment.getSchedule().getDate());


            if (appointment.getSchedule().getConsultant() != null) {
                response.setConsultantId(appointment.getSchedule().getConsultant().getId());
            } else {
                response.setConsultantId(null);
            }

        } else {

            response.setConsultantId(null);
            response.setScheduleId(null);
            response.setAppointmentDate(null);
        }

        return response;
    }


}