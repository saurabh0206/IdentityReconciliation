package com.bitespeed.identityRecoinciliation.dto;

import lombok.Data;
import java.util.List;

@Data
public class IdentityResponse {
    private ContactDto contact;

    @Data
    public static class ContactDto {
        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
    }
}
