package com.bitespeed.identityReconciliation.dto;


import lombok.Data;

import java.util.List;

@Data
public class ContactResponse {



        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;

}
