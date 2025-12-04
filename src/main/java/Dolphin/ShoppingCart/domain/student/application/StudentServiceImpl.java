package Dolphin.ShoppingCart.domain.student.application;

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
}
