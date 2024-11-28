package com.example.SERVER.domain.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultPaginationDTO {
	private Meta meta;
	private Object result;
}
