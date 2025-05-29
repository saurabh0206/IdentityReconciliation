package com.bitespeed.identityReconciliation.service;


import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.dto.IdentityRequest;
import com.bitespeed.identityReconciliation.entity.Contact;
import com.bitespeed.identityReconciliation.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public ContactResponse identifyContact(IdentityRequest request) {
        String email=request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        //all contacts matching the email or phone
        List<Contact> matchingContacts= new ArrayList<>();
        if(email!=null){
            matchingContacts.addAll(contactRepository.findByEmail(email));
        }
        if(phoneNumber!=null){
            matchingContacts.addAll(contactRepository.findByPhoneNumber(phoneNumber));
        }

        // only unique contacts we will remove duplicates
        Set<Contact> uniqueContacts= new HashSet<>(matchingContacts);






        return null;
    }
}
