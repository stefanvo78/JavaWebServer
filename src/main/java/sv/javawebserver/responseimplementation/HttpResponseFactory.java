package sv.javawebserver.responseimplementation;

import java.util.List;
import sv.javawebserver.api.*;

public class HttpResponseFactory {
	private List<IHttpRequestHandler> requestHandlers;
	private HttpErrorResponseFactory errorResponseFactory;

	public HttpResponseFactory(List<IHttpRequestHandler> requestHandlers) {
		this.requestHandlers = requestHandlers;
		this.errorResponseFactory = new HttpErrorResponseFactory();
	}

	public IHttpResponse response(IHttpRequest request) {

		IHttpResponse response = null;

		for (IHttpRequestHandler handler : requestHandlers) {
			if (handler.canHandleRequest(request)) {
				response = handler.handleRequest(request);
				break;
			}
		}

		if (response == null) {
			response = errorResponseFactory.getErrorResponse("NOT_IMPLEMENTED_501");
		}

		return response;
	}

}