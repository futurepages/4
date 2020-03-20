package org.futurepages.jersey.filters;

import org.futurepages.core.exception.AppLogger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AppExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    HttpServletRequest req;

    @Override
    public Response toResponse(Exception exception) {
        AppLogger.getInstance().execute(exception,req);
        return Response.status(500).build();
    }
}