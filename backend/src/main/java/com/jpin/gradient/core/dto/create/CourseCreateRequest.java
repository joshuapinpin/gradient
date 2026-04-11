package com.jpin.gradient.core.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseCreateRequest {

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotNull
	private Long termId;
}
