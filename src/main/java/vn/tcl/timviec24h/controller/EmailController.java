package vn.tcl.timviec24h.controller;

import org.springframework.mail.MailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tcl.timviec24h.service.EmailService;
import vn.tcl.timviec24h.service.SubscriberService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;
    public EmailController(EmailService emailService,  SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }
    @GetMapping("/email")
    @ApiMessage("Send Email")
//    @Scheduled(cron = "*/30 * * * * *")
//    @Transactional
    public String sendSimpleEmail(){
        //this.emailService.sendSimpleMail();
      //  emailService.sendEmailSync("congluctran1304@gmail.com","Test Send Email","<h1> <b>Hello</b> </h1>",false,true);
//        emailService.sendEmailFromTemplateSync("congluctran1304@gmail.com","Test send mail","job");
        subscriberService.sendSubscribersEmailJobs();
        return "Oks";
    }
}
