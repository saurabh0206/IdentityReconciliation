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

import java.time.LocalDateTime;
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
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.
                        PRIMARY).collect(Collectors.toSet());

        //merge multiple primary contacts
        if (primaryContacts.size() > 1) {
            primaryContacts = mergePrimaryContracts(primaryContacts);
        }

        //Get oldest primary contact
        Contact primaryContact = primaryContacts.stream()
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow();


        //check if we need to create a new secondary contact
        boolean shouldCreateSecondary = (email != null && !email.equals(primaryContact.getEmail())) ||
                (phoneNumber != null && !phoneNumber.equals(primaryContact.getPhoneNumber()));

        if (shouldCreateSecondary) {
            createSecondaryContact(primaryContact, email, phoneNumber);
        }

        return buildResponse(primaryContact);
    }

    private void createSecondaryContact(Contact primaryContact, String email, String phoneNumber) {

        //is there existing seci=ondary contact with these details
        boolean secondaryExist = contactRepository.findByLinkedId(primaryContact.getId()).stream()
                .anyMatch(c ->
                        (email != null && email.equals(c.getEmail())) ||
                                (phoneNumber != null && phoneNumber.equals(c.getPhoneNumber()));

        if (!secondaryExist) {

            // builder().build() returns local instance
            Contact secondaryContact = Contact.builder()
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .linkedId(primaryContact.getId())
                    .linkPrecedence(LinkPrecedence.SECONDARY)
                    .createdAt(LocalDateTime.now())
                    .build();
        }

    }

    private Set<Contact> mergePrimaryContracts(Set<Contact> primaryContacts) {
        // Find the oldest primary contact

        Contact oldestPrimary = primaryContacts.stream()
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow();

        // We will Convert all other primary contacts to secondary
        primaryContacts.stream()
                .filter(c -> !c.getId().equals(oldestPrimary.getId()))
                .forEach(c -> {
                    c.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    c.setLinkedId(oldestPrimary.getId());
                    c.setUpdatedAt(LocalDateTime.now());
                    contactRepository.save(c);

                    // Also update all contacts linked to this one

                    contactRepository.findByLinkedId(c.getId()).forEach(secondary -> {
                        secondary.setLinkedId(oldestPrimary.getId());
                        contactRepository.save(secondary);
                    });
                });

        // Return just the oldest primary
        return Collections.singleton(oldestPrimary);

    }

    private ContactResponse buildResponse(Contact primaryContact) {

        //Get all secondary
        List<Contact> secondaryContacts= contactRepository.findByLinkedId(primaryContact.getId());

        Set<String> emails= new LinkedHashSet<>();
        if(primaryContact.getEmail()!=null){
            emails.add(primaryContact.getEmail());
        }

        secondaryContacts.stream()
                .map(Contact::getEmail)
                .filter(Object::nonNull)
                .forEach(emails::add);

        //colloect no

        Set<String> phoneNumbers= new LinkedHashSet<>();
        if (primaryContact.getPhoneNumber() != null) {
            phoneNumbers.add(primaryContact.getPhoneNumber());
        }
        secondaryContacts.stream()
                .map(Contact::getPhoneNumber)
                .filter(Objects::nonNull)
                .forEach(phoneNumbers::add);

         // collect secondary cont ids
        List<Integer> secondaryContactIds= secondaryContacts.stream()
                .map(Contact::getId)
                .collect(Collectors.toList());

        return ContactResponse.builder()
                .contact(ContactDto.builder()
                .primaryContactId(primaryContact.getId())
                .emails(new ArrayList<>(emails))
                .phoneNumbers(new ArrayList<>(phoneNumbers))
                .secondaryContactIds(secondaryContactIds)
                .build())
                .build();

    }

    private Contact createNewPrimaryContact(String email, String phoneNumber) {
       Contact contact = Contact.builder()
               .email(email)
               .phoneNumber(phoneNumber)
               .linkPrecedence(LinkPrecedence.PRIMARY)
               .createdAt(LocalDateTime.now())
               .build();
        return contactRepository.save(contact);

    }
}
