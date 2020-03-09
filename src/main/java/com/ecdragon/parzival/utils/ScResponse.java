package com.ecdragon.parzival.utils;

import java.util.Collection;

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
public class ScResponse {
	
	private ApiResponse response;
	
	public ScResponse() {
		// Default no-arg constructor
	}
	
	public ScResponse(ApiResponse apiResponse) {
		this.response = apiResponse;
	}
	
	public static ScResponse createSuccessResponseWithOneDataRecord(Object dataToReturn) {
		ApiResponse apiResponse = ApiResponse.createSuccessResponseWithOneDataRecord(dataToReturn);
		ScResponse scResponse = new ScResponse(apiResponse);
		return scResponse;
	}
	
	public static ScResponse createSuccessResponseWithListOfDataRecords(Collection<? extends Object> dataToReturn) {
		ApiResponse apiResponse = ApiResponse.createSuccessResponseWithListOfDataRecords(dataToReturn);
		ScResponse scResponse = new ScResponse(apiResponse);
		return scResponse;
	}
	
	public static ScResponse createErrorResponse(String errorMessage) {
		ApiResponse apiResponse = ApiResponse.createErrorResponse(errorMessage);
		ScResponse scResponse = new ScResponse(apiResponse);
		return scResponse;
	}

	public ApiResponse getResponse() {
		return response;
	}
	public void setResponse(ApiResponse response) {
		this.response = response;
	}
}


