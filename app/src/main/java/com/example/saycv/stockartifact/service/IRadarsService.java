package com.example.saycv.stockartifact.service;

import org.saycv.sgs.services.ISgsBaseService;

import android.app.Activity;

public interface IRadarsService extends ISgsBaseService{
    String getDefaultIdentity();
    void setDefaultIdentity(String identity);
}
