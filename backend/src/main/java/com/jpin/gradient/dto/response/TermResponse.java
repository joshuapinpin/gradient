package com.jpin.gradient.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TermResponse {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

}
