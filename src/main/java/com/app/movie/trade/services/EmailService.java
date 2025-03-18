package com.app.movie.trade.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
	/*@Value("${from.email.address}")
	private String fromEmailAddress;

	@Autowired
	private JavaMailSender mailSender;

	@Async
	public void sendEmail(String recipient, String subject, String content)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(fromEmailAddress, "My Email Address");
		helper.setTo(recipient);
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}*/

}
