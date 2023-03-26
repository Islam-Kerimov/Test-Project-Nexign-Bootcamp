package com.nexign.model.tariff;

import com.nexign.model.CallType;

import java.time.LocalTime;

public interface Tariff {
    double addCostCall(CallType callType, LocalTime duration);
}
