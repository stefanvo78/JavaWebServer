package sv.javawebserver.responseimplementation;

import sv.javawebserver.api.*;
import sv.javawebserver.headerimplementation.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse implements IHttpResponse {
	private HttpStatus status;
	private HttpHeaders headers;
	private String body;
	private String httpVersion;
	private InputStream stream;

	HttpResponse(HttpStatus status, String httpVersion) {
		this(status, new HttpHeaders(), "", httpVersion);
	}

	public HttpResponse(HttpStatus status, HttpHeaders headers, String body, String httpVersion) {
		this.status = status;
		this.headers = headers;
		this.body = body;
		this.httpVersion = httpVersion;
		this.stream =  new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
	}

	public HttpResponse(HttpStatus status, String body, String httpVersion) {

		this.headers = new HttpHeaders(new HttpHeader("Content-length",String.valueOf(body.length())));
		

		this.status = status;
		this.body = body;
		this.httpVersion = httpVersion;
		this.stream =  new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));

		
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	@Override
	public InputStream stream() {
		return this.stream;
	}

	@Override
	public HttpHeaders headers() {
		return headers;
	}

	@Override
	public HttpStatus status() {
		return status;
	}

	@Override
	public String httpVersion() {
		return httpVersion;
	}
}
