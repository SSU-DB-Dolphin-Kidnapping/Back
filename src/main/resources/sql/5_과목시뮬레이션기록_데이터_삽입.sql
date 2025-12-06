-- ===================================
-- 수강신청 시뮬레이션 History 데이터
-- test_id = 1 기준
-- ===================================

-- 시뮬레이션 실행을 위한 Test 레코드 생성
INSERT INTO test (created_at, updated_at) VALUES (NOW(), NOW());

-- test_id 변수 설정 (방금 생성한 test의 id)
SET @test_id = LAST_INSERT_ID();

-- ========================================
-- 학생 1번 (bestBucket = 1) 시뮬레이션 결과
-- ========================================
-- Bucket 1: 모두 성공 장바구니
--   - bucket_element 1: teach 34 (DB응용 가반) → 성공
--   - bucket_element 2: teach 32 (네트워크보안 가반) → 성공
--   - bucket_element 3: teach 41 (운영체제 가반) → 성공

INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 1, 1, NULL, NOW(), NOW()),  -- teach 34 성공
(@test_id, 2, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 3, 1, NULL, NOW(), NOW());  -- teach 41 성공

-- ========================================
-- 학생 2-11번 (bestBucket = 5-14) 시뮬레이션 결과
-- ========================================
-- 각 학생: 소분설(38) + 운영체제(41) + DB응용(34)
-- 반응 시간 기준 정렬:
--   학생 5 (0.29초) → 가장 빠름
--   학생 14 (0.28초) → 가장 빠름!
--   학생 10 (0.31초)
--   학생 3 (0.32초)
--   학생 8 (0.33초)
--   학생 12 (0.34초)
--   학생 2 (0.35초)
--   학생 7 (0.36초)
--   학생 9 (0.37초)
--   학생 4 (0.38초)
--   학생 11 (0.40초)
--   학생 6 (0.41초)

-- 학생 2 (avg_reaction_time: 0.35초, bestBucket=5)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 10, 1, NULL, NOW(), NOW()),  -- bucket 5, priority 1: teach 38 성공
(@test_id, 11, 1, NULL, NOW(), NOW()),  -- bucket 5, priority 2: teach 41 성공
(@test_id, 12, 1, NULL, NOW(), NOW());  -- bucket 5, priority 3: teach 34 성공

-- 학생 3 (avg_reaction_time: 0.32초, bestBucket=6)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 13, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 14, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 15, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 4 (avg_reaction_time: 0.38초, bestBucket=7)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 16, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 17, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 18, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 5 (avg_reaction_time: 0.29초, bestBucket=8) - 매우 빠름!
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 19, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 20, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 21, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 6 (avg_reaction_time: 0.41초, bestBucket=9)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 22, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 23, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 24, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 7 (avg_reaction_time: 0.36초, bestBucket=10)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 25, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 26, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 27, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 8 (avg_reaction_time: 0.33초, bestBucket=11)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 28, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 29, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 30, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 9 (avg_reaction_time: 0.37초, bestBucket=12)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 31, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 32, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 33, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 10 (avg_reaction_time: 0.31초, bestBucket=13)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 34, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 35, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 36, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- 학생 11 (avg_reaction_time: 0.40초, bestBucket=14)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 37, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 38, 1, NULL, NOW(), NOW()),  -- teach 41 성공
(@test_id, 39, 1, NULL, NOW(), NOW());  -- teach 34 성공

-- ========================================
-- 학생 12-21번 (bestBucket = 15-24) 시뮬레이션 결과
-- ========================================
-- 각 학생: 소분설(38) + 네트워크보안(32) + DB응용(35)

-- 학생 12 (avg_reaction_time: 0.34초, bestBucket=15)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 40, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 41, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 42, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 13 (avg_reaction_time: 0.39초, bestBucket=16)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 43, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 44, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 45, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 14 (avg_reaction_time: 0.28초, bestBucket=17) - 가장 빠름!
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 46, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 47, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 48, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 15 (avg_reaction_time: 0.42초, bestBucket=18) - 가장 느림, teach 38 탈락!
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 49, 0, '정원 초과', NOW(), NOW()),  -- teach 38 실패!
(@test_id, 50, 1, NULL, NOW(), NOW()),         -- teach 32 성공
(@test_id, 51, 1, NULL, NOW(), NOW());         -- teach 35 성공

-- 학생 16 (avg_reaction_time: 0.30초, bestBucket=19)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 52, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 53, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 54, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 17 (avg_reaction_time: 0.35초, bestBucket=20)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 55, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 56, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 57, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 18 (avg_reaction_time: 0.33초, bestBucket=21)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 58, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 59, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 60, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 19 (avg_reaction_time: 0.37초, bestBucket=22)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 61, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 62, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 63, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 20 (avg_reaction_time: 0.31초, bestBucket=23)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 64, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 65, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 66, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- 학생 21 (avg_reaction_time: 0.36초, bestBucket=24)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 67, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 68, 1, NULL, NOW(), NOW()),  -- teach 32 성공
(@test_id, 69, 1, NULL, NOW(), NOW());  -- teach 35 성공

-- ========================================
-- 학생 22-50번 시뮬레이션 결과
-- ========================================
-- 나머지 학생들도 동일한 패턴으로 생성
-- 대부분 성공하지만 반응 시간이 매우 느린 일부 학생은 teach 38 실패

-- 학생 22-31: 소분설(38) + 운영체제(42) + 데이터사이언스(37)
-- 학생 32-41: 소분설(38) + DB응용(36) + 네트워크보안(33)
-- 학생 42-50: 소분설(38) + 운영체제(43) + DB응용(34)

-- 학생 22 (avg_reaction_time: 0.34초, bestBucket=25)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 70, 1, NULL, NOW(), NOW()),  -- teach 38 성공
(@test_id, 71, 1, NULL, NOW(), NOW()),  -- teach 42 성공
(@test_id, 72, 1, NULL, NOW(), NOW());  -- teach 37 성공

-- 학생 23 (avg_reaction_time: 0.38초, bestBucket=26)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 73, 1, NULL, NOW(), NOW()),
(@test_id, 74, 1, NULL, NOW(), NOW()),
(@test_id, 75, 1, NULL, NOW(), NOW());

-- 학생 24 (avg_reaction_time: 0.32초, bestBucket=27)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 76, 1, NULL, NOW(), NOW()),
(@test_id, 77, 1, NULL, NOW(), NOW()),
(@test_id, 78, 1, NULL, NOW(), NOW());

-- 학생 25 (avg_reaction_time: 0.40초, bestBucket=28)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 79, 1, NULL, NOW(), NOW()),
(@test_id, 80, 1, NULL, NOW(), NOW()),
(@test_id, 81, 1, NULL, NOW(), NOW());

-- 학생 26 (avg_reaction_time: 0.29초, bestBucket=29)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 82, 1, NULL, NOW(), NOW()),
(@test_id, 83, 1, NULL, NOW(), NOW()),
(@test_id, 84, 1, NULL, NOW(), NOW());

-- 학생 27 (avg_reaction_time: 0.41초, bestBucket=30)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 85, 1, NULL, NOW(), NOW()),
(@test_id, 86, 1, NULL, NOW(), NOW()),
(@test_id, 87, 1, NULL, NOW(), NOW());

-- 학생 28 (avg_reaction_time: 0.35초, bestBucket=31)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 88, 1, NULL, NOW(), NOW()),
(@test_id, 89, 1, NULL, NOW(), NOW()),
(@test_id, 90, 1, NULL, NOW(), NOW());

-- 학생 29 (avg_reaction_time: 0.33초, bestBucket=32)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 91, 1, NULL, NOW(), NOW()),
(@test_id, 92, 1, NULL, NOW(), NOW()),
(@test_id, 93, 1, NULL, NOW(), NOW());

-- 학생 30 (avg_reaction_time: 0.37초, bestBucket=33)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 94, 1, NULL, NOW(), NOW()),
(@test_id, 95, 1, NULL, NOW(), NOW()),
(@test_id, 96, 1, NULL, NOW(), NOW());

-- 학생 31 (avg_reaction_time: 0.31초, bestBucket=34)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 97, 1, NULL, NOW(), NOW()),
(@test_id, 98, 1, NULL, NOW(), NOW()),
(@test_id, 99, 1, NULL, NOW(), NOW());

-- 학생 32-41: 소분설(38) + DB응용(36) + 네트워크보안(33)

-- 학생 32 (avg_reaction_time: 0.39초, bestBucket=35)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 100, 1, NULL, NOW(), NOW()),
(@test_id, 101, 1, NULL, NOW(), NOW()),
(@test_id, 102, 1, NULL, NOW(), NOW());

-- 학생 33 (avg_reaction_time: 0.36초, bestBucket=36)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 103, 1, NULL, NOW(), NOW()),
(@test_id, 104, 1, NULL, NOW(), NOW()),
(@test_id, 105, 1, NULL, NOW(), NOW());

-- 학생 34 (avg_reaction_time: 0.34초, bestBucket=37)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 106, 1, NULL, NOW(), NOW()),
(@test_id, 107, 1, NULL, NOW(), NOW()),
(@test_id, 108, 1, NULL, NOW(), NOW());

-- 학생 35 (avg_reaction_time: 0.38초, bestBucket=38)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 109, 1, NULL, NOW(), NOW()),
(@test_id, 110, 1, NULL, NOW(), NOW()),
(@test_id, 111, 1, NULL, NOW(), NOW());

-- 학생 36 (avg_reaction_time: 0.30초, bestBucket=39)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 112, 1, NULL, NOW(), NOW()),
(@test_id, 113, 1, NULL, NOW(), NOW()),
(@test_id, 114, 1, NULL, NOW(), NOW());

-- 학생 37 (avg_reaction_time: 0.42초, bestBucket=40)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 115, 1, NULL, NOW(), NOW()),
(@test_id, 116, 1, NULL, NOW(), NOW()),
(@test_id, 117, 1, NULL, NOW(), NOW());

-- 학생 38 (avg_reaction_time: 0.32초, bestBucket=41)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 118, 1, NULL, NOW(), NOW()),
(@test_id, 119, 1, NULL, NOW(), NOW()),
(@test_id, 120, 1, NULL, NOW(), NOW());

-- 학생 39 (avg_reaction_time: 0.35초, bestBucket=42)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 121, 1, NULL, NOW(), NOW()),
(@test_id, 122, 1, NULL, NOW(), NOW()),
(@test_id, 123, 1, NULL, NOW(), NOW());

-- 학생 40 (avg_reaction_time: 0.37초, bestBucket=43)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 124, 1, NULL, NOW(), NOW()),
(@test_id, 125, 1, NULL, NOW(), NOW()),
(@test_id, 126, 1, NULL, NOW(), NOW());

-- 학생 41 (avg_reaction_time: 0.33초, bestBucket=44)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 127, 1, NULL, NOW(), NOW()),
(@test_id, 128, 1, NULL, NOW(), NOW()),
(@test_id, 129, 1, NULL, NOW(), NOW());

-- 학생 42-50: 소분설(38) + 운영체제(43) + DB응용(34)

-- 학생 42 (avg_reaction_time: 0.36초, bestBucket=45)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 130, 1, NULL, NOW(), NOW()),
(@test_id, 131, 1, NULL, NOW(), NOW()),
(@test_id, 132, 1, NULL, NOW(), NOW());

-- 학생 43 (avg_reaction_time: 0.34초, bestBucket=46)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 133, 1, NULL, NOW(), NOW()),
(@test_id, 134, 1, NULL, NOW(), NOW()),
(@test_id, 135, 1, NULL, NOW(), NOW());

-- 학생 44 (avg_reaction_time: 0.38초, bestBucket=47)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 136, 1, NULL, NOW(), NOW()),
(@test_id, 137, 1, NULL, NOW(), NOW()),
(@test_id, 138, 1, NULL, NOW(), NOW());

-- 학생 45 (avg_reaction_time: 0.31초, bestBucket=48)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 139, 1, NULL, NOW(), NOW()),
(@test_id, 140, 1, NULL, NOW(), NOW()),
(@test_id, 141, 1, NULL, NOW(), NOW());

-- 학생 46 (avg_reaction_time: 0.39초, bestBucket=49)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 142, 1, NULL, NOW(), NOW()),
(@test_id, 143, 1, NULL, NOW(), NOW()),
(@test_id, 144, 1, NULL, NOW(), NOW());

-- 학생 47 (avg_reaction_time: 0.35초, bestBucket=50)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 145, 1, NULL, NOW(), NOW()),
(@test_id, 146, 1, NULL, NOW(), NOW()),
(@test_id, 147, 1, NULL, NOW(), NOW());

-- 학생 48 (avg_reaction_time: 0.32초, bestBucket=51)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 148, 1, NULL, NOW(), NOW()),
(@test_id, 149, 1, NULL, NOW(), NOW()),
(@test_id, 150, 1, NULL, NOW(), NOW());

-- 학생 49 (avg_reaction_time: 0.40초, bestBucket=52)
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 151, 1, NULL, NOW(), NOW()),
(@test_id, 152, 1, NULL, NOW(), NOW()),
(@test_id, 153, 1, NULL, NOW(), NOW());

-- 학생 50 (avg_reaction_time: 0.28초, bestBucket=53) - 가장 빠름!
INSERT INTO history (test_id, bucket_element_id, is_success, failed_reason, created_at, updated_at) VALUES
(@test_id, 154, 1, NULL, NOW(), NOW()),
(@test_id, 155, 1, NULL, NOW(), NOW()),
(@test_id, 156, 1, NULL, NOW(), NOW());

-- ========================================
-- 시뮬레이션 결과 요약
-- ========================================
-- 총 50명 학생, 각 3개 과목 신청 = 150개 history 레코드
--
-- teach 38 (소프트웨어분석및설계 가반, 정원 50):
--   - 49명 신청, 48명 성공, 1명 실패 (학생 15 탈락)
--   - 가장 느린 학생이 탈락 (avg_reaction_time: 0.42초)
--
-- 나머지 과목들:
--   - 모두 정원 여유가 있어 전원 성공
--   - teach 41, 42, 43 (운영체제): 총 29명 신청, 정원 충분
--   - teach 32, 33 (네트워크보안): 총 20명 신청, 정원 충분
--   - teach 34, 35, 36 (DB응용): 총 29명 신청, 정원 충분
--   - teach 37 (데이터사이언스): 10명 신청, 정원 60명
--
-- 전체 성공률: 149/150 = 99.3%
-- ========================================
