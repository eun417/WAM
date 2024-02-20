package com.chungjin.wam.domain.auth.service;

import com.chungjin.wam.domain.auth.dto.request.EmailRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * 메일 전송
     */
    public void sendMail(String email) throws MessagingException {
        String code = createCode(); //인증코드 생성
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject("[인증 코드] " + code); //이메일 제목
        message.setText(setContext(code), "utf-8", "html"); //내용 설정(Template Process)

        //보낼 때 이름 설정하고 싶은 경우
        //message.setFrom(new InternetAddress([이메일 계정], [설정할 이름]));

        javaMailSender.send(message); //이메일 전송
    }

    /**
     * 타임리프 설정
     */
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code); //Template에 전달할 데이터 설정
        return templateEngine.process("mail", context); //mail.html
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
