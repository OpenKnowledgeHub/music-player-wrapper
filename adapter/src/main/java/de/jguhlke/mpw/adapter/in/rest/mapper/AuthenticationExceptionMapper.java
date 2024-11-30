package de.jguhlke.mpw.adapter.in.rest.mapper;

import de.jguhlke.mpw.application.exception.AuthenticationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationException> {
  @Override
  public Response toResponse(AuthenticationException exception) {
    return Response.status(Response.Status.UNAUTHORIZED).entity(exception.getMessage()).build();
  }
}
