package com.bitespeed.identityReconciliation.service;

import com.bitespeed.identityReconciliation.dto.ContactResponse;
import com.bitespeed.identityReconciliation.dto.IdentityRequest;

public interface ContactService {

     ContactResponse identifyContact(IdentityRequest request);
}
