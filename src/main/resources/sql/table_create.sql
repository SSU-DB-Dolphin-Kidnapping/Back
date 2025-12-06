-- -----------------------------------------------------
-- 1. College (단과대)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `college` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `name` VARCHAR(50) NOT NULL,
                                         `created_at` DATETIME(6),
                                         `updated_at` DATETIME(6),
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 2. Department (학과)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `department` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                                            `college_id` BIGINT,
                                            `name` VARCHAR(50) NOT NULL,
                                            `created_at` DATETIME(6),
                                            `updated_at` DATETIME(6),
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 3. Student (학생)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `department_id` BIGINT,
                                         `nickname` VARCHAR(255) NOT NULL,
                                         `password` VARCHAR(255) NOT NULL,
                                         `student_name` VARCHAR(255) NOT NULL,
                                         `student_number` VARCHAR(255),
                                         `soongsil_email` VARCHAR(255),
                                         `avg_reaction_time` DOUBLE,
                                         `best_bucket` BIGINT,
                                         `grade` INT,
                                         `verified` BIT,
                                         `verified_at` DATETIME(6),
                                         `created_at` DATETIME(6),
                                         `updated_at` DATETIME(6),
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_student_nickname` (`nickname`),
                                         UNIQUE KEY `uk_student_number` (`student_number`),
                                         UNIQUE KEY `uk_student_email` (`soongsil_email`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 4. Professor (교수)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `professor` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                           `department_id` BIGINT,
                                           `professor_name` VARCHAR(50) NOT NULL,
                                           `created_at` DATETIME(6),
                                           `updated_at` DATETIME(6),
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 5. Course (강의 기본 정보)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `name` VARCHAR(255) NOT NULL,
                                        `majority` VARCHAR(255),
                                        `time` FLOAT,
                                        `credit` FLOAT,
                                        `created_at` DATETIME(6),
                                        `updated_at` DATETIME(6),
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 6. CourseType (이수 구분)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `course_type` (
                                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                                             `course_id` BIGINT,
                                             `second_major` VARCHAR(30) NOT NULL,
                                             `created_at` DATETIME(6),
                                             `updated_at` DATETIME(6),
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 7. Teach (개설 강좌)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teach` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `course_id` BIGINT,
                                       `professor_id` BIGINT,
                                       `year` INT,
                                       `semester` VARCHAR(255),
                                       `number` BIGINT,
                                       `class_name` VARCHAR(255),
                                       `target_grade` INT,
                                       `type` VARCHAR(20),
                                       `max_count` INT,
                                       `enrolled_count` INT,
                                       `remain_count` INT,
                                       `created_at` DATETIME(6),
                                       `updated_at` DATETIME(6),
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 8. TeachInfo (강의 시간표 및 장소)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `teach_info` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                                            `teach_id` BIGINT,
                                            `day_of_the_week` VARCHAR(255),
                                            `start_time` TIME,
                                            `end_time` TIME,
                                            `classroom` VARCHAR(255),
                                            `created_at` DATETIME(6),
                                            `updated_at` DATETIME(6),
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 9. Bucket (장바구니)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bucket` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `student_id` BIGINT,
                                        `name` VARCHAR(255),
                                        `created_at` DATETIME(6),
                                        `updated_at` DATETIME(6),
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 10. BucketElement (장바구니 담은 과목)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bucket_element` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                `bucket_id` BIGINT,
                                                `teach_id` BIGINT,
                                                `sub_element_id` BIGINT,
                                                `priority` INT,
                                                `created_at` DATETIME(6),
                                                `updated_at` DATETIME(6),
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 11. MyLastBucketNumber (학생별 마지막 장바구니 번호)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `my_last_bucket_number` (
                                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                       `student_id` BIGINT,
                                                       `last_bucket_number` INT,
                                                       `created_at` DATETIME(6),
                                                       `updated_at` DATETIME(6),
                                                       PRIMARY KEY (`id`),
                                                       UNIQUE KEY `uk_mylastbucket_student` (`student_id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 12. StudentEmailVerification (이메일 인증)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_email_verification` (
                                                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                            `student_id` BIGINT NOT NULL,
                                                            `code` VARCHAR(255) NOT NULL,
                                                            `email` VARCHAR(255) NOT NULL,
                                                            `expired_at` DATETIME(6) NOT NULL,
                                                            `used` BIT NOT NULL,
                                                            `created_at` DATETIME(6),
                                                            `updated_at` DATETIME(6),
                                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 13. Test (시뮬레이션 테스트 기록)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT,
                                      `created_at` DATETIME(6),
                                      `updated_at` DATETIME(6),
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- 14. History (시뮬레이션 상세 결과)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `history` (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `bucket_element_id` BIGINT NOT NULL,
                                         `test_id` BIGINT,
                                         `is_success` BIT,
                                         `failed_reason` VARCHAR(255),
                                         `created_at` DATETIME(6),
                                         `updated_at` DATETIME(6),
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB;