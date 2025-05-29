package com.bitespeed.identityReconciliation.controller;


import com.bitespeed.identityReconciliation.dto.IdentityRequest;
import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api")

public class IdentityController {

    private  ContactService contactService;


    @PostMapping("/identity")
    public ResponseEntity<ContactResponse> identityContract( @RequestBody IdentityRequest request){

        ContactResponse response = new ContactResponse();
        response.setPrimaryContactId(1);
        response.setEmails(List.of("test@example.com"));
        response.setPhoneNumbers(List.of("1234567890"));
        response.setSecondaryContactIds(List.of(2, 3));

    //  ContactResponse response= contactService.identifyContact(request);

      return ResponseEntity.ok(response) ;
    }
}
