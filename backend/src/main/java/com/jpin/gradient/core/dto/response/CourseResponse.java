package com.jpin.gradient.core.dto.response;

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
