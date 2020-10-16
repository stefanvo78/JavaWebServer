package sv.javawebserver.configuration;

import sv.javawebserver.api.*;
import java.util.List;

public class HttpWebServerConfiguration {
    private int portNumber;
    private int timeoutInSecs;
	private List<IHttpRequestHandler> requestHandlers;
	private int numberOfThreads;
	
	
	public int numberOfThreads(){
		return numberOfThreads;
	}
	public int portNumber(){
		return portNumber;
	}

	public int timeoutInSecs(){
		return timeoutInSecs;
	}

	public List<IHttpRequestHandler> requestHandlers(){
		return requestHandlers;
	}

    /**
	 *                        HttpWebServerConfiguration by passing values  
     *
	 * @param portNumber            The port-numer the Server will be bound to 
	 * @param timeoutInSecs         Request Timeout in Seconds
	 * @param requestHandlers A list of handlers to use to service requests. The order of the list determines the
	 *                        priority of the handlers (handlers appearing first will be given first opportunity to
	 *                        handle requests).
	 * @throws IllegalArgumentException The portnumber has to be between 0-65535
	 * @throws IllegalArgumentException RequestHandlers has to be defined and > 0
	 */
    public HttpWebServerConfiguration (int portNumber, int timeoutInSecs, int numberOfThreads, List<IHttpRequestHandler> requestHandlers )    {
        if (portNumber < 0 || portNumber > 65535) {
			throw new IllegalArgumentException("Portnumber has to be between 0-65535.");
		}
		
		if (requestHandlers == null || requestHandlers.isEmpty()) {
			throw new IllegalArgumentException("The list of requesthandlers has to be set and > 0");
		}

		this.portNumber = portNumber;
        this.requestHandlers = requestHandlers;
		this.timeoutInSecs = timeoutInSecs;
		this.numberOfThreads = numberOfThreads;
    }
}