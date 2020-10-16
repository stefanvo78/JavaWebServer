package sv.javawebserver.api;

import java.net.URI;
import java.io.InputStream;

public interface IHttpRequest extends IHttpMessage {

    HttpMethod method();
    URI uri();
   
}