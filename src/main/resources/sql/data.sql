-- ===================================
-- 수강신청 시뮬레이션 초기 데이터
-- 25-2학기 실제 강의 데이터 기반
-- ===================================

SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 1. College (대학)
-- ========================================
INSERT INTO college (id, name, created_at, updated_at) VALUES
(1, 'IT대학', NOW(), NOW());

-- ========================================
-- 2. Department (학과)
-- ========================================
INSERT INTO department (id, college_id, name, created_at, updated_at) VALUES
(1, 1, '소프트웨어학부', NOW(), NOW());

-- ========================================
-- 3. Student (학생) - 50명 (모두 3학년)
-- ========================================
-- 참고: 모든 학생은 3학년으로 설정되어 target_grade가 NULL 또는 3인 강의만 신청 가능
INSERT INTO student (id, department_id, nickname, password, student_name, avg_reaction_time, best_bucket, created_at, updated_at) VALUES
-- 테스트 사용자 (id = 1, 3학년, 4개 장바구니 보유)
(1, 1, 'test_user', 'password123', '김테스트', 0.30, 1, NOW(), NOW()),
-- 정원을 채울 학생들 (id = 2-50, teach 38에 신청)
(2, 1, 'student02', 'password123', '이학생', 0.35, 5, NOW(), NOW()),
(3, 1, 'student03', 'password123', '박학생', 0.32, 6, NOW(), NOW()),
(4, 1, 'student04', 'password123', '최학생', 0.38, 7, NOW(), NOW()),
(5, 1, 'student05', 'password123', '정학생', 0.29, 8, NOW(), NOW()),
(6, 1, 'student06', 'password123', '강학생', 0.41, 9, NOW(), NOW()),
(7, 1, 'student07', 'password123', '조학생', 0.36, 10, NOW(), NOW()),
(8, 1, 'student08', 'password123', '윤학생', 0.33, 11, NOW(), NOW()),
(9, 1, 'student09', 'password123', '임학생', 0.37, 12, NOW(), NOW()),
(10, 1, 'student10', 'password123', '한학생', 0.31, 13, NOW(), NOW()),
(11, 1, 'student11', 'password123', '신학생', 0.40, 14, NOW(), NOW()),
(12, 1, 'student12', 'password123', '서학생', 0.34, 15, NOW(), NOW()),
(13, 1, 'student13', 'password123', '권학생', 0.39, 16, NOW(), NOW()),
(14, 1, 'student14', 'password123', '황학생', 0.28, 17, NOW(), NOW()),
(15, 1, 'student15', 'password123', '배학생', 0.42, 18, NOW(), NOW()),
(16, 1, 'student16', 'password123', '송학생', 0.30, 19, NOW(), NOW()),
(17, 1, 'student17', 'password123', '류학생', 0.35, 20, NOW(), NOW()),
(18, 1, 'student18', 'password123', '전학생', 0.33, 21, NOW(), NOW()),
(19, 1, 'student19', 'password123', '홍학생', 0.37, 22, NOW(), NOW()),
(20, 1, 'student20', 'password123', '고학생', 0.31, 23, NOW(), NOW()),
(21, 1, 'student21', 'password123', '문학생', 0.36, 24, NOW(), NOW()),
(22, 1, 'student22', 'password123', '손학생', 0.34, 25, NOW(), NOW()),
(23, 1, 'student23', 'password123', '양학생', 0.38, 26, NOW(), NOW()),
(24, 1, 'student24', 'password123', '배학생2', 0.32, 27, NOW(), NOW()),
(25, 1, 'student25', 'password123', '백학생', 0.40, 28, NOW(), NOW()),
(26, 1, 'student26', 'password123', '허학생', 0.29, 29, NOW(), NOW()),
(27, 1, 'student27', 'password123', '남학생', 0.41, 30, NOW(), NOW()),
(28, 1, 'student28', 'password123', '심학생', 0.35, 31, NOW(), NOW()),
(29, 1, 'student29', 'password123', '유학생', 0.33, 32, NOW(), NOW()),
(30, 1, 'student30', 'password123', '노학생', 0.37, 33, NOW(), NOW()),
(31, 1, 'student31', 'password123', '하학생', 0.31, 34, NOW(), NOW()),
(32, 1, 'student32', 'password123', '곽학생', 0.39, 35, NOW(), NOW()),
(33, 1, 'student33', 'password123', '성학생', 0.36, 36, NOW(), NOW()),
(34, 1, 'student34', 'password123', '차학생', 0.34, 37, NOW(), NOW()),
(35, 1, 'student35', 'password123', '주학생', 0.38, 38, NOW(), NOW()),
(36, 1, 'student36', 'password123', '우학생', 0.30, 39, NOW(), NOW()),
(37, 1, 'student37', 'password123', '구학생', 0.42, 40, NOW(), NOW()),
(38, 1, 'student38', 'password123', '라학생', 0.32, 41, NOW(), NOW()),
(39, 1, 'student39', 'password123', '진학생', 0.35, 42, NOW(), NOW()),
(40, 1, 'student40', 'password123', '옥학생', 0.37, 43, NOW(), NOW()),
(41, 1, 'student41', 'password123', '추학생', 0.33, 44, NOW(), NOW()),
-- 추가 학생들
(42, 1, 'student42', 'password123', '민학생', 0.36, 45, NOW(), NOW()),
(43, 1, 'student43', 'password123', '석학생', 0.34, 46, NOW(), NOW()),
(44, 1, 'student44', 'password123', '안학생', 0.38, 47, NOW(), NOW()),
(45, 1, 'student45', 'password123', '봉학생', 0.31, 48, NOW(), NOW()),
(46, 1, 'student46', 'password123', '범학생', 0.39, 49, NOW(), NOW()),
(47, 1, 'student47', 'password123', '탁학생', 0.35, 50, NOW(), NOW()),
(48, 1, 'student48', 'password123', '금학생', 0.32, 51, NOW(), NOW()),
(49, 1, 'student49', 'password123', '길학생', 0.40, 52, NOW(), NOW()),
(50, 1, 'student50', 'password123', '예학생', 0.28, 53, NOW(), NOW());

-- ========================================
-- 4. Bucket (장바구니)
-- ========================================
INSERT INTO bucket (id, student_id, name, created_at, updated_at) VALUES
-- 학생 1: 테스트 사용자 (4개 장바구니 - 각각 다른 제약사항 테스트)
(1, 1, '모두 성공 장바구니', NOW(), NOW()),
(2, 1, '정원 초과 + 대체 성공 장바구니', NOW(), NOW()),
(3, 1, '시간대 충돌 장바구니', NOW(), NOW()),
(4, 1, '같은 course_id 장바구니', NOW(), NOW()),
-- 학생 2-50: teach 38 신청자들 (정원 50명 채우기)
(5, 2, '1지망', NOW(), NOW()),
(6, 3, '1지망', NOW(), NOW()),
(7, 4, '1지망', NOW(), NOW()),
(8, 5, '1지망', NOW(), NOW()),
(9, 6, '1지망', NOW(), NOW()),
(10, 7, '1지망', NOW(), NOW()),
(11, 8, '1지망', NOW(), NOW()),
(12, 9, '1지망', NOW(), NOW()),
(13, 10, '1지망', NOW(), NOW()),
(14, 11, '1지망', NOW(), NOW()),
(15, 12, '1지망', NOW(), NOW()),
(16, 13, '1지망', NOW(), NOW()),
(17, 14, '1지망', NOW(), NOW()),
(18, 15, '1지망', NOW(), NOW()),
(19, 16, '1지망', NOW(), NOW()),
(20, 17, '1지망', NOW(), NOW()),
(21, 18, '1지망', NOW(), NOW()),
(22, 19, '1지망', NOW(), NOW()),
(23, 20, '1지망', NOW(), NOW()),
(24, 21, '1지망', NOW(), NOW()),
(25, 22, '1지망', NOW(), NOW()),
(26, 23, '1지망', NOW(), NOW()),
(27, 24, '1지망', NOW(), NOW()),
(28, 25, '1지망', NOW(), NOW()),
(29, 26, '1지망', NOW(), NOW()),
(30, 27, '1지망', NOW(), NOW()),
(31, 28, '1지망', NOW(), NOW()),
(32, 29, '1지망', NOW(), NOW()),
(33, 30, '1지망', NOW(), NOW()),
(34, 31, '1지망', NOW(), NOW()),
(35, 32, '1지망', NOW(), NOW()),
(36, 33, '1지망', NOW(), NOW()),
(37, 34, '1지망', NOW(), NOW()),
(38, 35, '1지망', NOW(), NOW()),
(39, 36, '1지망', NOW(), NOW()),
(40, 37, '1지망', NOW(), NOW()),
(41, 38, '1지망', NOW(), NOW()),
(42, 39, '1지망', NOW(), NOW()),
(43, 40, '1지망', NOW(), NOW()),
(44, 41, '1지망', NOW(), NOW()),
(45, 42, '1지망', NOW(), NOW()),
(46, 43, '1지망', NOW(), NOW()),
(47, 44, '1지망', NOW(), NOW()),
(48, 45, '1지망', NOW(), NOW()),
(49, 46, '1지망', NOW(), NOW()),
(50, 47, '1지망', NOW(), NOW()),
(51, 48, '1지망', NOW(), NOW()),
(52, 49, '1지망', NOW(), NOW()),
(53, 50, '1지망', NOW(), NOW());

-- ========================================
-- 5. BucketElement (장바구니 항목)
-- ========================================

-- 학생 1의 4개 장바구니 시나리오

-- [Bucket 1] 모두 성공 장바구니
-- - teach 34 (데이터베이스응용 가반, course_id=13, 화 15:00-16:15, 금 12:00-13:15)
-- - teach 32 (네트워크보안 가반, course_id=12, 수 09:00-10:15, 목 10:30-11:45)
-- - teach 41 (운영체제 가반, course_id=16, 월 09:00-10:15, 월 10:30-11:45)
-- → 시간 겹치지 않고, course_id 모두 다르고, 정원 충분 → 모두 성공 ✅
INSERT INTO bucket_element (id, teach_id, bucket_id, priority, sub_element_id, created_at, updated_at) VALUES
(1, 34, 1, 1, NULL, NOW(), NOW()),
(2, 32, 1, 2, NULL, NOW(), NOW()),
(3, 41, 1, 3, NULL, NOW(), NOW()),

-- [Bucket 2] 정원 초과 + 대체 성공 장바구니
-- - teach 38 (소프트웨어분석및설계 가반, course_id=15, 정원 50) → 정원 초과로 실패 ❌
-- - sub_element: teach 39 (소프트웨어분석및설계 나반, course_id=15, 정원 52) → 대체되어 성공 ✅
-- 학생 2-50 (49명)이 teach 38에 신청하여 정원 50 채움
-- → teach 38 실패 시 sub_element_id=5인 teach 39로 자동 대체
(4, 38, 2, 1, 5, NOW(), NOW()),      -- 메인 과목 (정원 초과 실패 예상, 대체 과목 ID=5)
(5, 39, 2, 2, NULL, NOW(), NOW()),   -- 대체 과목 (teach 38 실패 시 이걸로 대체)

-- [Bucket 3] 시간대 충돌 장바구니
-- - teach 38 (월수 12:00-13:15) → 성공 ✅
-- - teach 42 (월 12:00-13:15, 월 13:30-14:45) → 시간 충돌로 실패 ❌
(6, 38, 3, 1, NULL, NOW(), NOW()),
(7, 42, 3, 2, NULL, NOW(), NOW()),

-- [Bucket 4] 같은 course_id 장바구니
-- - teach 38 (course_id=15) → 성공 ✅
-- - teach 39 (course_id=15) → course_id 중복으로 실패 ❌
(8, 38, 4, 1, NULL, NOW(), NOW()),
(9, 39, 4, 2, NULL, NOW(), NOW());

-- 학생 2-50 (49명): teach 38에 우선순위 1로 신청하여 정원 50명 채우기
-- → Bucket 2의 teach 38이 정원 초과로 실패하도록 만듦
INSERT INTO bucket_element (teach_id, bucket_id, priority, sub_element_id, created_at, updated_at) VALUES
(38, 5, 1, NULL, NOW(), NOW()),
(38, 6, 1, NULL, NOW(), NOW()),
(38, 7, 1, NULL, NOW(), NOW()),
(38, 8, 1, NULL, NOW(), NOW()),
(38, 9, 1, NULL, NOW(), NOW()),
(38, 10, 1, NULL, NOW(), NOW()),
(38, 11, 1, NULL, NOW(), NOW()),
(38, 12, 1, NULL, NOW(), NOW()),
(38, 13, 1, NULL, NOW(), NOW()),
(38, 14, 1, NULL, NOW(), NOW()),
(38, 15, 1, NULL, NOW(), NOW()),
(38, 16, 1, NULL, NOW(), NOW()),
(38, 17, 1, NULL, NOW(), NOW()),
(38, 18, 1, NULL, NOW(), NOW()),
(38, 19, 1, NULL, NOW(), NOW()),
(38, 20, 1, NULL, NOW(), NOW()),
(38, 21, 1, NULL, NOW(), NOW()),
(38, 22, 1, NULL, NOW(), NOW()),
(38, 23, 1, NULL, NOW(), NOW()),
(38, 24, 1, NULL, NOW(), NOW()),
(38, 25, 1, NULL, NOW(), NOW()),
(38, 26, 1, NULL, NOW(), NOW()),
(38, 27, 1, NULL, NOW(), NOW()),
(38, 28, 1, NULL, NOW(), NOW()),
(38, 29, 1, NULL, NOW(), NOW()),
(38, 30, 1, NULL, NOW(), NOW()),
(38, 31, 1, NULL, NOW(), NOW()),
(38, 32, 1, NULL, NOW(), NOW()),
(38, 33, 1, NULL, NOW(), NOW()),
(38, 34, 1, NULL, NOW(), NOW()),
(38, 35, 1, NULL, NOW(), NOW()),
(38, 36, 1, NULL, NOW(), NOW()),
(38, 37, 1, NULL, NOW(), NOW()),
(38, 38, 1, NULL, NOW(), NOW()),
(38, 39, 1, NULL, NOW(), NOW()),
(38, 40, 1, NULL, NOW(), NOW()),
(38, 41, 1, NULL, NOW(), NOW()),
(38, 42, 1, NULL, NOW(), NOW()),
(38, 43, 1, NULL, NOW(), NOW()),
(38, 44, 1, NULL, NOW(), NOW()),
(38, 45, 1, NULL, NOW(), NOW()),
(38, 46, 1, NULL, NOW(), NOW()),
(38, 47, 1, NULL, NOW(), NOW()),
(38, 48, 1, NULL, NOW(), NOW()),
(38, 49, 1, NULL, NOW(), NOW()),
(38, 50, 1, NULL, NOW(), NOW()),
(38, 51, 1, NULL, NOW(), NOW()),
(38, 52, 1, NULL, NOW(), NOW()),
(38, 53, 1, NULL, NOW(), NOW());


SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 시뮬레이션 시나리오 설명
-- ========================================
--
-- [학생 설정]
-- - 모든 학생: 3학년 (target_grade=3 또는 NULL인 강의만 신청 가능)
--
-- [수강신청 제약사항 3가지]
-- 1. 정원 초과: max_count를 초과하면 실패
-- 2. course_id 중복: 이미 신청한 과목과 같은 course_id면 실패 (같은 과목의 다른 분반)
-- 3. 시간표 충돌: 이미 신청한 강의와 시간이 겹치면 실패
--
-- [학생 1 테스트 시나리오 - 4개 장바구니로 모든 제약사항 테스트]
--
-- **Bucket 1: 모두 성공 장바구니**
--   - teach 34 (데이터베이스응용 가반, course_id=13)
--   - teach 32 (네트워크보안 가반, course_id=12)
--   - teach 41 (운영체제 가반, course_id=16)
--   → 시간 겹치지 않고, course_id 모두 다르고, 정원 충분
--   **결과: 3개 모두 성공 ✅✅✅**
--
-- **Bucket 2: 정원 초과 + 대체 성공 장바구니**
--   - teach 38 (소프트웨어분석및설계 가반, course_id=15, 정원 50)
--     - sub_element_id=5로 대체 과목 지정
--     - 학생 2-50 (49명)이 우선순위 1로 신청하여 정원 50 채움
--     → 정원 초과로 실패 ❌
--   - teach 39 (소프트웨어분석및설계 나반, course_id=15, 정원 52)
--     - teach 38 실패 시 이 과목으로 자동 대체
--     → 대체되어 성공 ✅
--   **결과: teach 38 실패 → teach 39로 대체 성공**
--
-- **Bucket 3: 시간대 충돌 장바구니**
--   - teach 38 (월수 12:00-13:15) → 성공 ✅
--   - teach 42 (월 12:00-13:15, 월 13:30-14:45)
--     → teach 38과 월 12:00-13:15 시간 충돌로 실패 ❌
--   **결과: 1개 성공, 1개 실패 (시간 충돌)**
--
-- **Bucket 4: 같은 course_id 장바구니**
--   - teach 38 (course_id=15) → 성공 ✅
--   - teach 39 (course_id=15)
--     → teach 38과 같은 course_id로 실패 ❌
--   **결과: 1개 성공, 1개 실패 (course_id 중복)**
--
-- [정원 관리 메커니즘]
-- - teach 38: 정원 50명
-- - 학생 2-50 (49명): 모두 우선순위 1로 teach 38 신청
-- - Bucket 2의 teach 38: 우선순위 1, 정원 초과 예상
-- - Bucket 3, 4의 teach 38: 학생 2-50보다 먼저 신청하여 성공
--
-- [시간표 정보]
-- - teach 38: 월 12:00-13:15, 수 12:00-13:15 (course_id=15)
-- - teach 39: 월 15:00-16:15, 수 15:00-16:15 (course_id=15)
-- - teach 42: 월 12:00-13:15, 월 13:30-14:45 (course_id=16)
-- - teach 34: 화 15:00-16:15, 금 12:00-13:15 (course_id=13)
-- - teach 32: 수 09:00-10:15, 목 10:30-11:45 (course_id=12)
-- - teach 41: 월 09:00-10:15, 월 10:30-11:45 (course_id=16)
--
-- ========================================
