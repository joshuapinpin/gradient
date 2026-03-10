package com.jpin.gradient.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseCreateRequest {

	@NotBlank
	@Size(max = 50)
	private String name;
}
