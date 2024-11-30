package de.jguhlke.mpw.adapter.in.rest.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class FallbackExceptionMapper implements ExceptionMapper<Exception> {
  @Override
  public Response toResponse(Exception exception) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("An unexpected error occurred")
        .build();
  }
}
