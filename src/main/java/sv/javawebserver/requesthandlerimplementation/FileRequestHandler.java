package sv.javawebserver.requesthandlerimplementation;

import java.io.File;


import sv.javawebserver.responseimplementation.*;
import sv.javawebserver.api.*;
import sv.javawebserver.headerimplementation.*;
import sv.javawebserver.requestimplementation.*;
import sv.javawebserver.api.IHttpRequestHandler;
import sv.javawebserver.tools.*;

public class FileRequestHandler implements IHttpRequestHandler{
    
    String path;
    
    public FileRequestHandler(String path ){
        this.path = path;
    }
    
    @Override
	public boolean canHandleRequest(IHttpRequest request) {
		return request.method() == HttpMethod.GET || request.method() == HttpMethod.HEAD;
    }
    
    @Override
    public IHttpResponse handleRequest(IHttpRequest request) {
        File requestedFile = new File(this.path);

        IHttpResponse response;

        if ( !requestedFile.exists())
        {
            return new HttpErrorResponseFactory().getErrorResponse("NOT_FOUND_404");
        }
        else if ( requestedFile.isDirectory())
        {
            response = handleDirectory(request, requestedFile);
        }
        else{
            response  = handleFile(request,requestedFile);
        }
        
        return response;
    }

    private IHttpResponse handleFile(IHttpRequest request,File requestedFile){
        HttpStatus status = HttpStatus.OK_200;
        String body = "<h1>Test Filehandler</h1>";


        //headers().set("Content-length", String.valueOf(file.length()));
        //headers().set("Content-type", contentType(file));

        String length = String.valueOf(body.length());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-length", String.valueOf(body.length()));
        headers.set("Content-type", "text/html");
        
        // HttpHeaders headers = new HttpHeaders(new HttpHeader("Content-length",length));
        // headers.set("Content-type","text/html" );

        HttpResponse response = new HttpResponse(status, headers, body, request.httpVersion());
        return response;
    }
    private IHttpResponse handleDirectory(IHttpRequest request,File requestedFile){
        HttpStatus status = HttpStatus.OK_200;
        String body = listDirectories(requestedFile);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-length", String.valueOf(body.length()));
        headers.set("Content-type", "application/json");
        
        if ( request.method() == HttpMethod.HEAD){
            body ="";
        }

        HttpResponse response = new HttpResponse(status, headers, body, request.httpVersion());
        return response;
    }

    private String listDirectories(File requestedFile)
    {
        DirectoryStructureToJson dirStructureToJson = new DirectoryStructureToJson(requestedFile);
        return dirStructureToJson.getDirectoryStructureAsJson();
    }

}