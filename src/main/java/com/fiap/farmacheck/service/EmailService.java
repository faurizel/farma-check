package com.fiap.farmacheck.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarNotificacaoDisponibilidade(String destinatario, String nomeUsuario, String nomeMedicamento) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinatario);
        message.setSubject("FarmaCheck - Medicamento Disponível: " + nomeMedicamento);
        message.setText(
                "Olá " + nomeUsuario + ",\n\n" +
                "O medicamento que você pesquisou está agora disponível na farmácia pública!\n\n" +
                "Medicamento: " + nomeMedicamento + "\n\n" +
                "Dirija-se à unidade mais próxima para retirar o seu medicamento.\n\n" +
                "Atenciosamente,\n" +
                "Equipe FarmaCheck"
        );
        message.setFrom("farmacheck@noreply.com");

        try {
            mailSender.send(message);
            logger.info("Email de notificação enviado para {} sobre o medicamento '{}'", destinatario, nomeMedicamento);
        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", destinatario, e.getMessage(), e);
        }
    }
}
