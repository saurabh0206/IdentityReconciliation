package com.bitespeed.identityReconciliation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
 public class ContactDto {
        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
}
