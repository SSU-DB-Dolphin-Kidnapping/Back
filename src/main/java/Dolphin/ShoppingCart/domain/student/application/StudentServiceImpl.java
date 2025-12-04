package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.academic.entity.Department;
import Dolphin.ShoppingCart.domain.academic.repository.DepartmentRepository;
import Dolphin.ShoppingCart.domain.student.converter.StudentConverter;
import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.update.StudentUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import Dolphin.ShoppingCart.domain.student.exception.StudentException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository; // 생성자 주입
    private final DepartmentRepository departmentRepository;

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
        Student saved = studentRepository.save(student);

        return StudentConverter.toSignUpResponseDTO(saved);
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
}
