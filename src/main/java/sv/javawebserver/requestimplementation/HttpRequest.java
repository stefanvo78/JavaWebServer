package sv.javawebserver.requestimplementation;

import java.net.URISyntaxException;
import sv.javawebserver.api.*;
import java.net.URI;
import sv.javawebserver.headerimplementation.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class HttpRequest implements IHttpRequest {

	private HttpMethod method;
	private URI uri;
	// private HttpVersion httpVersion;
	private String httpVersion;
	private HttpHeaders headers;
	private String body;

	public HttpRequest(HttpMethod method, URI uri, HttpHeaders headers, String body) {
		this.method = method;
		this.uri = uri;
		this.headers = headers;
		this.body = body;
	}

	public HttpRequest(String request) {
		parseRequest(request);
	}

	private void parseRequest(String request) {
		String[] requestParts = request.trim().split(" +", 3);
		if (requestParts.length != 3) {
			throw new IllegalArgumentException("Invalid request, wrong format: " + request);
		}

		method = HttpMethod.valueOf(requestParts[0].trim());

		try {
			uri = new URI(requestParts[1]);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid request URI", e);
		}
		this.httpVersion = requestParts[2].trim();
		// httpVersion = new HttpVersion(requestParts[2].trim());
	}

	@Override
	public HttpMethod method() {
		return this.method;
	}

	@Override
	public URI uri() {
		return this.uri;
	}

	@Override
	public HttpHeaders headers() {
		return headers;
	}

	@Override
	public String httpVersion() {
		return this.httpVersion;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	@Override
	public InputStream stream() {
		return new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8));
	}

}