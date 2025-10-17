package vn.tcl.timviec24h.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Job;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.domain.Subscriber;
import vn.tcl.timviec24h.domain.response.email.ResEmailJob;
import vn.tcl.timviec24h.repository.JobRepository;
import vn.tcl.timviec24h.repository.SkillRepository;
import vn.tcl.timviec24h.repository.SubscriberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository,
                             EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public Subscriber createSubscriber(Subscriber subscriber) {
        if (subscriber.getSkills() != null) {
            List<Long> idsSkill = subscriber.getSkills().stream().map(x -> x.getId()).toList();
            List<Skill> skillsDB = skillRepository.findByIdIn(idsSkill);
            subscriber.setSkills(skillsDB);
        }
        return subscriberRepository.save(subscriber);
    }

    public Subscriber findSubscriberById(Long id) {
        Optional<Subscriber> optional = subscriberRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public boolean existsSubscriberByEmail(String email) {
        if (subscriberRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }
    public Subscriber findByEmail(String email) {
        Optional<Subscriber> subscriber = subscriberRepository.findByEmail(email);
        if (subscriber.isPresent()) {
            return subscriber.get();

        }
        return null;
    }
    public Subscriber updateSubscriber(Subscriber subscriber) {
        Subscriber subscriberDB = findSubscriberById(subscriber.getId());
        if (subscriberDB != null) {
            if (subscriber.getSkills() != null) {
                List<Long> idsSkill = subscriber.getSkills().stream().map(x -> x.getId()).toList();
                List<Skill> skillsDB = skillRepository.findByIdIn(idsSkill);
                subscriberDB.setSkills(skillsDB);
            }
        }
        return subscriberRepository.save(subscriberDB);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                         List<ResEmailJob> arr = listJobs.stream().map(
                         job -> this.convertJobToSendEmail(job)).toList();

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }
    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .toList();
        res.setSkills(s);
        return res;
    }


}
