package com.bitespeed.identityReconciliation.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContactResponse {



        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;

}
