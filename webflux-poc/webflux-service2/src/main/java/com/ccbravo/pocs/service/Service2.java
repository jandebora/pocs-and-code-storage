package com.ccbravo.pocs.service;

import com.ccbravo.pocs.model.MessageSumDTO;

public interface Service2 {

    MessageSumDTO getMessageSumAsync();
    MessageSumDTO getMessageSumSync();
}
