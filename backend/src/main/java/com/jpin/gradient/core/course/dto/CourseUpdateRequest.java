package com.jpin.gradient.core.course.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseUpdateRequest {

	@Size(max = 50)
	private String name;
}
