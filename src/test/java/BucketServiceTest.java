import Dolphin.ShoppingCart.domain.academic.entity.Professor;
import Dolphin.ShoppingCart.domain.course.entity.Course;
import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.enums.MajorType;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.application.BucketServiceImpl;
import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
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
import static org.mockito.Mockito.times;
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

    // --- 기존 테스트: 장바구니 내용물 관리 ---

    @Test
    @DisplayName("장바구니 목록 조회 성공")
    void getBucketList_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);

        Course course = Course.builder().name("자료구조").majority(MajorType.MAJOR_REQUIRED).build();
        Professor professor = Professor.builder().professorName("김교수").build();
        Teach teach = Teach.builder().id(200L).course(course).professor(professor).build();

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
        Long studentId = 1L;
        Long bucketId = 100L;
        Long elementId = 10L;

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = createBucket(bucketId, student);
        BucketElement element = BucketElement.builder().id(elementId).bucket(bucket).build();

        given(bucketElementRepository.findById(elementId)).willReturn(Optional.of(element));

        bucketService.deleteCourse(studentId, elementId);

        verify(bucketElementRepository).delete(element);
    }

    @Test
    @DisplayName("장바구니 과목 삭제 실패 - 내 장바구니가 아님")
    void deleteCourse_Fail_Forbidden() {
        Long myStudentId = 1L;
        Long otherStudentId = 2L;
        Long bucketId = 100L;
        Long elementId = 10L;

        Student otherStudent = createStudent(otherStudentId, bucketId);
        Bucket bucket = createBucket(bucketId, otherStudent);
        BucketElement element = BucketElement.builder().id(elementId).bucket(bucket).build();

        given(bucketElementRepository.findById(elementId)).willReturn(Optional.of(element));

        assertThatThrownBy(() -> bucketService.deleteCourse(myStudentId, elementId))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._FORBIDDEN);
    }

    @Test
    @DisplayName("우선순위 변경 성공")
    void updatePriorities_Success() {
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

        bucketService.updatePriorities(studentId, List.of(request));

        assertThat(element.getPriority()).isEqualTo(2);
    }

    @Test
    @DisplayName("대체과목 설정 성공")
    void updateAlternateCourse_Success() {
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

        bucketService.updateAlternateCourse(studentId, request);

        assertThat(targetElement.getSubElement()).isEqualTo(altElement);
    }

    // --- 신규 테스트: 장바구니 자체 관리 (생성, 조회, 대표설정, 삭제) ---

    @Test
    @DisplayName("내 장바구니 목록 조회 성공")
    void getMyBuckets_Success() {
        // given
        Long studentId = 1L;
        Student student = createStudent(studentId, 100L); // 대표 장바구니 ID: 100

        Bucket bucket1 = Bucket.builder().id(100L).name("플랜A").student(student).build();
        Bucket bucket2 = Bucket.builder().id(101L).name("플랜B").student(student).build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.findAllByStudent(student)).willReturn(List.of(bucket1, bucket2));

        // when
        List<BucketSummaryDTO> results = bucketService.getMyBuckets(studentId);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("플랜A");
        assertThat(results.get(0).getIsBest()).isTrue(); // 100번이 대표이므로 True
        assertThat(results.get(1).getName()).isEqualTo("플랜B");
        assertThat(results.get(1).getIsBest()).isFalse();
    }

    @Test
    @DisplayName("장바구니 생성 성공 - 첫 장바구니는 자동으로 대표 설정")
    void createBucket_First_AutoBest() {
        // given
        Long studentId = 1L;
        // 대표 장바구니가 없는 상태
        Student student = createStudent(studentId, null);

        BucketCreateRequestDTO request = new BucketCreateRequestDTO();
        ReflectionTestUtils.setField(request, "name", "새 장바구니");

        Bucket savedBucket = Bucket.builder().id(100L).student(student).build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.save(any(Bucket.class))).willReturn(savedBucket);

        // when
        bucketService.createBucket(studentId, request);

        // then
        verify(bucketRepository).save(any(Bucket.class));
        // 첫 장바구니였으므로 student의 bestBucket이 100L로 업데이트 되어야 함
        assertThat(student.getBestBucket()).isEqualTo(100L);
    }

    @Test
    @DisplayName("장바구니 생성 성공 - 이미 대표가 있으면 그냥 생성만")
    void createBucket_NotFirst() {
        // given
        Long studentId = 1L;
        Student student = createStudent(studentId, 99L); // 이미 99번이 대표

        BucketCreateRequestDTO request = new BucketCreateRequestDTO();
        ReflectionTestUtils.setField(request, "name", "플랜B");

        Bucket savedBucket = Bucket.builder().id(100L).student(student).build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.save(any(Bucket.class))).willReturn(savedBucket);

        // when
        bucketService.createBucket(studentId, request);

        // then
        verify(bucketRepository).save(any(Bucket.class));
        // 대표 장바구니는 여전히 99번이어야 함
        assertThat(student.getBestBucket()).isEqualTo(99L);
    }

    @Test
    @DisplayName("대표 장바구니 설정 성공")
    void setBestBucket_Success() {
        // given
        Long studentId = 1L;
        Long newBestBucketId = 200L;
        Student student = createStudent(studentId, 100L); // 기존 100번

        Bucket newBucket = Bucket.builder().id(newBestBucketId).student(student).build();

        BucketSelectRequestDTO request = new BucketSelectRequestDTO();
        ReflectionTestUtils.setField(request, "bucketId", newBestBucketId);

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.findById(newBestBucketId)).willReturn(Optional.of(newBucket));

        // when
        bucketService.setBestBucket(studentId, request);

        // then
        assertThat(student.getBestBucket()).isEqualTo(newBestBucketId);
    }

    @Test
    @DisplayName("장바구니 삭제 성공")
    void deleteBucket_Success() {
        // given
        Long studentId = 1L;
        Long bucketId = 200L; // 삭제할 장바구니
        Long bestBucketId = 100L; // 대표 장바구니

        Student student = createStudent(studentId, bestBucketId);
        Bucket bucket = Bucket.builder().id(bucketId).student(student).build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.findById(bucketId)).willReturn(Optional.of(bucket));

        // when
        bucketService.deleteBucket(studentId, bucketId);

        // then
        verify(bucketRepository).delete(bucket);
    }

    @Test
    @DisplayName("장바구니 삭제 실패 - 대표 장바구니는 삭제 불가")
    void deleteBucket_Fail_IsBest() {
        // given
        Long studentId = 1L;
        Long bucketId = 100L; // 삭제하려는게 대표 장바구니

        Student student = createStudent(studentId, bucketId);
        Bucket bucket = Bucket.builder().id(bucketId).student(student).build();

        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));
        given(bucketRepository.findById(bucketId)).willReturn(Optional.of(bucket));

        // when & then
        assertThatThrownBy(() -> bucketService.deleteBucket(studentId, bucketId))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._BAD_REQUEST);

        verify(bucketRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("장바구니 삭제 실패 - 내 장바구니 아님")
    void deleteBucket_Fail_NotOwner() {
        // given
        Long myId = 1L;
        Long otherId = 2L;
        Long bucketId = 300L;

        Student me = createStudent(myId, 100L);
        Student other = createStudent(otherId, 300L);

        Bucket bucket = Bucket.builder().id(bucketId).student(other).build(); // 남의꺼

        given(studentRepository.findById(myId)).willReturn(Optional.of(me));
        given(bucketRepository.findById(bucketId)).willReturn(Optional.of(bucket));

        // when & then
        assertThatThrownBy(() -> bucketService.deleteBucket(myId, bucketId))
                .isInstanceOf(StudentException.class)
                .extracting("code")
                .isEqualTo(ErrorStatus._FORBIDDEN);
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