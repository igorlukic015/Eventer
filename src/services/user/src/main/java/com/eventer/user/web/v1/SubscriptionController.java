package com.eventer.user.web.v1;

import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.service.SubscriptionService;
import com.eventer.user.web.dto.subscription.CreateSubscriptionDTO;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController implements ResultUnwrapper {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateSubscriptionDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Result result = this.subscriptionService.create(dto.entityType(), dto.entityId(), userDetails);
        return this.okOrError(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getIsEventSubscribed(@PathVariable Long eventId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Result<Boolean> result = this.subscriptionService.getIsEventSubscribed(eventId, userDetails);
        return this.okOrError(result);
    }

    @GetMapping("/subscribed-categories")
    public ResponseEntity<?> getSubscribedCategoryIds(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Result<Set<Long>> result = this.subscriptionService.getSubscribedCategoryIds(userDetails);
        return this.okOrError(result);
    }
}
