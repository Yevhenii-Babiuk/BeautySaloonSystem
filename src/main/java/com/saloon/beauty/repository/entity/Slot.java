package com.saloon.beauty.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Slot {

    private long id;

    private long master;

    private long user;

    private Status status;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private long procedure;

    private boolean feedbackRequest;
}
