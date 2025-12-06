-- ============================================================
-- 2) 데이터 조작 스크립트 (DML)
-- 주제: 1번 학생(김테스트)의 수강신청 시뮬레이션 및 결과 분석
-- ============================================================

-- ------------------------------------------------------------
-- 1. 삽입 (INSERT)
-- 학생 생성 대신, '새로운 장바구니'를 만들고 과목을 담습니다.
-- ------------------------------------------------------------

-- 1-1. 1번 학생에게 '플랜 B'라는 새로운 장바구니 생성
INSERT INTO bucket (student_id, name, created_at, updated_at)
VALUES (1, '플랜 B', NOW(), NOW());

-- 1-2. 방금 만든 장바구니에 '인공지능(예시)' 과목 담기
-- (실습 편의를 위해 teach_id=21 '머신러닝'을 담는다고 가정)
INSERT INTO bucket_element (bucket_id, teach_id, priority, created_at, updated_at)
VALUES (
           (SELECT id FROM bucket WHERE student_id = 1 AND name = '플랜 B' LIMIT 1),
           21, -- 머신러닝
           1,
           NOW(),
           NOW()
       );

-- (참고) 시뮬레이션 결과 데이터(History)는 data.sql에 없으므로,
-- 조회 쿼리 시연을 위해 가상의 결과 데이터를 넣어줍니다.
INSERT INTO test (created_at, updated_at) VALUES (NOW(), NOW()); -- 테스트 회차 생성

-- 1번 학생의 기존 장바구니 항목들에 대한 결과(성공/실패) 생성
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at)
VALUES
    ((SELECT MAX(id) FROM test), 1, 1, NULL, NOW(), NOW()),       -- Bucket1: DB응용 (성공)
    ((SELECT MAX(id) FROM test), 2, 1, NULL, NOW(), NOW()),       -- Bucket1: 네트워크보안 (성공)
    ((SELECT MAX(id) FROM test), 4, 0, '정원 초과', NOW(), NOW()),  -- Bucket2: 소분설(가) (실패)
    ((SELECT MAX(id) FROM test), 5, 1, NULL, NOW(), NOW());       -- Bucket2: 소분설(나) (대체성공)


-- ------------------------------------------------------------
-- 2. 삭제 (DELETE)
-- ------------------------------------------------------------

-- 방금 '플랜 B'에 담았던 '머신러닝' 과목이 마음에 안 들어서 삭제
DELETE FROM bucket_element
WHERE teach_id = 21
  AND bucket_id = (SELECT id FROM bucket WHERE student_id = 1 AND name = '플랜 B' LIMIT 1);


-- ------------------------------------------------------------
-- 3. 변경 (UPDATE)
-- ------------------------------------------------------------

-- 3-1. 1번 학생 닉네임 변경 ('test_user' -> 'graduating_soon')
UPDATE student
SET nickname = 'graduating_soon', updated_at = NOW()
WHERE id = 1;

-- 3-2. 1번 학생의 '모두 성공 장바구니(id=1)'에 있는
-- '네트워크보안(teach_id=32)'의 우선순위를 2순위에서 1순위로 변경
UPDATE bucket_element
SET priority = 1, updated_at = NOW()
WHERE bucket_id = 1 AND teach_id = 32;


-- ------------------------------------------------------------
-- 4. 검색 (SELECT 8개) - 시뮬레이션 심층 분석
-- ------------------------------------------------------------

-- 4-1. [강의] 강의명에 '프로그래밍'가 포함된 강의 목록 조회
SELECT c.name AS course_name, p.professor_name, t.class_name, ti.day_of_the_week, ti.start_time, ti.end_time
FROM teach t
         JOIN course c ON t.course_id = c.id
         JOIN professor p ON t.professor_id = p.id
         JOIN shoppingcart.teach_info ti On t.id = ti.teach_id
WHERE c.name LIKE '%프로그래밍%';

-- 4-2. [장바구니] 닉네임이 바뀐 'graduating_soon(구 김테스트)' 학생의 장바구니 목록 조회
SELECT b.name AS bucket_name, c.name AS course_name, be.priority
FROM bucket b
         JOIN bucket_element be ON b.id = be.bucket_id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
         JOIN student s ON b.student_id = s.id
WHERE s.id = 1
ORDER BY b.id, be.priority;

-- 4-3. [시뮬레이션-개인] 1번 학생의 수강신청 상세 결과(성공/실패 여부 및 사유) 조회
-- "내가 신청한 과목들이 어떻게 되었을까?"
SELECT b.name AS bucket_name, c.name AS course_name, h.is_success, h.failed_reason
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN bucket b ON be.bucket_id = b.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
WHERE b.student_id = 1;

-- 4-4. [시뮬레이션-통계] 가장 최근 시뮬레이션(Test)의 전체 성공/실패 횟수 집계
SELECT h.test_id,
       COUNT(CASE WHEN h.is_success = 1 THEN 1 END) AS total_success,
       COUNT(CASE WHEN h.is_success = 0 THEN 1 END) AS total_fail
FROM history h
WHERE h.test_id = (SELECT MAX(id) FROM test)
GROUP BY h.test_id;

-- 4-5. [시뮬레이션-분석] '정원 초과'로 가장 많이 실패한 인기 과목 TOP 5 조회
SELECT c.name AS course_name, p.professor_name, COUNT(h.id) AS fail_count
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
         JOIN professor p ON t.professor_id = p.id
WHERE h.is_success = 0 AND h.failed_reason = '정원 초과'
GROUP BY t.id
ORDER BY fail_count DESC
LIMIT 5;

-- 4-6. [시뮬레이션-분석] 이번 테스트에서 'All Fail' (성공이 0개)인 비운의 학생 명단 조회
SELECT s.student_name, s.student_number
FROM student s
WHERE s.id IN (
    SELECT b.student_id
    FROM history h
             JOIN bucket_element be ON h.bucket_element_id = be.id
             JOIN bucket b ON be.bucket_id = b.id
    WHERE h.test_id = (SELECT MAX(id) FROM test)
    GROUP BY b.student_id
    HAVING SUM(CASE WHEN h.is_success = 1 THEN 1 ELSE 0 END) = 0
);

-- 4-7. [시뮬레이션-개인] 1번 학생이 이번에 확보한 '총 학점' 계산
SELECT s.student_name, SUM(c.credit) AS total_earned_credits
FROM history h
         JOIN bucket_element be ON h.bucket_element_id = be.id
         JOIN bucket b ON be.bucket_id = b.id
         JOIN student s ON b.student_id = s.id
         JOIN teach t ON be.teach_id = t.id
         JOIN course c ON t.course_id = c.id
WHERE s.id = 1 AND h.is_success = 1
GROUP BY s.id;

-- 4-8. [시뮬레이션-통계] 실패 원인별 비중 분석 (어떤 이유로 많이 떨어졌나?)
SELECT h.failed_reason, COUNT(*) AS count
FROM history h
WHERE h.is_success = 0 AND h.test_id = (SELECT MAX(id) FROM test)
GROUP BY h.failed_reason
ORDER BY count DESC;