package sv.javawebserver.api;

public enum HttpStatus {
        OK_200(200, "OK"),
        BAD_REQUEST_400(400, "Bad Request"),
        UNAUTHORIZED_401(401, "Unauthorized"),
        FORBIDDEN_403(403, "Forbidden"),
        NOT_FOUND_404(404, "Not Found"),
        METHOD_NOT_ALLOWED_405(405, "Method Not Allowed"),
        INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
        NOT_IMPLEMENTED_501(501, "Not Implemented"),
        HTTP_VERSION_NOT_SUPPORTED_505(505, "Http Version not supported");

    private final int statusCode;
    
    public int statusCode() {
		return statusCode;
	}
    
	private final String description;
    public String description() {
		return description;
	}
    HttpStatus(int statusCode, String description)
    {
        this.statusCode = statusCode;
        this.description = description;
    }
}


