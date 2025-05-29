package com.bitespeed.identityReconciliation.service;


import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.dto.IdentityRequest;
import com.bitespeed.identityReconciliation.entity.Contact;
import com.bitespeed.identityReconciliation.entity.LinkPrecedence;
import com.bitespeed.identityReconciliation.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    @Override
    @Transactional
    public ContactResponse identifyContact(IdentityRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();

        //all contacts matching the email or phone
        List<Contact> matchingContacts = new ArrayList<>();
        if (email != null) {
            matchingContacts.addAll(contactRepository.findByEmail(email));
        }
        if (phoneNumber != null) {
            matchingContacts.addAll(contactRepository.findByPhoneNumber(phoneNumber));
        }

        // only unique contacts we will remove duplicates
        Set<Contact> uniqueContacts = new HashSet<>(matchingContacts);

        if (uniqueContacts.isEmpty()) {
            // there is no existing contact so-> create new
            Contact newContact = createNewPrimaryContact(email, phoneNumber);
            return buildResponse(newContact);

        }

        //all primary contacts in matching into a SET
        Set<Contact> primaryContacts = uniqueContacts.stream()
                .filter(c -> c.getLinkPrecedence()== LinkPrecedence.
                PRIMARY).collect(Collectors.toSet());

        //merge multiple primary contacts
        if(primaryContacts.size()>1){
            primaryContacts= mergePrimaryContracts(primaryContacts);
        }

        //Get oldest primary contact
        Contact primaryContact = primaryContacts.stream()
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow();


        //check if we need to create a new secondary contact
        boolean shouldCreateSecondary= (email!=null && !email.equals(primaryContact.getEmail())) ||
                (phoneNumber !=null && !phoneNumber.equals(primaryContact.getPhoneNumber()));

        if(shouldCreateSecondary){
            createNewSecondaryContact(primaryContact,email,phoneNumber);
        }

       return buildResponse(primaryContact);
    }

    private void createNewSecondaryContact(Contact primaryContact, String email, String phoneNumber) {
    }

    private Set<Contact> mergePrimaryContracts(Set<Contact> primaryContacts) {
    }

    private ContactResponse buildResponse(Contact newContact) {
    }

    private Contact createNewPrimaryContact(String email, String phoneNumber) {
    }
}
