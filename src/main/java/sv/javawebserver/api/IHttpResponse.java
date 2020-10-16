package sv.javawebserver.api;

import java.net.URI;
import java.io.InputStream;

public interface IHttpResponse extends IHttpMessage {

   HttpStatus status();
  
}