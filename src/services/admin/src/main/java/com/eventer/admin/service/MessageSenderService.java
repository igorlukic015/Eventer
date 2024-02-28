package com.eventer.admin.service;

import com.eventer.admin.contracts.Message;

public interface MessageSenderService {
    void sendMessage(String recipient, String message);
}
