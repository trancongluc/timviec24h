package vn.tcl.timviec24h.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tcl.timviec24h.domain.Subscriber;
import vn.tcl.timviec24h.service.SubscriberService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }
    @PostMapping("/subscribers")
    @ApiMessage("Create subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        if(subscriberService.existsSubscriberByEmail(subscriber.getEmail())) {
            throw new IdInvalidException("Email đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriberService.createSubscriber(subscriber));
    }
    @PutMapping("/subscribers")
    @ApiMessage("Update subscriber")
    public ResponseEntity<Subscriber> updateSubscriber( @RequestBody Subscriber subscriber) throws IdInvalidException {
        if(subscriberService.findSubscriberById(subscriber.getId()) == null) {
            throw new IdInvalidException("Không tìm thấy Subscriber với id = "+subscriber.getId());
        }
        return ResponseEntity.ok().body(subscriberService.updateSubscriber(subscriber));
    }
}
