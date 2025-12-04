package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.student.converter.StudentConverter;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
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

        // 닉네임 중복
        if (studentRepository.existsByNickname(requestDTO.getNickname())) {
            throw new StudentException(ErrorStatus.STUDENT_NICKNAME_DUPLICATED);
        }

        Student student = StudentConverter.toStudent(requestDTO);
        Student saved = studentRepository.save(student);

        return StudentConverter.toSignUpResponseDTO(saved);
    }
}
