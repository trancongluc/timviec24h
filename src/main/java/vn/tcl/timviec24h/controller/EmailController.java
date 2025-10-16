package vn.tcl.timviec24h.controller;

import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tcl.timviec24h.service.EmailService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @GetMapping("/email")
    @ApiMessage("Send Email")
    public String sendSimpleEmail(){
        //this.emailService.sendSimpleMail();
      //  emailService.sendEmailSync("congluctran1304@gmail.com","Test Send Email","<h1> <b>Hello</b> </h1>",false,true);
        emailService.sendEmailFromTemplateSync("congluctran1304@gmail.com","Test send mail","job");
        return "Oks";
    }
}
