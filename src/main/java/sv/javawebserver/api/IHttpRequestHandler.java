package sv.javawebserver.api;


public interface IHttpRequestHandler{
    
   boolean canHandleRequest ( IHttpRequest request);

   IHttpResponse handleRequest( IHttpRequest request);

}

