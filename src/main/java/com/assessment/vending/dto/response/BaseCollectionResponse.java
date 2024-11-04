package com.assessment.vending.dto.response;

import com.assessment.vending.util.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

import static com.assessment.vending.util.ResponseCode.SUCCESS;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class BaseCollectionResponse<T> extends BaseResponse{

	private Collection<T> data;
	
	public BaseCollectionResponse(Collection<T> data) {
		super(SUCCESS.getCode(), SUCCESS.getDescription());
		this.data = data;
	}

	public BaseCollectionResponse(ResponseCode responseCode, Collection<T> data) {
		super(responseCode.getCode(), responseCode.getDescription());
		this.data = data;
	}

	public BaseCollectionResponse(ResponseCode responseCode) {
		super(responseCode.getCode(), responseCode.getDescription());
	}
	
	public BaseCollectionResponse(String responseCode, String responseMessage) {
		super(responseCode, responseMessage);
	}
}
