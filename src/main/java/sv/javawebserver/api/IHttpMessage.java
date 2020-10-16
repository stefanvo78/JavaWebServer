package sv.javawebserver.api;

import java.net.URI;
import java.io.InputStream;
import sv.javawebserver.headerimplementation.*;

public interface IHttpMessage {
    InputStream stream();

    HttpHeaders headers();
    // HttpVersion version();
    String httpVersion();
}