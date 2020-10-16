package sv.javawebserver;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sv.javawebserver.requesthandlerimplementation.FileRequestHandler;
import sv.javawebserver.webserver.HttpWebServer;
import sv.javawebserver.configuration.*;
import sv.javawebserver.api.*;

public class App {
   private static final Logger logger = LogManager.getLogger("App");
    public static void main(String[] args) {
        String webserverRootPath = "C:\\Sources\\UNIPER\\Workshop20180626";
        int port = 3333;
        int timeout = 45;
        int numberOfThreads = 50;

        IHttpRequestHandler   fileRequestHandler = new FileRequestHandler(webserverRootPath);
        List<IHttpRequestHandler> requestHandlers = Arrays.asList(fileRequestHandler);
        
        HttpWebServerConfiguration webserverConfiguration = new HttpWebServerConfiguration( port, timeout, numberOfThreads, requestHandlers);

        HttpWebServer server = new HttpWebServer(webserverConfiguration);
        
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(server)));

		// Start the server
		server.run();
    }

    private static class ShutdownHook implements Runnable {

        private HttpWebServer server;
    
        ShutdownHook(HttpWebServer server){
            this.server = server;
        }
    
        @Override 
        public void run()
        {
            if (server.running()){
                server.stop();
            }
        }
    }
}


