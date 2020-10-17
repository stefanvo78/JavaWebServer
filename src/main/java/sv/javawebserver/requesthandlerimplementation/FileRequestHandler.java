package sv.javawebserver.requesthandlerimplementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import sv.javawebserver.responseimplementation.*;
import sv.javawebserver.api.*;
import sv.javawebserver.headerimplementation.*;
import sv.javawebserver.requestimplementation.*;
import sv.javawebserver.api.IHttpRequestHandler;
import sv.javawebserver.tools.*;
import org.apache.tika.Tika;

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
        File requestedFile = new File(this.path, request.uri().toString());

        IHttpResponse response;

        if ( !requestedFile.exists()) {
            return new HttpErrorResponseFactory().getErrorResponse("NOT_FOUND_404");
        } else if ( requestedFile.isDirectory()) {
            response = handleDirectory(request, requestedFile);
        }
        else{
            response  = handleFile(request,requestedFile);
        }
        
        return response;
    }

    private IHttpResponse handleFile(IHttpRequest request,File requestedFile){
        HttpStatus status = HttpStatus.OK_200;
      
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-length", String.valueOf( requestedFile.length()));

        try {
			headers.set("Content-type", getContentType(requestedFile));
		} catch (IOException e) {
			//throw new ResponseException("Failed to detect MIME type of " + file.getPath(), e);
		}
        

        HttpResponse response = new HttpResponse(status, headers, "", request.httpVersion());
        try {
            response.setStream(new FileInputStream(requestedFile));
		} catch (FileNotFoundException e) {
			//throw new ResponseException("Requested file does not exist", e);
		}
       
        return response;
    }

    private String getContentType(File file)throws IOException{
		Tika tika = new Tika();
		return tika.detect(file);
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