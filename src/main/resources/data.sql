-- ===================================
-- 초기 데이터 생성 SQL
-- 수강신청 시뮬레이션 애플리케이션
-- ===================================

-- College (대학) 데이터
INSERT INTO college (id, name, created_at, updated_at) VALUES
(1, '공과대학', NOW(), NOW()),
(2, '자연과학대학', NOW(), NOW()),
(3, '인문대학', NOW(), NOW()),
(4, '사회과학대학', NOW(), NOW());

-- Department (학과) 데이터
INSERT INTO department (id, college_id, name, created_at, updated_at) VALUES
(1, 1, '컴퓨터공학과', NOW(), NOW()),
(2, 1, '전자공학과', NOW(), NOW()),
(3, 2, '수학과', NOW(), NOW()),
(4, 2, '물리학과', NOW(), NOW()),
(5, 3, '국어국문학과', NOW(), NOW()),
(6, 4, '경영학과', NOW(), NOW());

-- Professor (교수) 데이터
INSERT INTO professor (id, department_id, professor_name, created_at, updated_at) VALUES
(1, 1, '김철수', NOW(), NOW()),
(2, 1, '이영희', NOW(), NOW()),
(3, 1, '박민수', NOW(), NOW()),
(4, 2, '최지훈', NOW(), NOW()),
(5, 3, '정수진', NOW(), NOW()),
(6, 6, '강민호', NOW(), NOW()),
(7, 1, '홍길동', NOW(), NOW()),
(8, 5, '윤서연', NOW(), NOW());

-- Course (강의) 데이터
INSERT INTO course (id, name, majority, time, credit, created_at, updated_at) VALUES
-- 전공 과목
(1, '자료구조', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(2, '알고리즘', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(3, '운영체제', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(4, '데이터베이스', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(5, '컴퓨터네트워크', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(6, '소프트웨어공학', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(7, '인공지능', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
(8, '웹프로그래밍', 'MAJOR_REQUIRED', 3.0, 3.0, NOW(), NOW()),
-- 교양 과목
(9, '미적분학', 'MAJOR_ELECTIVE', 3.0, 3.0, NOW(), NOW()),
(10, '선형대수학', 'MAJOR_ELECTIVE', 3.0, 3.0, NOW(), NOW()),
(11, '영어회화', 'MAJOR_ELECTIVE', 2.0, 2.0, NOW(), NOW()),
(12, '경영학원론', 'MAJOR_ELECTIVE', 3.0, 3.0, NOW(), NOW()),
(13, '심리학개론', 'MAJOR_ELECTIVE', 3.0, 3.0, NOW(), NOW()),
(14, '글쓰기', 'MAJOR_ELECTIVE', 2.0, 2.0, NOW(), NOW());

-- Teach (2025년 1학기 강의 개설 정보)
INSERT INTO teach (id, course_id, professor_id, year, semester, number, class_name, type, max_count, enrolled_count, remain_count, created_at, updated_at) VALUES
-- 자료구조 (3개 분반)
(1, 1, 1, 2025, 'FIRST', 1001, 'A분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
(2, 1, 2, 2025, 'FIRST', 1002, 'B분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
(3, 1, 3, 2025, 'FIRST', 1003, 'C분반', 'OFFLINE', 45, 0, 45, NOW(), NOW()),
-- 알고리즘 (2개 분반)
(4, 2, 1, 2025, 'FIRST', 2001, 'A분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
(5, 2, 7, 2025, 'FIRST', 2002, 'B분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
-- 운영체제
(6, 3, 2, 2025, 'FIRST', 3001, 'A분반', 'OFFLINE', 60, 0, 60, NOW(), NOW()),
(7, 3, 3, 2025, 'FIRST', 3002, 'B분반', 'OFFLINE', 60, 0, 60, NOW(), NOW()),
-- 데이터베이스
(8, 4, 1, 2025, 'FIRST', 4001, 'A분반', 'OFFLINE', 55, 0, 55, NOW(), NOW()),
(9, 4, 7, 2025, 'FIRST', 4002, 'B분반', 'OFFLINE', 55, 0, 55, NOW(), NOW()),
-- 컴퓨터네트워크
(10, 5, 2, 2025, 'FIRST', 5001, 'A분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
-- 소프트웨어공학
(11, 6, 3, 2025, 'FIRST', 6001, 'A분반', 'OFFLINE', 50, 0, 50, NOW(), NOW()),
-- 인공지능
(12, 7, 1, 2025, 'FIRST', 7001, 'A분반', 'OFFLINE', 40, 0, 40, NOW(), NOW()),
(13, 7, 7, 2025, 'FIRST', 7002, 'B분반', 'OFFLINE', 40, 0, 40, NOW(), NOW()),
-- 웹프로그래밍
(14, 8, 3, 2025, 'FIRST', 8001, 'A분반', 'OFFLINE', 45, 0, 45, NOW(), NOW()),
-- 교양 과목
(15, 9, 5, 2025, 'FIRST', 9001, 'A분반', 'OFFLINE', 100, 0, 100, NOW(), NOW()),
(16, 10, 5, 2025, 'FIRST', 10001, 'A분반', 'OFFLINE', 100, 0, 100, NOW(), NOW()),
(17, 11, 8, 2025, 'FIRST', 11001, 'A분반', 'OFFLINE', 30, 0, 30, NOW(), NOW()),
(18, 12, 6, 2025, 'FIRST', 12001, 'A분반', 'OFFLINE', 80, 0, 80, NOW(), NOW()),
(19, 13, 8, 2025, 'FIRST', 13001, 'A분반', 'OFFLINE', 70, 0, 70, NOW(), NOW()),
(20, 14, 8, 2025, 'FIRST', 14001, 'A분반', 'OFFLINE', 50, 0, 50, NOW(), NOW());

-- Student (학생) 데이터
INSERT INTO student (id, department_id, nickname, password, student_name, avg_reaction_time, best_bucket, created_at, updated_at) VALUES
(1, 1, 'student01', 'password123', '김학생', 0.35, NULL, NOW(), NOW()),
(2, 1, 'student02', 'password123', '이학생', 0.42, NULL, NOW(), NOW()),
(3, 1, 'student03', 'password123', '박학생', 0.38, NULL, NOW(), NOW()),
(4, 1, 'student04', 'password123', '최학생', 0.31, NULL, NOW(), NOW()),
(5, 1, 'student05', 'password123', '정학생', 0.45, NULL, NOW(), NOW()),
(6, 1, 'coder123', 'password123', '강개발', 0.29, NULL, NOW(), NOW()),
(7, 1, 'hacker99', 'password123', '윤코더', 0.33, NULL, NOW(), NOW()),
(8, 2, 'elec01', 'password123', '송전자', 0.37, NULL, NOW(), NOW()),
(9, 6, 'biz01', 'password123', '임경영', 0.40, NULL, NOW(), NOW()),
(10, 1, 'fast_clicker', 'password123', '한빠름', 0.25, NULL, NOW(), NOW()),
(11, 1, 'student11', 'password123', '조학생', 0.36, NULL, NOW(), NOW()),
(12, 1, 'cs_master', 'password123', '신컴공', 0.28, NULL, NOW(), NOW()),
(13, 1, 'algorithm_god', 'password123', '류알고', 0.32, NULL, NOW(), NOW()),
(14, 1, 'student14', 'password123', '문학생', 0.41, NULL, NOW(), NOW()),
(15, 1, 'student15', 'password123', '오학생', 0.39, NULL, NOW(), NOW());

-- Bucket (장바구니) 데이터
INSERT INTO bucket (id, student_id, name, created_at, updated_at) VALUES
-- 각 학생마다 1-2개의 장바구니
(1, 1, '1순위 시간표', NOW(), NOW()),
(2, 1, '2순위 시간표', NOW(), NOW()),
(3, 2, '희망 시간표', NOW(), NOW()),
(4, 3, '오전 수업 위주', NOW(), NOW()),
(5, 3, '오후 수업 위주', NOW(), NOW()),
(6, 4, '졸업 필수', NOW(), NOW()),
(7, 5, '교양 많이', NOW(), NOW()),
(8, 6, '전공 집중', NOW(), NOW()),
(9, 7, '밸런스 시간표', NOW(), NOW()),
(10, 8, '전자공학 커리큘럼', NOW(), NOW()),
(11, 9, '경영 + 교양', NOW(), NOW()),
(12, 10, '빠른손 장바구니', NOW(), NOW()),
(13, 11, '기본 시간표', NOW(), NOW()),
(14, 12, '심화 과정', NOW(), NOW()),
(15, 13, '알고리즘 마스터', NOW(), NOW()),
(16, 14, '여유로운 시간표', NOW(), NOW()),
(17, 15, '빡센 시간표', NOW(), NOW());

-- BucketElement (장바구니에 담긴 과목들)
INSERT INTO bucket_element (id, teach_id, bucket_id, priority, sub_element_id, created_at, updated_at) VALUES
-- 학생1의 1순위 시간표 (bucket_id=1)
(1, 1, 1, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반
(2, 4, 1, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반
(3, 8, 1, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반
(4, 15, 1, 4, NULL, NOW(), NOW()), -- 미적분학
(5, 17, 1, 5, NULL, NOW(), NOW()), -- 영어회화

-- 학생1의 2순위 시간표 (bucket_id=2)
(6, 2, 2, 1, NULL, NOW(), NOW()),  -- 자료구조 B분반 (대체: C분반)
(7, 5, 2, 2, NULL, NOW(), NOW()),  -- 알고리즘 B분반
(8, 6, 2, 3, NULL, NOW(), NOW()),  -- 운영체제 A분반 (대체: B분반)
(9, 18, 2, 4, NULL, NOW(), NOW()), -- 경영학원론

-- 학생2의 희망 시간표 (bucket_id=3)
(10, 1, 3, 1, NULL, NOW(), NOW()), -- 자료구니 A분반 (대체: B분반)
(11, 6, 3, 2, NULL, NOW(), NOW()), -- 운영체제 A분반
(12, 12, 3, 3, NULL, NOW(), NOW()), -- 인공지능 A분반 (대체: B분반)
(13, 16, 3, 4, NULL, NOW(), NOW()), -- 선형대수학
(14, 20, 3, 5, NULL, NOW(), NOW()), -- 글쓰기

-- 학생3의 오전 수업 위주 (bucket_id=4)
(15, 1, 4, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반
(16, 4, 4, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반
(17, 10, 4, 3, NULL, NOW(), NOW()), -- 컴퓨터네트워크
(18, 15, 4, 4, NULL, NOW(), NOW()), -- 미적분학

-- 학생3의 오후 수업 위주 (bucket_id=5)
(19, 2, 5, 1, NULL, NOW(), NOW()),  -- 자료구조 B분반
(20, 7, 5, 2, NULL, NOW(), NOW()),  -- 운영체제 B분반
(21, 14, 5, 3, NULL, NOW(), NOW()), -- 웹프로그래밍
(22, 19, 5, 4, NULL, NOW(), NOW()), -- 심리학개론

-- 학생4의 졸업 필수 (bucket_id=6)
(23, 1, 6, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반 (대체: B분반)
(24, 4, 6, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반 (대체: B분반)
(25, 8, 6, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반 (대체: B분반)
(26, 6, 6, 4, NULL, NOW(), NOW()),  -- 운영체제 A분반
(27, 11, 6, 5, NULL, NOW(), NOW()), -- 소프트웨어공학

-- 학생5의 교양 많이 (bucket_id=7)
(28, 3, 7, 1, NULL, NOW(), NOW()),  -- 자료구조 C분반
(29, 15, 7, 2, NULL, NOW(), NOW()), -- 미적분학
(30, 17, 7, 3, NULL, NOW(), NOW()), -- 영어회화
(31, 18, 7, 4, NULL, NOW(), NOW()), -- 경영학원론
(32, 19, 7, 5, NULL, NOW(), NOW()), -- 심리학개론

-- 학생6의 전공 집중 (bucket_id=8)
(33, 1, 8, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반
(34, 4, 8, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반
(35, 8, 8, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반
(36, 12, 8, 4, NULL, NOW(), NOW()), -- 인공지능 A분반
(37, 14, 8, 5, NULL, NOW(), NOW()), -- 웹프로그래밍
(38, 10, 8, 6, NULL, NOW(), NOW()), -- 컴퓨터네트워크

-- 학생7의 밸런스 시간표 (bucket_id=9)
(39, 2, 9, 1, NULL, NOW(), NOW()),  -- 자료구조 B분반
(40, 6, 9, 2, NULL, NOW(), NOW()),  -- 운영체제 A분반
(41, 14, 9, 3, NULL, NOW(), NOW()), -- 웹프로그래밍
(42, 16, 9, 4, NULL, NOW(), NOW()), -- 선형대수학
(43, 17, 9, 5, NULL, NOW(), NOW()), -- 영어회화

-- 학생8의 전자공학 커리큘럼 (bucket_id=10)
(44, 15, 10, 1, NULL, NOW(), NOW()), -- 미적분학
(45, 16, 10, 2, NULL, NOW(), NOW()), -- 선형대수학
(46, 10, 10, 3, NULL, NOW(), NOW()), -- 컴퓨터네트워크
(47, 18, 10, 4, NULL, NOW(), NOW()), -- 경영학원론

-- 학생9의 경영 + 교양 (bucket_id=11)
(48, 18, 11, 1, NULL, NOW(), NOW()), -- 경영학원론
(49, 19, 11, 2, NULL, NOW(), NOW()), -- 심리학개론
(50, 17, 11, 3, NULL, NOW(), NOW()), -- 영어회화
(51, 20, 11, 4, NULL, NOW(), NOW()), -- 글쓰기

-- 학생10의 빠른손 장바구니 (bucket_id=12)
(52, 1, 12, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반 (대체: B분반)
(53, 4, 12, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반 (대체: B분반)
(54, 8, 12, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반 (대체: B분반)
(55, 12, 12, 4, NULL, NOW(), NOW()), -- 인공지능 A분반 (대체: B분반)
(56, 10, 12, 5, NULL, NOW(), NOW()), -- 컴퓨터네트워크

-- 학생11의 기본 시간표 (bucket_id=13)
(57, 3, 13, 1, NULL, NOW(), NOW()),  -- 자료구조 C분반
(58, 5, 13, 2, NULL, NOW(), NOW()),  -- 알고리즘 B분반
(59, 6, 13, 3, NULL, NOW(), NOW()),  -- 운영체제 A분반
(60, 15, 13, 4, NULL, NOW(), NOW()), -- 미적분학

-- 학생12의 심화 과정 (bucket_id=14)
(61, 4, 14, 1, NULL, NOW(), NOW()),  -- 알고리즘 A분반
(62, 8, 14, 2, NULL, NOW(), NOW()),  -- 데이터베이스 A분반
(63, 12, 14, 3, NULL, NOW(), NOW()), -- 인공지능 A분반
(64, 11, 14, 4, NULL, NOW(), NOW()), -- 소프트웨어공학
(65, 10, 14, 5, NULL, NOW(), NOW()), -- 컴퓨터네트워크

-- 학생13의 알고리즘 마스터 (bucket_id=15)
(66, 4, 15, 1, NULL, NOW(), NOW()),  -- 알고리즘 A분반 (대체: B분반)
(67, 1, 15, 2, NULL, NOW(), NOW()),  -- 자료구조 A분반
(68, 8, 15, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반
(69, 12, 15, 4, NULL, NOW(), NOW()), -- 인공지능 A분반
(70, 15, 15, 5, NULL, NOW(), NOW()), -- 미적분학

-- 학생14의 여유로운 시간표 (bucket_id=16)
(71, 3, 16, 1, NULL, NOW(), NOW()),  -- 자료구조 C분반
(72, 17, 16, 2, NULL, NOW(), NOW()), -- 영어회화
(73, 19, 16, 3, NULL, NOW(), NOW()), -- 심리학개론
(74, 20, 16, 4, NULL, NOW(), NOW()), -- 글쓰기

-- 학생15의 빡센 시간표 (bucket_id=17)
(75, 1, 17, 1, NULL, NOW(), NOW()),  -- 자료구조 A분반
(76, 4, 17, 2, NULL, NOW(), NOW()),  -- 알고리즘 A분반
(77, 8, 17, 3, NULL, NOW(), NOW()),  -- 데이터베이스 A분반
(78, 6, 17, 4, NULL, NOW(), NOW()),  -- 운영체제 A분반
(79, 12, 17, 5, NULL, NOW(), NOW()), -- 인공지능 A분반
(80, 11, 17, 6, NULL, NOW(), NOW()); -- 소프트웨어공학

-- BucketElement 대체 과목 관계 설정 (2단계) - 같은 장바구니 내의 다음 과목으로 연결
UPDATE bucket_element SET sub_element_id = 7 WHERE id = 6;   -- bucket_id=2: 자료구조 B → 알고리즘 B
UPDATE bucket_element SET sub_element_id = 9 WHERE id = 8;   -- bucket_id=2: 운영체제 A → 경영학원론
UPDATE bucket_element SET sub_element_id = 11 WHERE id = 10; -- bucket_id=3: 자료구조 A → 운영체제 A
UPDATE bucket_element SET sub_element_id = 13 WHERE id = 12; -- bucket_id=3: 인공지능 A → 선형대수학
UPDATE bucket_element SET sub_element_id = 24 WHERE id = 23; -- bucket_id=6: 자료구조 A → 알고리즘 A
UPDATE bucket_element SET sub_element_id = 25 WHERE id = 24; -- bucket_id=6: 알고리즘 A → 데이터베이스 A
UPDATE bucket_element SET sub_element_id = 26 WHERE id = 25; -- bucket_id=6: 데이터베이스 A → 운영체제 A
UPDATE bucket_element SET sub_element_id = 53 WHERE id = 52; -- bucket_id=12: 자료구조 A → 알고리즘 A
UPDATE bucket_element SET sub_element_id = 54 WHERE id = 53; -- bucket_id=12: 알고리즘 A → 데이터베이스 A
UPDATE bucket_element SET sub_element_id = 55 WHERE id = 54; -- bucket_id=12: 데이터베이스 A → 인공지능 A
UPDATE bucket_element SET sub_element_id = 56 WHERE id = 55; -- bucket_id=12: 인공지능 A → 컴퓨터네트워크
UPDATE bucket_element SET sub_element_id = 67 WHERE id = 66; -- bucket_id=15: 알고리즘 A → 자료구조 A

-- 학생들의 대표 장바구니 설정
UPDATE student SET best_bucket = 1 WHERE id = 1;
UPDATE student SET best_bucket = 3 WHERE id = 2;
UPDATE student SET best_bucket = 4 WHERE id = 3;
UPDATE student SET best_bucket = 6 WHERE id = 4;
UPDATE student SET best_bucket = 7 WHERE id = 5;
UPDATE student SET best_bucket = 8 WHERE id = 6;
UPDATE student SET best_bucket = 9 WHERE id = 7;
UPDATE student SET best_bucket = 10 WHERE id = 8;
UPDATE student SET best_bucket = 11 WHERE id = 9;
UPDATE student SET best_bucket = 12 WHERE id = 10;
UPDATE student SET best_bucket = 13 WHERE id = 11;
UPDATE student SET best_bucket = 14 WHERE id = 12;
UPDATE student SET best_bucket = 15 WHERE id = 13;
UPDATE student SET best_bucket = 16 WHERE id = 14;
UPDATE student SET best_bucket = 17 WHERE id = 15;