package com.bitespeed.identityReconciliation.repository;

import com.bitespeed.identityReconciliation.entity.Contact;

import com.bitespeed.identityReconciliation.entity.LinkPrecedence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {

     List<Contact> findByEmail(String email);
    List<Contact> findByPhoneNumber(String phoneNumber);
    List<Contact> findByLinkedId(Integer linkedId);
    List<Contact> findByLinkPrecedence(LinkPrecedence linkPrecedence);



}
