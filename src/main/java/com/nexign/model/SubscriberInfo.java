package com.nexign.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class SubscriberInfo {
    private CallType callType;
    private String phoneNumber;
    private LocalDateTime callStart;
    private LocalDateTime callEnd;
    private LocalTime duration;
    private TariffType tariffType;
    private double cost;

    public SubscriberInfo() {
    }

    public CallType getCallType() {
        return callType;
    }

    public void setCallType(CallType callType) {
        this.callType = callType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCallStart() {
        return callStart;
    }

    public void setCallStart(LocalDateTime callStart) {
        this.callStart = callStart;
    }

    public LocalDateTime getCallEnd() {
        return callEnd;
    }

    public void setCallEnd(LocalDateTime callEnd) {
        this.callEnd = callEnd;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public TariffType getTariffType() {
        return tariffType;
    }

    public void setTariffType(TariffType tariffType) {
        this.tariffType = tariffType;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriberInfo that = (SubscriberInfo) o;
        return callType == that.callType && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(callStart, that.callStart) && Objects.equals(callEnd, that.callEnd) && Objects.equals(duration, that.duration) && tariffType == that.tariffType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(callType, phoneNumber, callStart, callEnd, duration, tariffType);
    }

    @Override
    public String toString() {
        return "SubscriberInfo{" +
                "callType=" + callType +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", callStart=" + callStart +
                ", callEnd=" + callEnd +
                ", duration=" + duration +
                ", tariffType=" + tariffType +
                ", cost=" + cost +
                '}';
    }
}
