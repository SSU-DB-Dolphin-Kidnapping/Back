package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.academic.entity.College;
import Dolphin.ShoppingCart.domain.academic.entity.Department;
import Dolphin.ShoppingCart.domain.academic.repository.CollegeRepository;
import Dolphin.ShoppingCart.domain.academic.repository.DepartmentRepository;
import Dolphin.ShoppingCart.domain.student.converter.StudentConverter;
import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.onboarding.StudentOnboardingRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.update.StudentUpdateRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailSendRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.verify.StudentEmailVerifyRequestDTO;
import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.MyLastBucketNumber;
import Dolphin.ShoppingCart.domain.student.entity.StudentEmailVerification;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.MyLastBucketNumberRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentEmailVerificationRepository;
import Dolphin.ShoppingCart.global.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import Dolphin.ShoppingCart.domain.student.exception.StudentException;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository; // 생성자 주입
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;
    private final StudentEmailVerificationRepository studentEmailVerificationRepository;
    private final MailService mailService;
    private final BucketRepository bucketRepository;
    private final MyLastBucketNumberRepository myLastBucketNumberRepository;

    @Transactional(readOnly = true)
    public Double getReactionTime(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));
        return student.getAvgReactionTime();
    }

    @Transactional
    public void updateReactionTime(Long studentId, StudentReactionRequestDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        student.updateReactionTime(request.getAvgReactionTime());
    }


    @Transactional
    public StudentSignUpResponseDTO signUp(StudentSignUpRequestDTO requestDTO) {

        if (studentRepository.existsByNickname(requestDTO.getNickname())) {
            throw new StudentException(ErrorStatus.STUDENT_NICKNAME_DUPLICATED);
        }

        Student student = StudentConverter.toStudent(requestDTO);
        Student savedStudent = studentRepository.save(student);

        Bucket basicBucket = Bucket.builder()
                .student(savedStudent)
                .name("기본장바구니")
                .build();

        Bucket savedBucket = bucketRepository.save(basicBucket);

        savedStudent.changeBestBucket(savedBucket.getId());

        MyLastBucketNumber lastBucket = MyLastBucketNumber.builder()
                .student(savedStudent)
                .lastBucketNumber(savedBucket.getId().intValue())
                .build();

        myLastBucketNumberRepository.save(lastBucket);

        return StudentConverter.toSignUpResponseDTO(savedStudent);
    }

    public StudentLoginResponseDTO login(StudentLoginRequestDTO requestDTO) {

        Student student = studentRepository.findByNickname(requestDTO.getNickname())
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_LOGIN_FAILED));

        if (!student.getPassword().equals(requestDTO.getPassword())) {
            throw new StudentException(ErrorStatus.STUDENT_LOGIN_FAILED);
        }

        return StudentConverter.toLoginResponseDTO(student);
    }

    @Override
    public StudentInfoResponseDTO getStudentInfo(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_NOT_FOUND));

        return StudentConverter.toStudentInfoResponseDTO(student);
    }


    @Override
    @Transactional
    public StudentInfoResponseDTO updateStudentInfo(Long studentId, StudentUpdateRequestDTO requestDTO) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_NOT_FOUND));

        if (requestDTO.getStudentName() != null && !requestDTO.getStudentName().isBlank()) {
            student.updateStudentName(requestDTO.getStudentName());
        }

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            student.updatePassword(requestDTO.getPassword());
        }

        if (requestDTO.getGrade() != null) {
            student.updateGrade(requestDTO.getGrade());
        }

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new StudentException(ErrorStatus.DEPARTMENT_NOT_FOUND));

            student.updateDepartment(department);
        }

        return StudentConverter.toStudentInfoResponseDTO(student);
    }



    @Override
    @Transactional
    public StudentInfoResponseDTO onboarding(Long studentId, StudentOnboardingRequestDTO requestDTO) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_NOT_FOUND));

        if (requestDTO.getStudentNumber() != null && !requestDTO.getStudentNumber().isBlank()) {

            boolean duplicated = studentRepository.existsByStudentNumber(requestDTO.getStudentNumber());

            if (duplicated && (student.getStudentNumber() == null
                    || !student.getStudentNumber().equals(requestDTO.getStudentNumber()))) {
                throw new StudentException(ErrorStatus.STUDENT_NUMBER_DUPLICATED);
            }

            student.updateStudentNumber(requestDTO.getStudentNumber());
        }

        if (requestDTO.getGrade() != null) {
            student.updateGrade(requestDTO.getGrade());
        }

        College college = null;
        if (requestDTO.getCollegeId() != null) {
            college = collegeRepository.findById(requestDTO.getCollegeId())
                    .orElseThrow(() -> new StudentException(ErrorStatus.COLLEGE_NOT_FOUND));
        }

        if (requestDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                    .orElseThrow(() -> new StudentException(ErrorStatus.DEPARTMENT_NOT_FOUND));

            if (college != null && department.getCollege() != null
                    && !department.getCollege().getId().equals(college.getId())) {
                throw new StudentException(ErrorStatus.DEPARTMENT_NOT_FOUND);
            }

            student.updateDepartment(department);
        }

        return StudentConverter.toStudentInfoResponseDTO(student);
    }




    private String generateCode() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(num);
    }

    @Override
    @Transactional
    public void sendVerificationEmail(Long studentId, StudentEmailSendRequestDTO requestDTO) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_NOT_FOUND));

        String email = requestDTO.getSoongsilEmail();

        if (Boolean.TRUE.equals(student.getVerified())) {
            throw new StudentException(ErrorStatus.ALREADY_VERIFIED_STUDENT);
        }

        if (studentRepository.existsBySoongsilEmail(email)) {
            throw new StudentException(ErrorStatus.EMAIL_ALREADY_IN_USE);
        }

        if (!email.endsWith("@soongsil.ac.kr")) {
            throw new StudentException(ErrorStatus._BAD_REQUEST);
        }

        student.updateSoongsilEmail(email);

        String code = generateCode();

        StudentEmailVerification verification = StudentEmailVerification.builder()
                .student(student)
                .email(email)
                .code(code)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        studentEmailVerificationRepository.save(verification);

        mailService.sendVerificationMail(email, code);
    }

    @Override
    @Transactional
    public StudentInfoResponseDTO verifyEmail(Long studentId, StudentEmailVerifyRequestDTO requestDTO) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.STUDENT_NOT_FOUND));

        StudentEmailVerification verification = studentEmailVerificationRepository
                .findTopByStudentIdOrderByCreatedAtDesc(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus.VERIFICATION_CODE_INVALID));

        if (verification.getUsed()) {
            throw new StudentException(ErrorStatus.VERIFICATION_CODE_ALREADY_USED);
        }

        if (verification.isExpired()) {
            throw new StudentException(ErrorStatus.VERIFICATION_CODE_EXPIRED);
        }

        if (!verification.getCode().equals(requestDTO.getCode())) {
            throw new StudentException(ErrorStatus.VERIFICATION_CODE_INVALID);
        }

        // 코드 사용 처리
        verification.markUsed();

        // 학생 인증 처리
        student.verify();

        // 최종 학생 정보 반환 (verified 포함)
        return StudentConverter.toStudentInfoResponseDTO(student);
    }

}
