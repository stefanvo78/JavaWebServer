package sv.javawebserver.responseimplementation;

import sv.javawebserver.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.nio.charset.StandardCharsets;

public class HttpErrorResponse extends HttpResponse {
	public HttpErrorResponse(HttpStatus status, String httpStatus) {
		super(status,httpStatus);
	}

	@Override
	public InputStream stream() {
		String body = MessageFormat.format("<title>{0} {1}</title><h1>{0} {1}</h1>", status().statusCode(),
				status().description());
		return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
	}
}