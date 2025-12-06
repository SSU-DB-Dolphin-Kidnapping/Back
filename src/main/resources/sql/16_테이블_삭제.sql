-- 외래키 체크 해제 (순서 무관하게 삭제 가능하도록 설정)
SET FOREIGN_KEY_CHECKS = 0;

-- 생성된 모든 테이블 삭제
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS test;
DROP TABLE IF EXISTS student_email_verification;
DROP TABLE IF EXISTS my_last_bucket_number;
DROP TABLE IF EXISTS bucket_element;
DROP TABLE IF EXISTS bucket;
DROP TABLE IF EXISTS teach_info;
DROP TABLE IF EXISTS teach;
DROP TABLE IF EXISTS course_type;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS professor;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS college;

-- 외래키 체크 다시 설정
SET FOREIGN_KEY_CHECKS = 1;