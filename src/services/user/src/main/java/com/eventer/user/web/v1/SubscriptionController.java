package com.eventer.user.web.v1;

import com.eventer.user.service.SubscriptionService;
import com.eventer.user.web.dto.subscription.CreateSubscriptionDTO;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController implements ResultUnwrapper {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateSubscriptionDTO dto) {
        Result result = this.subscriptionService.create(dto.entityType(), dto.entityId());
        return this.okOrError(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getIsEventSubscribed(@PathVariable Long eventId) {
        Result<Boolean> result = this.subscriptionService.getIsEventSubscribed(eventId);
        return this.okOrError(result);
    }
}
