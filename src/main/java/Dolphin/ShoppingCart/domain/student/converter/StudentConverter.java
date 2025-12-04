package Dolphin.ShoppingCart.domain.student.converter;

import Dolphin.ShoppingCart.domain.student.dto.info.StudentInfoResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.login.StudentLoginResponseDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpRequestDTO;
import Dolphin.ShoppingCart.domain.student.dto.signup.StudentSignUpResponseDTO;
import Dolphin.ShoppingCart.domain.student.entity.Student;

public class StudentConverter {

    // 회원가입용 Student 엔티티 생성
    public static Student toStudent(StudentSignUpRequestDTO requestDTO) {
        return Student.builder()
                .studentName(requestDTO.getStudentName())
                .nickname(requestDTO.getNickname())
                .password(requestDTO.getPassword()) // 나중에 암호화 붙이면 여기서 인코딩
                .department(null)                    // 온보딩에서 채울 예정
                .grade(null)
                .avgReactionTime(null)
                .bestBucket(null)
                .build();
    }

    // 회원가입 응답 DTO 변환
    public static StudentSignUpResponseDTO toSignUpResponseDTO(Student student) {
        return StudentSignUpResponseDTO.builder()
                .id(student.getId())
                .studentName(student.getStudentName())
                .nickname(student.getNickname())
                .build();
    }

    // 로그인 응답 변환
    public static StudentLoginResponseDTO toLoginResponseDTO(Student student) {
        return StudentLoginResponseDTO.builder()
                .id(student.getId())
                .studentName(student.getStudentName())
                .nickname(student.getNickname())
                .build();
    }


    // 회원정보 조회용 변환 메서드
    public static StudentInfoResponseDTO toStudentInfoResponseDTO(Student student) {
        return StudentInfoResponseDTO.builder()
                .id(student.getId())
                .studentName(student.getStudentName())
                .nickname(student.getNickname())
                .studentNumber(student.getStudentNumber())
                .grade(student.getGrade())
                .departmentId(
                        student.getDepartment() != null ? student.getDepartment().getId() : null
                )
                .avgReactionTime(student.getAvgReactionTime())
                .bestBucket(student.getBestBucket())
                .build();
    }
}