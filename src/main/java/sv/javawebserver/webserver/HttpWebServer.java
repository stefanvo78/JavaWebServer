package sv.javawebserver.webserver;

// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import sv.javawebserver.configuration.*;
import sv.javawebserver.responseimplementation.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class HttpWebServer {
   // private static final Logger logger = LogManager.getLogger("HttpWebServer");

   private final HttpResponseFactory responseFactory;
   private HttpWebServerConfiguration webserverConfiguration;
   private boolean running;
   private ExecutorService threadPool;

   public boolean running(){
      return running;
   }

   public HttpWebServer(HttpWebServerConfiguration webserverConfiguration) {
      this.webserverConfiguration = webserverConfiguration;

      responseFactory = new HttpResponseFactory(webserverConfiguration.requestHandlers());

   }

   public void run() {
      running = true;

      threadPool = Executors.newFixedThreadPool(webserverConfiguration.numberOfThreads());

      try (ServerSocket serverSocket = new ServerSocket(webserverConfiguration.portNumber())) {

         serverSocket.setSoTimeout(1000);

         // logger.info("Started server on port {}", serverSocket.getLocalPort());

         while (running) {
            executeRequest(serverSocket);
         }

      } catch (IOException e) {
         // logger.warn("Error on Connection.", e);
         stop();
      }
   }

   public void stop() {
      running = false;

      // logger.info("Stop server");

      try {
         threadPool.shutdown();
         threadPool.awaitTermination(webserverConfiguration.timeoutInSecs(), TimeUnit.SECONDS);
      } catch (InterruptedException e) {
         // logger.error("Error on Stopping Server.", e);
         Thread.currentThread().interrupt();
      }

      // logger.info("Server Stopped.");
   }

   private void executeRequest(ServerSocket serverSocket) throws IOException {
      try {
         Socket client = serverSocket.accept();
         Runnable worker = (Runnable) new HttpWebJob(responseFactory, client);
         threadPool.execute(worker);
      } catch (SocketTimeoutException ex) {
         
      }
   }

}