package com.bitespeed.identityRecoinciliation.controller;


import com.bitespeed.identityRecoinciliation.dto.IdentityRequest;
import com.bitespeed.identityRecoinciliation.dto.IdentityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IdentityController {

    @PostMapping("/identity")
    public ResponseEntity<IdentityResponse> identityContract( @RequestBody IdentityRequest request){

        IdentityResponse response= new IdentityResponse();
      return ResponseEntity.ok(response) ;
    }
}
