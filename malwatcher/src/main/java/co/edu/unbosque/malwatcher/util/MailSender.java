package co.edu.unbosque.malwatcher.util;

import java.util.Properties;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailSender {

	/**
	 * Correo electronico
	 */
	private static String emailFrom = "MalWatcher30@gmail.com";
	/**
	 * Contraseña de seguridad
	 */
	private static String passwordFrom = "kucg clru poeq crgs";
	/**
	 * Asunto
	 */
	private static String subject;
	/**
	 * Contenido
	 */
	private static String content;
	/**
	 * Propiedades
	 */
	private static Properties prop;
	/**
	 * Sesion
	 */
	private static Session session;
	/**
	 * Correo
	 */
	private static MimeMessage email;

	/**
	 * Metodo para enviar el correo
	 * 
	 * @param addressee
	 * @param username
	 */
	public static boolean sendEmail(String addressee, String username, String password, String codigoVerificacion) {
		prop = new Properties();
		subject = "BIENVENIDO A MAL-WATCHER!";
		content = "<!DOCTYPE html>\r\n" + "<html lang=\"es\">\r\n" + "\r\n" + "<head>\r\n"
				+ "  <meta charset=\"UTF-8\">\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "  <title>Registro exitoso - Mal-Watcher</title>\r\n" + "  <style>\r\n" + "    body {\r\n"
				+ "      font-family: Arial, sans-serif;\r\n" + "      background-color: #F3F3F3;\r\n"
				+ "      margin: 0;\r\n" + "      padding: 0;\r\n" + "    }\r\n" + "\r\n" + "    .container {\r\n"
				+ "      width: 100%;\r\n" + "      max-width: 600px;\r\n" + "      margin: 0 auto;\r\n"
				+ "      background-color: #13173d;\r\n" + "      padding: 20px;\r\n" + "      border-radius: 10px;\r\n"
				+ "      text-align: center;\r\n" + "    }\r\n" + "\r\n" + "    .header,\r\n" + "    .footer {\r\n"
				+ "      background-color: #232c42;\r\n" + "      color: white;\r\n" + "      padding: 10px;\r\n"
				+ "      border-radius: 5px;\r\n" + "    }\r\n" + "\r\n" + "    .header img {\r\n"
				+ "      max-width: 80px;\r\n" + "      margin-right: 10px;\r\n" + "    }\r\n" + "\r\n"
				+ "    .content {\r\n" + "      background-color: #232c42;\r\n" + "      color: white;\r\n"
				+ "      padding: 30px;\r\n" + "      margin: 20px 0;\r\n" + "      border-radius: 10px;\r\n"
				+ "    }\r\n" + "\r\n" + "    .content p {\r\n" + "      font-size: 18px;\r\n"
				+ "      margin: 10px 0;\r\n" + "    }\r\n" + "\r\n" + "    .social-icons img {\r\n"
				+ "      width: 30px;\r\n" + "      margin: 0 10px;\r\n" + "    }\r\n" + "\r\n"
				+ "    .social-text {\r\n" + "      background-color: #13173d;\r\n" + "      color: black;\r\n"
				+ "    }\r\n" + "\r\n" + "    .footer a {\r\n" + "      color: white;\r\n"
				+ "      text-decoration: none;\r\n" + "    }\r\n" + "\r\n" + "    .footer a:hover {\r\n"
				+ "      text-decoration: underline;\r\n" + "    }\r\n" + "\r\n" + "    .important-text {\r\n"
				+ "      font-weight: bold;\r\n" + "      color: #FFD700;\r\n" + "    }\r\n" + "\r\n"
				+ "    .social-icons p {\r\n" + "      color: #bbbec7 !important;\r\n"
				+ "      /* Cambiado a azul claro con prioridad */\r\n" + "    }\r\n" + "  </style>\r\n" + "</head>\r\n"
				+ "\r\n" + "<body>\r\n" + "\r\n" + "  <div class=\"container\">\r\n" + "    <!-- Header -->\r\n"
				+ "    <div class=\"header\">\r\n" + "      <img src=\"https://i.imgur.com/99YPEiS.png\">\r\n"
				+ "      <h1>BIENVENIDO A MAL-WATCHER!</h1>\r\n" + "    </div>\r\n" + "\r\n"
				+ "    <!-- Content -->\r\n" + "    <div class=\"content\">\r\n"
				+ "      <p>Su nombre de usuario es: <span class=\"important-text\">"+username+"</span></p>\r\n"
				+ "      <br />\r\n"
				+ "      <p>Su contraseña es: <span class=\"important-text\">"+password+"</span></p>\r\n"
				+ "      <br />\r\n" + "      <br />\r\n" + "      <br />\r\n"
				+ "      <p>Su codigo de verificacion es: <span class=\"important-text\">" + codigoVerificacion
				+ "</span></p>\r\n" + "      <br />\r\n" + "      <br />\r\n" + "      <br />\r\n" + "      <br />\r\n"
				+ "      <p>GRACIAS POR CONFIAR EN NOSOTROS</p>\r\n" + "\r\n" + "    </div>\r\n" + "\r\n"
				+ "    <!-- Social Icons -->\r\n" + "    <div class=\"social-icons\">\r\n"
				+ "      <p>Síguenos en nuestras plataformas:</p>\r\n"
				+ "      <a href=\"#\"><img src=\"https://i.imgur.com/lQwoYlu.png\"></a>\r\n"
				+ "      <a href=\"#\"><img src=\"https://i.imgur.com/7Yg1W4l.png\"></a>\r\n"
				+ "      <a href=\"#\"><img src=\"https://i.imgur.com/lWrPvdh.png\"></a>\r\n"
				+ "      <a href=\"#\"><img src=\"https://i.imgur.com/w1xZMz3.png\"></a>\r\n" + "    </div>\r\n"
				+ "\r\n" + "    <!-- Footer -->\r\n" + "    <div class=\"footer\">\r\n"
				+ "      <p>FELICIDADES, SE HA REGISTRADO CON ÉXITO</p>\r\n" + "    </div>\r\n" + "  </div>\r\n"
				+ "\r\n" + "</body>\r\n" + "\r\n" + "</html>";
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		prop.setProperty("mail.smtp.port", "587");
		prop.setProperty("mail.smtp.user", emailFrom);
		prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
		prop.setProperty("mail.smtp.auth", "true");

		session = Session.getDefaultInstance(prop);

		try {

			email = new MimeMessage(session);
			email.setFrom(new InternetAddress(emailFrom));
			email.setRecipient(Message.RecipientType.TO, new InternetAddress(addressee));
			BodyPart text = new MimeBodyPart();
			text.setContent(content, "text/html");
			MimeMultipart part = new MimeMultipart();
			part.addBodyPart(text);
			email.setSubject(subject);
			email.setContent(part);
			Transport t = session.getTransport("smtp");
			t.connect(emailFrom, passwordFrom);
			t.sendMessage(email, email.getRecipients(Message.RecipientType.TO));
			t.close();
			return true;

		} catch (AddressException e) {
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
