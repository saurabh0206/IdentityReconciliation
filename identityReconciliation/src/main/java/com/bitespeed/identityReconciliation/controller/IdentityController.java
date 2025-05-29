package com.bitespeed.identityReconciliation.controller;


import com.bitespeed.identityReconciliation.dto.IdentityRequest;
import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity")
public class IdentityController {

    private  ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> identityContract( @RequestBody IdentityRequest request){

      ContactResponse response= contactService.identifyContact(request);
      return ResponseEntity.ok(response) ;
    }
}
