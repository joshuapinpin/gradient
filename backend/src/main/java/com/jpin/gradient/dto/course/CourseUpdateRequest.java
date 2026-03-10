package com.jpin.gradient.dto.course;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseUpdateRequest {

	@Size(max = 50)
	private String name;
}
