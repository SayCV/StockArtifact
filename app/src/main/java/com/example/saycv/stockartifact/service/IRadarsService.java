package com.example.saycv.stockartifact.service;

import com.example.saycv.stockartifact.service.impl.RadarsHistoryService;

import org.saycv.sgs.services.ISgsBaseService;

public interface IRadarsService extends ISgsBaseService {
    String getDefaultIdentity();

    void setDefaultIdentity(String identity);
}
