package sv.javawebserver.responseimplementation;

import java.util.List;
import sv.javawebserver.api.*;
import java.util.HashMap;

public  class HttpErrorResponseFactory{
    private HashMap<String,HttpErrorResponse> errorResponses;

    public HttpErrorResponseFactory() {
		this.errorResponses = new HashMap<String,HttpErrorResponse>();
		this.errorResponses.put("BAD_REQUEST_400",new HttpErrorResponse(HttpStatus.BAD_REQUEST_400, "HTTP/1.0"));
		this.errorResponses.put("FORBIDDEN_403",new HttpErrorResponse(HttpStatus.FORBIDDEN_403, "HTTP/1.0"));
		this.errorResponses.put("NOT_FOUND_404",new HttpErrorResponse(HttpStatus.NOT_FOUND_404, "HTTP/1.0"));
		this.errorResponses.put("INTERNAL_SERVER_ERROR_500",new HttpErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, "HTTP/1.0"));
		this.errorResponses.put("NOT_IMPLEMENTED_501",new HttpErrorResponse(HttpStatus.NOT_IMPLEMENTED_501, "HTTP/1.0"));
		this.errorResponses.put("HTTP_VERSION_NOT_SUPPORTED_505",new HttpErrorResponse(HttpStatus.HTTP_VERSION_NOT_SUPPORTED_505, "HTTP/1.0"));

	}

	public  HttpErrorResponse getErrorResponse(String status)
	{
		return this.errorResponses.get(status);
	}
}