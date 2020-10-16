package sv.javawebserver.responseimplementation;

import java.io.Writer;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import sv.javawebserver.api.*;

public class HttpResponseWriter extends Writer {

    private OutputStream outputStream;

    public HttpResponseWriter(OutputStream outputStream) {
        super(outputStream);
        this.outputStream = outputStream;
    }

    @Override
    public void write(char[] outbuff, int off, int len) throws IOException {
        outputStream.write(new String(outbuff).getBytes(StandardCharsets.UTF_8), off, len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    public void writeResponse(IHttpResponse response) {
        try {
            // Write Response
            writeResponseStatus(response);
            writeResponseHeaders(response);
            writeResponseBody(response);

        } catch (Exception e) {
            throw new HttpException("Unable to send response", e);
        }
    }

    private void writeResponseStatus(IHttpResponse response) throws IOException {
        String line = getStatusLine(response);
        write(getStatusLine(response));
    }

    private void writeResponseHeaders(IHttpResponse response) throws IOException {
        write(response.headers().toString());
        write("\r\n");
    }

    private void writeResponseBody(IHttpResponse response) throws IOException {
        InputStream responseStream = response.stream();
        IOUtils.copy(responseStream, outputStream);
        responseStream.close();
    }

    private String getStatusLine(IHttpResponse response) {
        return  response.httpVersion().toString() + " " +  response.status().statusCode() + " " + response.status().description() + "\r\n";
    }

}
