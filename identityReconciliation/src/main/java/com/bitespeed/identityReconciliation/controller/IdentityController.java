package com.bitespeed.identityReconciliation.controller;

import com.bitespeed.identityReconciliation.dto.IdentityRequest;
import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.service.ContactService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity")
@RequiredArgsConstructor
public class IdentityController {

    private final ContactService contactService;

    @PostMapping
    public ContactResponse identityContract(@RequestBody IdentityRequest request) {
        return contactService.identifyContact(request);
    }
}
