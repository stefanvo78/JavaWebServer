package sv.javawebserver;

import java.util.*;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

import sv.javawebserver.requesthandlerimplementation.FileRequestHandler;
import sv.javawebserver.webserver.HttpWebServer;
import sv.javawebserver.configuration.*;
import sv.javawebserver.api.*;

import com.google.common.base.Strings;

public class App {
   
    public static void main(final String[] args) {
        // Logger logger = LogManager.getRootLogger();
        String webserverRootPath = System.getProperty("user.dir");
        int port = 3333;
        int timeout = 45;
        int numberOfThreads = 50;

        if ( args.length > 0 && !Strings.isNullOrEmpty(args[0])){
            webserverRootPath =  args[0];
        }
        
        if ( args.length > 1 && !Strings.isNullOrEmpty(args[1])){
            try{
                port = Integer.parseInt (args[1]);
            }catch (NumberFormatException ex){
                // logger.error("Value for port not valid. Standard (80) will be taken");
            }
        }

        if ( args.length > 2 && !Strings.isNullOrEmpty(args[2])){
            try{
                timeout = Integer.parseInt (args[2]);
            }catch (NumberFormatException ex){
                //logger.error("Value for timeout not valid. Standard (45) will be taken");
            }
        }

        if ( args.length > 3 && !Strings.isNullOrEmpty(args[3])){
            try{
                numberOfThreads = Integer.parseInt (args[3]);
            }catch (NumberFormatException ex){
                //logger.error("Value for numberofThreads not valid. Standard (50) will be taken");
            }
        }

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


