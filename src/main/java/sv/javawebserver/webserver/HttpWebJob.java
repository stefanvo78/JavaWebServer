package sv.javawebserver.webserver;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.*;

import sv.javawebserver.responseimplementation.*;
import sv.javawebserver.requestimplementation.*;
import sv.javawebserver.api.*;
import sv.javawebserver.headerimplementation.*;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import org.apache.commons.io.IOUtils;
import java.util.List;
import com.google.common.base.Strings;

import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class HttpWebJob implements Runnable {
	// private static final Logger logger = LogManager.getLogger("HttpWebJob");
	private final HttpResponseFactory responseFactory;
	private final Socket socket;
	private boolean keepAlive = false;
	private InputStream inputStream;
	private OutputStream outputStream;
	private HttpErrorResponseFactory errorResponseFactory;
	private HttpResponseWriter responseWriter;

	public HttpWebJob(HttpResponseFactory responseFactory, Socket socket) {
		this.responseFactory = responseFactory;
		this.socket = socket;
		this.keepAlive = true;
		this.errorResponseFactory = new HttpErrorResponseFactory();
	}

	@Override
	public void run() {
		try {
			executeRequest();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			closeConnection();
		}
	}

	private void executeRequest() {

		try {
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			responseWriter = new HttpResponseWriter(outputStream);
		} catch (IOException e) {
			// logger.error("Client Connetion failed", e);
			return;
		}

		while (keepAlive) {

			IHttpRequest request;
			IHttpResponse response;

			try {

				request = readRequest();

				this.keepAlive = checkIfIsKeepalive(request);

				response = generateResponse(request);
			} catch (Exception e) {
				response = errorResponseFactory.getErrorResponse("BAD_REQUEST_400");
			}

			if (keepAlive) {
				keepAlive = checkIfIsKeepalive(response);
			}

			writeResponse(response);
		}
	}

	private void writeResponse(IHttpResponse response) {
		try {
			responseWriter.writeResponse(response);
		} catch (HttpException e) {
			// logger.error("Unable to write Response", e);
		}
	}

	private IHttpResponse generateResponse(IHttpRequest request) {
		try {
			return responseFactory.response(request);
		} catch (Exception e) {
			// logger.error("Error generating response message", e);
			return errorResponseFactory.getErrorResponse("INTERNAL_SERVER_ERROR_500");
		}
	}

	private IHttpRequest readRequest() {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
		try {
			HttpRequest httpRequest = new HttpRequest(inputReader.readLine());
			HttpHeaders headers = readHeaders(inputReader);

			String body = "";
			if (headers.contains("Content-length") || headers.contains("Transfer-Encoding")) {
				body = readBody(inputReader);
			}

			httpRequest.setBody(body);
			httpRequest.setHeaders(headers);

			return httpRequest;
		} catch (Exception e) {
			// logger.error("Unable to parse input stream into a Request object.");
			throw new HttpException("Unable to parse input stream into a Request object.", e);
		}
	}

	private HttpHeaders readHeaders(BufferedReader inputReader) throws IOException {
		HttpHeaders headers = new HttpHeaders();

		if (inputReader.ready()) {
			String line = inputReader.readLine();
			while (line != null && !"".equals(line)) {
				headers.add(parseHeaderLine(line));
				line = inputReader.readLine();
			}
		}

		return headers;
	}

	private HttpHeader parseHeaderLine(String line) {
		int splitPos = line.indexOf(':');

		if (splitPos == -1) {
			throw new IllegalArgumentException("Malformed header: " + line);
		}

		String name = line.substring(0, splitPos).trim();
		String[] values = line.substring(splitPos + 1, line.length()).split(",");

		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}

		return new HttpHeader(name, values);
	}

	private String readBody(BufferedReader inputReader) throws IOException {
		return IOUtils.toString(inputReader);
	}

	private void closeConnection() {
		try {
			socket.close();
		} catch (IOException e) {
			// logger.error("Failure on closing connection", e);
		}
	}

	private boolean checkIfIsKeepalive(IHttpMessage message) {
		List<String> connectionValues = message.headers().values("Connection");
		boolean returnValue = false;
		if (connectionValues != null) {
			if (connectionValues.contains("keep-alive")) {
				returnValue = true;
			} else if (connectionValues.contains("close")) {
				returnValue = false;
			}
		}

		return returnValue;
	}

}