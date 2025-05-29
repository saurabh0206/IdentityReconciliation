package com.bitespeed.identityReconciliation.dto;

import lombok.Data;
import java.util.List;

@Data
public class ContactResponse {
    private ContactDto contact;

    @Data
    public static class ContactDto {
        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
    }
}
