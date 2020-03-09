package com.ecdragon.parzival.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * From smartclient:
 * 
 * An unrecoverable error, such as an unexpected server failure, can be flagged by setting 
 *   <status> to -1 and setting <data> to an error message. 
 *   In this case the <errors> element is not used (it's specific to validation errors).
 * 
 {
    "response": {
       "status": 0,
       "startRow": 0,
       "endRow": 76,
       "totalRows": 546,
       "data": [
           {"field1": "value", "field2": "value"},
           {"field1": "value", "field2": "value"},
           ... 76 total records ...
       ]
    }
 }
 *
 *
 {    "response":
      {   "status": -4,
          "errors":
              {   "field1": {"errorMessage": "A validation error on field1"},
                  "field2": {"errorMessage": "A validation error on field2"}
              }
      }
 }
 *
 *
 {    "response":
      {   "status": -4,
          "errors":
              {   "field1": [
                      {"errorMessage": "First error on field1"},
                      {"errorMessage": "Second error on field1"}
                  ]
              }
      }
 }
 */
public class ApiResponse {
	
	private Integer status;
	private Object errors;
	private List<Object> data;
	
//	private static ObjectMapper objectMapper = new ObjectMapper();

	public <T extends Object> void addDataRecord(T dataRecord) {
		if (dataRecord == null) {
			return;
		}
		if (data == null) {
			data = new ArrayList<>();
		}
		try {
			data.add(dataRecord);
		} 
		catch (Exception e) {
			// handle exception
		}
	}
	
	public void addDataRecords(Collection<? extends Object> dataRecords) {
		if (dataRecords == null) {
			return;
		}
		for (Object dataRecord : dataRecords) {
			this.addDataRecord(dataRecord);
		}
	}
	
	public static ApiResponse createErrorResponse(String errorMessage) {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setResponseStatus(ResponseStatus.errorGeneral);
		try {
			apiResponse.addDataRecord(errorMessage);
//			apiResponse.setData(objectMapper.valueToTree(errorMessage));
		} 
		catch (Exception e) {
			// Skip it
		}
		return apiResponse;
	}
	
	public static ApiResponse createSuccessResponseWithOneDataRecord(Object dataToReturn) {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setResponseStatus(ResponseStatus.success);
		apiResponse.addDataRecord(dataToReturn);
		return apiResponse;
	}
	
	public static ApiResponse createSuccessResponseWithListOfDataRecords(Collection<? extends Object> dataToReturn) {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setResponseStatus(ResponseStatus.success);
		apiResponse.addDataRecords(dataToReturn);
		return apiResponse;
	}
	
	public void setResponseStatus(ResponseStatus responseStatus) {
		if (responseStatus == null) {
			this.status = ResponseStatus.errorGeneral.getStatusInteger();
		}
		this.status = responseStatus.getStatusInteger();
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
	public Object getErrors() {
		return errors;
	}
	public void setErrors(Object errors) {
		this.errors = errors;
	}
	
	public enum ResponseStatus {
		success(0),
		errorGeneral(-1),
		errorValidation(-4)
		;
		
		private Integer statusInteger;
		public Integer getStatusInteger() {
			return statusInteger;
		}
		private ResponseStatus(Integer statusInteger) {
			this.statusInteger = statusInteger;
		}
	}
}


