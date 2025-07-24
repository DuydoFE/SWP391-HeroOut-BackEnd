package com.demo.demo.service;

import com.demo.demo.dto.*;
import com.demo.demo.entity.Course;
import com.demo.demo.entity.Enrollment;
import com.demo.demo.entity.Chapter;
import com.demo.demo.entity.Account;
import com.demo.demo.enums.AgeGroup;
import com.demo.demo.enums.ProgressStatus;
import com.demo.demo.enums.CourseStatus;
import com.demo.demo.repository.CourseRepository;
import com.demo.demo.repository.EnrollmentRepository;
import com.demo.demo.repository.ChapterRepository;
import com.demo.demo.repository.AccountRepository;
import com.demo.demo.service.CloudinaryService;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    public CourseCreateResponse createCourse(CourseRequest request) {
        // Tạo course mà không map chapters field
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setObjectives(request.getObjectives());
        course.setOverview(request.getOverview());
        course.setAgeGroup(request.getAgeGroup());
        course.setCreatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);

        // Tạo chapters nếu có trong request
        List<ChapterResponse> chapterResponses = null;
        if (request.getChapters() != null && !request.getChapters().isEmpty()) {
            chapterResponses = request.getChapters().stream().map(chapterRequest -> {
                Chapter chapter = new Chapter();
                chapter.setTitle(chapterRequest.getTitle());
                chapter.setContent(chapterRequest.getContent());
                chapter.setCourse(saved);
                try {
                    if (chapterRequest.getImage() != null && !chapterRequest.getImage().isEmpty()) {
                        String imageUrl = cloudinaryService.uploadFile(chapterRequest.getImage(), "chapters/images");
                        chapter.setImage(imageUrl);
                    }
                    if (chapterRequest.getVideo() != null && !chapterRequest.getVideo().isEmpty()) {
                        String videoUrl = cloudinaryService.uploadFile(chapterRequest.getVideo(), "chapters/videos");
                        chapter.setVideo(videoUrl);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Upload file thất bại: " + e.getMessage());
                }
                Chapter savedChapter = chapterRepository.save(chapter);

                ChapterResponse chapterResponse = new ChapterResponse();
                chapterResponse.setId(savedChapter.getId());
                chapterResponse.setCourseId(savedChapter.getCourse().getId());
                chapterResponse.setTitle(savedChapter.getTitle());
                chapterResponse.setContent(savedChapter.getContent());
                chapterResponse.setImage(savedChapter.getImage());
                chapterResponse.setVideo(savedChapter.getVideo());
                return chapterResponse;
            }).collect(Collectors.toList());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(saved);
        enrollment.setAccount(null);
        enrollment.setStatus(ProgressStatus.NOT_STARTED);
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);

        // Tạo response với chapters
        CourseCreateResponse response = new CourseCreateResponse();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setDescription(saved.getDescription());
        response.setObjectives(saved.getObjectives());
        response.setOverview(saved.getOverview());
        response.setAgeGroup(saved.getAgeGroup());
        response.setCreatedAt(saved.getCreatedAt());
        response.setTotalEnrollment(0); // Mới tạo nên chưa có enrollment
        response.setChapters(chapterResponses);

        return response;
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        modelMapper.map(request, course);
        return mapCourseWithTotal(courseRepository.save(course));
    }

    public CourseResponse updateCourseStatus(Long id, CourseStatus status) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (course.getStatus() == status) {
            throw new RuntimeException("Course is already in status: " + status);
        }
        course.setStatus(status);
        Course updated = courseRepository.save(course);
        CourseResponse response = modelMapper.map(updated, CourseResponse.class);
        long total = updated.getEnrollments() == null ? 0 : updated.getEnrollments().stream()
                .filter(e -> e.getAccount() != null)
                .count();
        response.setTotalEnrollment(total);
        return response;
    }

    public List<CourseResponse> getAllCoursesWithStatus() {
        return courseRepository.findAll().stream().map(course -> {
            CourseResponse response = modelMapper.map(course, CourseResponse.class);
            long total = course.getEnrollments() == null ? 0 : course.getEnrollments().stream()
                    .filter(e -> e.getAccount() != null)
                    .count();
            response.setTotalEnrollment(total);
            return response;
        }).collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapCourseWithTotal(course);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapCourseWithTotal)
                .collect(Collectors.toList());
    }


    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<InProgressCourseResponse> getCoursesByStatusAndAccount(Long accountId, ProgressStatus status) {
        List<Course> courses = courseRepository.findCoursesByAccountIdAndStatus(accountId, status)
                .stream().filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .collect(Collectors.toList());

        return courses.stream().map(course -> {
            InProgressCourseResponse response = modelMapper.map(course, InProgressCourseResponse.class);

            // Tổng số chương của khóa học
            long totalChapter = course.getChapters() != null ? course.getChapters().size() : 0;
            response.setTotalChapter(totalChapter);

            // Tìm enrollment của user với khóa học
            Enrollment enrollment = course.getEnrollments() != null
                    ? course.getEnrollments().stream()
                    .filter(e -> e.getAccount() != null && accountId.equals(e.getAccount().getId()))
                    .findFirst()
                    .orElse(null)
                    : null;

            long completedChapter = 0;
            if (enrollment != null && enrollment.getEnrollmentChapters() != null) {
                completedChapter = enrollment.getEnrollmentChapters().stream()
                        .filter(ec -> ec.getStatus() == ProgressStatus.COMPLETED)
                        .count();
            }

            response.setCompletedChapter(completedChapter);
            return response;
        }).collect(Collectors.toList());
    }


    public List<CourseResponse> getCoursesNotStartedByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        java.util.Date dob = account.getDateOfBirth();
        if (dob == null) {
            throw new com.demo.demo.exception.exceptions.BadRequestException("Account does not have date of birth information.");
        }
        java.time.LocalDate localDob;
        if (dob instanceof java.sql.Date) {
            localDob = ((java.sql.Date) dob).toLocalDate();
        } else {
            localDob = dob.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }
        com.demo.demo.enums.AgeGroup userAgeGroup = AccountService.getAgeGroup(localDob);
        List<Course> courses = courseRepository.findCoursesNotEnrolledByAccount(accountId)
                .stream().filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .filter(course -> course.getAgeGroup() != null && course.getAgeGroup().ordinal() <= userAgeGroup.ordinal())
                .collect(Collectors.toList());
        return courses.stream().map(this::mapCourseWithTotal).collect(Collectors.toList());
    }

    public long countEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.countByCourse_IdAndAccountIsNotNull(courseId);
    }

    private CourseResponse mapCourseWithTotal(Course course) {
        CourseResponse response = modelMapper.map(course, CourseResponse.class);
        long total = course.getEnrollments() == null ? 0 : course.getEnrollments().stream()
                .filter(e -> e.getAccount() != null)
                .count();
        response.setTotalEnrollment(total);
        return response;
    }
}
