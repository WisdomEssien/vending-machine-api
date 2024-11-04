package com.assessment.vending.dto.response;

import com.assessment.vending.util.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import static com.assessment.vending.util.ResponseCode.SUCCESS;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class BaseStandardResponse<T> extends BaseResponse{

	private T data;

	public BaseStandardResponse(T data) {
		super(SUCCESS.getCode(), SUCCESS.getDescription());
		this.data = data;
	}

	public BaseStandardResponse(ResponseCode responseCode, T data) {
		super(responseCode.getCode(), responseCode.getDescription());
		this.data = data;
	}
	
	public BaseStandardResponse(ResponseCode responseCode) {
		super(responseCode.getCode(), responseCode.getDescription());
	}
}
