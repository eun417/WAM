package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.global.common.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Random;

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
     * 메일 전송
     */
    public void sendCodeMail(String email) throws MessagingException, UnsupportedEncodingException {
        String authCode = createCode(); //인증코드 생성

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject("[인증 코드] " + authCode); //이메일 제목
        message.setText(setCodeContext(authCode), "utf-8", "html"); //내용 설정(Template Process)
        message.setFrom(new InternetAddress(smtpEmail, "WAM"));   //보낼 때 이름 설정하고 싶은 경우

        //인증시간 만료를 위해 Redis에 5분 동안 저장
        redisService.setDataExpire(email, authCode, 60 * 5L);

        javaMailSender.send(message);   //이메일 전송
    }

    /**
     * 타임리프 설정
     */
    private String setCodeContext(String authCode) {
        Context context = new Context();
        context.setVariable("authCode", authCode); //Template에 전달할 데이터 설정
        return templateEngine.process("mailAuthCode", context); //mailAuthCode.html
    }

    /**
     * 인증번호 생성
     */
    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) { //인증코드 8자리
            int index = rnd.nextInt(3); //0~2 까지 랜덤, rnd 값에 따라서 아래 switch 문이 실행됨
            switch (index) {
                case 0:
                    code.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //a~z (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    code.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //A~Z
                    break;
                case 2:
                    code.append((rnd.nextInt(10)));
                    //0~9
                    break;
            }
        }

        return code.toString();
    }

}
