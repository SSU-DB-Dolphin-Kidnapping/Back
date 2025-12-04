package Dolphin.ShoppingCart.global.service;

import Dolphin.ShoppingCart.global.error.code.status.ErrorStatus;
import Dolphin.ShoppingCart.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendVerificationMail(String to, String code) {
        // null 혹은 빈 문자열이면 메일 보내지 않음
        if (to == null || to.isBlank()) {
            return; // 혹은 예외 throw
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[너의 장바구니가 가능해보여?] 숭실대 이메일 인증 코드 안내");
            message.setText(
                    "안녕하세요.\n\n" +
                            "숭실대학교 재학생 인증을 위한 이메일 인증 코드입니다.\n\n" +
                            "인증 코드: " + code + "\n\n" +
                            "해당 코드는 발송 시점으로부터 10분 동안만 유효합니다.\n\n" +
                            "감사합니다."
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }
}