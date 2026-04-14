package com.jpin.gradient.core.course.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseResponse {
	private Long id;
	private String name;
	private Integer assessmentCount;
	private Long termId;
}
