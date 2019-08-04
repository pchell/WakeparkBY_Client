package com.wakeparkby.HTTPController;

import lombok.Data;

@Data
public class Time {
    private String startHours;
    private String startMinutes;
    private String endHours;
    private String endMinutes;
    private String status;


    public Time(String startHours, String startMinutes, String endHours, String endMinutes, String status) {
        this.startMinutes = startMinutes;
        this.startHours = startHours;
        this.endHours = endHours;
        this.endMinutes = endMinutes;
        this.status = status;
    }
}
