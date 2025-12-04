import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import Dolphin.ShoppingCart.domain.course.entity.Course;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.enums.MajorType;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.application.BucketServiceImpl; // 추가된 Import
import Dolphin.ShoppingCart.domain.student.dto.bucket.BucketAddRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.bucket.BucketAlternateRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.bucket.BucketPriorityRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.bucket.BucketResponseDTO;
import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.exception.StudentException;
import Dolphin.ShoppingCart.domain.student.repository.BucketElementRepository;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BucketServiceTest {

    @InjectMocks
    private BucketServiceImpl bucketService;

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private BucketRepository bucketRepository;
    @Mock
    private BucketElementRepository bucketElementRepository;
    @Mock
    private TeachRepository teachRepository;

    @Test
    @DisplayName("장바구니 목록 조회 성공")
    void getBucketList_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;

        // Student & Bucket
        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);

        // Teach & Course & Professor
        Course course = Course.builder().name("자료구조").majority(MajorType.MAJOR_REQUIRED).build();
        Professor professor = Professor.builder().professorName("김교수").build();
        Teach teach = Teach.builder().id(200L).course(course).professor(professor).build();

        // BucketElement
        BucketElement element = BucketElement.builder()
                .id(10L)
                .bucket(bucket)
                .teach(teach)
                .priority(1)
                .build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketElementRepository.findAllByBucketIdWithDetails(bucketId)).willReturn(List.of(element));

        // when
        List<BucketResponseDTO> result = bucketService.getBucketList(studentId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseName()).isEqualTo("자료구조");
        assertThat(result.get(0).getProfessorName()).isEqualTo("김교수");
    }

    @Test
    @DisplayName("장바구니에 과목 담기 성공")
    void addCourse_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;
        Long teachId = 200L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);

        Course course = Course.builder().name("자료구조").majority(MajorType.MAJOR_REQUIRED).build();
        Teach teach = Teach.builder().id(teachId).course(course).build();

        BucketAddRequestDTO request = new BucketAddRequestDTO();
        ReflectionTestUtils.setField(request, "teachId", teachId);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketElementRepository.existsByBucketIdAndTeachId(bucketId, teachId)).willReturn(false);
        given(bucketRepository.findById(bucketId)).willReturn(Optional.of(bucket));
        given(teachRepository.findById(teachId)).willReturn(Optional.of(teach));
        given(bucketElementRepository.findMaxPriorityByBucketId(bucketId)).willReturn(Optional.of(1));

        // when
        bucketService.addCourse(studentId, request);

        // then
        verify(bucketElementRepository).save(any(BucketElement.class));
    }

    @Test
    @DisplayName("장바구니 과목 삭제 성공")
    void deleteCourse_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;
        Long elementId = 10L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);
        BucketElement element = BucketElement.builder().id(elementId).bucket(bucket).build();

        given(bucketElementRepository.findById(elementId)).willReturn(Optional.of(element));

        // when
        bucketService.deleteCourse(studentId, elementId);

        // then
        verify(bucketElementRepository).delete(element);
    }

    @Test
    @DisplayName("장바구니 과목 삭제 실패 - 내 장바구니가 아님")
    void deleteCourse_Fail_Forbidden() {
        // given
        Long myStudentId = 1L;
        Long otherStudentId = 2L;
        Long bucketId = 100L;
        Long elementId = 10L;

        Student otherStudent = createStudent(otherStudentId, bucketId);
        Bucket bucket = createBucket(bucketId, otherStudent);
        BucketElement element = BucketElement.builder().id(elementId).bucket(bucket).build();

        given(bucketElementRepository.findById(elementId)).willReturn(Optional.of(element));

        // when & then
        assertThatThrownBy(() -> bucketService.deleteCourse(myStudentId, elementId))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._FORBIDDEN);
    }

    @Test
    @DisplayName("우선순위 변경 성공")
    void updatePriorities_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;
        Long elementId = 10L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);

        BucketElement element = new BucketElement(elementId, null, bucket, 1, null, null);

        BucketPriorityRequestDTO request = new BucketPriorityRequestDTO();
        ReflectionTestUtils.setField(request, "bucketElementId", elementId);
        ReflectionTestUtils.setField(request, "priority", 2);

        given(bucketElementRepository.findById(elementId)).willReturn(Optional.of(element));

        // when
        bucketService.updatePriorities(studentId, List.of(request));

        // then
        assertThat(element.getPriority()).isEqualTo(2);
    }

    @Test
    @DisplayName("대체과목 설정 성공")
    void updateAlternateCourse_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;
        Long targetElementId = 10L;
        Long alternateTeachId = 300L;
        Long alternateElementId = 20L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);

        BucketElement targetElement = new BucketElement(targetElementId, null, bucket, 1, null, null);
        BucketElement altElement = new BucketElement(alternateElementId, null, bucket, 2, null, null);

        BucketAlternateRequestDTO request = new BucketAlternateRequestDTO();
        ReflectionTestUtils.setField(request, "bucketElementId", targetElementId);
        ReflectionTestUtils.setField(request, "alternateTeachId", alternateTeachId);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketElementRepository.findById(targetElementId)).willReturn(Optional.of(targetElement));
        given(bucketElementRepository.findByBucketIdAndTeachId(bucketId, alternateTeachId))
                .willReturn(Optional.of(altElement));

        // when
        bucketService.updateAlternateCourse(studentId, request);

        // then
        assertThat(targetElement.getSubElement()).isEqualTo(altElement);
    }

    // --- Helper Methods ---
    private Student createStudent(Long id, Long bucketId) {
        Student student = Student.builder().bestBucket(bucketId).build();
        ReflectionTestUtils.setField(student, "id", id);
        return student;
    }

    private Bucket createBucket(Long id, Student student) {
        return Bucket.builder().id(id).student(student).build();
    }
}