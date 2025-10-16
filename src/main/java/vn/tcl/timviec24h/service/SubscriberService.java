package vn.tcl.timviec24h.service;

import org.springframework.stereotype.Service;
import vn.tcl.timviec24h.domain.Skill;
import vn.tcl.timviec24h.domain.Subscriber;
import vn.tcl.timviec24h.repository.SkillRepository;
import vn.tcl.timviec24h.repository.SubscriberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
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
}
