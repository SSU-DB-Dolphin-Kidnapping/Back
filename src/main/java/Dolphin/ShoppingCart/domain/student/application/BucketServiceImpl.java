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
    public List<BucketResponseDTO> getBucketList(Long studentId) {
        Long bucketId = getBestBucketId(studentId);
        List<BucketElement> elements = bucketElementRepository.findAllByBucketIdWithDetails(bucketId);

        return elements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addCourse(Long studentId, BucketAddRequestDTO request) {
        Long bucketId = getBestBucketId(studentId);

        // 중복 체크
        if (bucketElementRepository.existsByBucketIdAndTeachId(bucketId, request.getTeachId())) {
            throw new StudentException(ErrorStatus._BAD_REQUEST); // 이미 담긴 과목
        }

        Bucket bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        Teach teach = teachRepository.findById(request.getTeachId())
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        // 우선순위 결정 (마지막 + 1)
        Integer maxPriority = bucketElementRepository.findMaxPriorityByBucketId(bucketId).orElse(0);

        BucketElement newElement = BucketElement.builder()
                .bucket(bucket)
                .teach(teach)
                .priority(maxPriority + 1)
                .build();

        bucketElementRepository.save(newElement);
    }

    @Override
    public void deleteCourse(Long studentId, Long bucketElementId) {
        BucketElement element = bucketElementRepository.findById(bucketElementId)
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        // 내 장바구니의 항목인지 검증
        if (!element.getBucket().getStudent().getId().equals(studentId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        bucketElementRepository.delete(element);
    }

    @Override
    public void updatePriorities(Long studentId, List<BucketPriorityRequestDTO> requests) {
        for (BucketPriorityRequestDTO req : requests) {
            BucketElement element = bucketElementRepository.findById(req.getBucketElementId())
                    .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

            // 검증
            if (!element.getBucket().getStudent().getId().equals(studentId)) {
                throw new StudentException(ErrorStatus._FORBIDDEN);
            }

            element.updatePriority(req.getPriority());
        }
    }

    @Override
    public void updateAlternateCourse(Long studentId, BucketAlternateRequestDTO request) {
        Long bucketId = getBestBucketId(studentId);

        BucketElement targetElement = bucketElementRepository.findById(request.getBucketElementId())
                .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST));

        // 내 장바구니 검증
        if (!targetElement.getBucket().getId().equals(bucketId)) {
            throw new StudentException(ErrorStatus._FORBIDDEN);
        }

        if (request.getAlternateTeachId() == null) {
            // 대체과목 해제
            targetElement.updateSubElement(null);
        } else {
            // 대체과목으로 설정할 과목이 내 장바구니에 있어야 함
            BucketElement altElement = bucketElementRepository.findByBucketIdAndTeachId(bucketId, request.getAlternateTeachId())
                    .orElseThrow(() -> new StudentException(ErrorStatus._BAD_REQUEST)); // 장바구니에 없는 과목은 대체과목 설정 불가 정책

            // 순환 참조 방지 (A -> B -> A) 등의 로직은 생략되었으나 필요 시 추가
            if (targetElement.getId().equals(altElement.getId())) {
                throw new StudentException(ErrorStatus._BAD_REQUEST);
            }

            targetElement.updateSubElement(altElement);
        }
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

        // 시간 장소 포맷팅 (단순화: 첫번째 시간만 표시)
        String timePlace = "";
        if (teach.getTeachInfos() != null && !teach.getTeachInfos().isEmpty()) {
            TeachInfo info = teach.getTeachInfos().get(0);
            timePlace = info.getDayOfTheWeek().getDescription().substring(0, 1) + " " +
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