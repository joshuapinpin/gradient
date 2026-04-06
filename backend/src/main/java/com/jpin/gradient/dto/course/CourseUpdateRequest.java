package com.jpin.gradient.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseUpdateRequest {

	@NotBlank
	@Size(max = 50)
	private String name;

	private Long termId;
}
