package com.spring.GramSevaPortal.service;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendComplaintStatusEmail(String toEmail, String userName, long complaintId, String newStatus) {

        Email from = new Email("jayeshkarankal345@gmail.com");
        Email to = new Email(toEmail);

        String subject = "Complaint Status Update - ID: " + complaintId;

        String message =
                "Hello " + userName + ",\n\n" +
                        "Your complaint (ID: " + complaintId + ") status has been updated to: " + newStatus + ".\n\n" +
                        "Thank you for your patience.\n" +
                        "Regards,\nSupport Team";

        Content content = new Content("text/plain", message);
        Mail mail = new Mail(from, subject, to, content);

        try {
            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("STATUS: " + response.getStatusCode());
            System.out.println("BODY: " + response.getBody());
            System.out.println("HEADERS: " + response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
