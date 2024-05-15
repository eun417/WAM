package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.global.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.chungjin.wam.global.exception.error.ErrorCodeType.SEND_MAIL_FAILED;
import static com.chungjin.wam.global.util.Constants.PROJECT_URL;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String smtpEmail;

    /**
     * 이메일 인증을 위한 인증번호 메일 전송
     */
    @Async
    public void sendAuthCodeMail(String email) {
        try {
            String authCode = createCode(); //인증코드 생성
            String subject = "[인증 코드] " + authCode;
            String content = setMailContext("join/mailAuthCode", "authCode", authCode);

            //메일 전송
            sendEmail(email, subject, content);

            //인증시간 만료를 위해 Redis 에 5분 동안 저장
            redisService.setDataExpire(email, authCode, 60 * 5L);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(SEND_MAIL_FAILED);
        }
    }

    /**
     * 비밀번호 재설정 링크 메일 전송
     */
    @Async
    public void sendLinkMail(String email) {
        try {
            String authCode = createCode(); //인증코드 생성
            String subject = "[WAM] 비밀번호 재설정 링크입니다.";
            String changePwLink = PROJECT_URL + "auth/change-pw/form?authCode=" + authCode;   //인증코드로 링크 생성
            String content = setMailContext("login/mailChangePwLink", "changePwLink", changePwLink);

            //메일 전송
            sendEmail(email, subject, content);

            //링크 제공시간 제한을 위해 Redis 에 10분 동안 저장
            redisService.setDataExpire(authCode, email, 60 * 10L);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new CustomException(SEND_MAIL_FAILED);
        }
    }

    //타임리프 설정 함수
    private String setMailContext(String templateName, String variableName, String variableValue) {
        Context context = new Context();
        context.setVariable(variableName, variableValue); //Template 에 전달할 데이터 설정
        return templateEngine.process(templateName, context);
    }

    //인증번호 생성 함수
    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) {    //인증코드 6자리
            code.append((rnd.nextInt(10)));
        }

        return code.toString();
    }

    //메일 전송 함수
    private void sendEmail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);  //true : html 사용
        helper.setFrom(new InternetAddress(smtpEmail, "WAM"));   //보낸이 이름 설정

        javaMailSender.send(message);
    }

}
