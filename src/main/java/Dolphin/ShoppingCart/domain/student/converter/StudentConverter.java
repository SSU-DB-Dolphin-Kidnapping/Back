package Dolphin.ShoppingCart.domain.student.converter;

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

}