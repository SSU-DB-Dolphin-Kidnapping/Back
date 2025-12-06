package Dolphin.ShoppingCart.domain.student.application;

import Dolphin.ShoppingCart.domain.course.entity.Teach;
import Dolphin.ShoppingCart.domain.course.entity.TeachInfo;
import Dolphin.ShoppingCart.domain.course.repository.TeachRepository;
import Dolphin.ShoppingCart.domain.student.dto.bucket.*;
import Dolphin.ShoppingCart.domain.student.entity.Bucket;
import Dolphin.ShoppingCart.domain.student.entity.BucketElement;
import Dolphin.ShoppingCart.domain.student.entity.Student;
import Dolphin.ShoppingCart.domain.student.exception.StudentException;
import Dolphin.ShoppingCart.domain.student.repository.BucketElementRepository;
import Dolphin.ShoppingCart.domain.student.repository.BucketRepository;
import Dolphin.ShoppingCart.domain.student.repository.StudentRepository;
import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BucketServiceImpl implements BucketService {

    private final StudentRepository studentRepository;
    private final BucketRepository bucketRepository;
    private final BucketElementRepository bucketElementRepository;
    private final TeachRepository teachRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BucketResponseDTO> getBucketElements(Long studentId, Long bucketId) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        List<BucketElement> elements = bucketElementRepository.findAllByBucketIdWithDetails(bucketId);

        return elements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addCourse(Long studentId, Long bucketId, BucketAddRequestDTO request) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        if (bucketElementRepository.existsByBucketIdAndTeachId(bucketId, request.getTeachId())) {
            throw new StudentException(ErrorStatus._BAD_REQUEST);
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        Teach teach = teachRepository.findById(request.getTeachId())
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (student.getGrade() != null && teach.getTargetGrade() != null) {
            if (!student.getGrade().equals(teach.getTargetGrade())) {
                throw new StudentException(ErrorStatus._BAD_REQUEST);
            }
        }

        Integer maxPriority = bucketElementRepository.findMaxPriorityByBucketId(bucketId).orElse(0);

        BucketElement newElement = BucketElement.builder()
                .bucket(bucket)
                .teach(teach)
                .priority(maxPriority + 1)
                .build();

        bucketElementRepository.save(newElement);
    }

    @Override
    public void deleteCourse(Long studentId, Long bucketId, Long bucketElementId) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        BucketElement element = bucketElementRepository.findById(bucketElementId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!element.getBucket().getId().equals(bucketId)) {
            throw new StudentException(ErrorStatus._BAD_REQUEST);
        }

        bucketElementRepository.delete(element);
    }

    @Override
    public void updatePriorities(Long studentId, Long bucketId, List<BucketPriorityRequestDTO> requests) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        for (BucketPriorityRequestDTO req : requests) {
            BucketElement element = bucketElementRepository.findById(req.getBucketElementId())
                    .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

            if (!element.getBucket().getId().equals(bucketId)) {
                throw new StudentException(ErrorStatus._BAD_REQUEST);
            }

            element.updatePriority(req.getPriority());
        }
    }

    @Override
    public void updateAlternateCourse(Long studentId, Long bucketId, Long bucketElementId, BucketAlternateRequestDTO request) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        BucketElement targetElement = bucketElementRepository.findById(bucketElementId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!targetElement.getBucket().getId().equals(bucketId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        if (request.getAlternateTeachId() == null) {
            targetElement.updateSubElement(null);
        } else {
            BucketElement altElement = bucketElementRepository.findByBucketIdAndTeachId(bucketId, request.getAlternateTeachId())
                    .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST)); // 장바구니에 없는 과목은 대체과목 설정 불가 정책

            if (targetElement.getId().equals(altElement.getId())) {
                throw new StudentException(ErrorStatus._BAD_REQUEST);
            }

            targetElement.updateSubElement(altElement);
        }
    }

    @Override
    public void removeAlternateCourse(Long studentId, Long bucketId, Long bucketElementId) {
        // 장바구니가 해당 학생의 것인지 검증
        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        BucketElement targetElement = bucketElementRepository.findById(bucketElementId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!targetElement.getBucket().getId().equals(bucketId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        targetElement.updateSubElement(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BucketSummaryDTO> getMyBuckets(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        List<Bucket> buckets = bucketRepository.findAllByStudent(student);

        return buckets.stream()
                .map(bucket -> BucketSummaryDTO.builder()
                        .bucketId(bucket.getId())
                        .name(bucket.getName())
                        .isBest(bucket.getId().equals(student.getBestBucket()))
                        .createdAt(bucket.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void createBucket(Long studentId, BucketCreateRequestDTO request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        Bucket newBucket = Bucket.builder()
                .student(student)
                .name(request.getName())
                // 초기에는 빈 리스트
                .bucketElements(new ArrayList<>())
                .build();

        Bucket savedBucket = bucketRepository.save(newBucket);

        if (student.getBestBucket() == null) {
            student.changeBestBucket(savedBucket.getId());
        }
    }

    @Override
    public void deleteBucket(Long studentId, Long bucketId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        if (bucketId.equals(student.getBestBucket())) {
            throw new StudentException(ErrorStatus._BAD_REQUEST);
        }

        bucketRepository.delete(bucket);
    }

    @Override
    public void setBestBucket(Long studentId, Long bucketId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (!bucket.getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        student.changeBestBucket(bucketId);
    }

    private Long getBestBucketId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        if (student.getBestBucket() == null) {
            throw new StudentException(ErrorStatus._BAD_REQUEST); // 대표 장바구니 없음
        }
        return student.getBestBucket();
    }

    private BucketResponseDTO convertToDTO(BucketElement element) {
        Teach teach = element.getTeach();

        String timePlace = "";
        if (teach.getTeachInfos() != null && !teach.getTeachInfos().isEmpty()) {
            TeachInfo info = teach.getTeachInfos().get(0);
            timePlace = info.getDayOfTheWeek().getDescription().charAt(0) + " " +
                    info.getStartTime() + " (" + info.getClassroom() + ")";
        }

        String altName = "-";
        Long altTeachId = null;
        if (element.getSubElement() != null) {
            altName = element.getSubElement().getTeach().getCourse().getName();
            altTeachId = element.getSubElement().getTeach().getId();
        }

        return BucketResponseDTO.builder()
                .bucketElementId(element.getId())
                .teachId(teach.getId())
                .priority(element.getPriority())
                .majorType(teach.getCourse().getMajority().getDescription())
                .courseName(teach.getCourse().getName())
                .professorName(teach.getProfessor().getProfessorName())
                .timePlace(timePlace)
                .alternateTeachId(altTeachId)
                .alternateSubjectName(altName)
                .build();
    }
}