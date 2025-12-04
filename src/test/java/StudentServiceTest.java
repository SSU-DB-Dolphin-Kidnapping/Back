import Dolphin.ShoppingCart.domain.student.application.StudentServiceImpl;
import Dolphin.ShoppingCart.domain.student.dto.StudentReactionRequestDTO;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.exception.StudentException;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Test
    @DisplayName("반응속도 조회 성공")
    void getReactionTime_Success() {
        // given
        Long studentId = 1L;
        Double expectedTime = 250.5;

        Student student = Student.builder()
                .avgReactionTime(expectedTime)
                .build();
        ReflectionTestUtils.setField(student, "id", studentId);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        // when
        Double result = studentService.getReactionTime(studentId);

        // then
        assertThat(result).isEqualTo(expectedTime);
    }

    @Test
    @DisplayName("반응속도 조회 실패 - 학생 없음")
    void getReactionTime_Fail_NotFound() {
        // given
        Long studentId = 999L;
        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studentService.getReactionTime(studentId))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._BAD_REQUEST);
    }

    @Test
    @DisplayName("반응속도 업데이트 성공")
    void updateReactionTime_Success() {
        // given
        Long studentId = 1L;
        Double oldTime = 300.0;
        Double newTime = 150.0;

        // 기존 학생 정보 (반응속도 300ms)
        Student student = Student.builder()
                .avgReactionTime(oldTime)
                .build();
        ReflectionTestUtils.setField(student, "id", studentId);

        // 업데이트 요청 DTO 생성 (반응속도 150ms)
        StudentReactionRequestDTO request = new StudentReactionRequestDTO();
        ReflectionTestUtils.setField(request, "avgReactionTime", newTime);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        // when
        studentService.updateReactionTime(studentId, request);

        // then
        // 학생 엔티티의 상태가 변경되었는지 확인 (Dirty Checking 동작 검증용)
        assertThat(student.getAvgReactionTime()).isEqualTo(newTime);
    }

    @Test
    @DisplayName("반응속도 업데이트 실패 - 학생 없음")
    void updateReactionTime_Fail_NotFound() {
        // given
        Long studentId = 999L;
        StudentReactionRequestDTO request = new StudentReactionRequestDTO();

        given(studentRepository.findById(studentId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> studentService.updateReactionTime(studentId, request))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._BAD_REQUEST);
    }
}