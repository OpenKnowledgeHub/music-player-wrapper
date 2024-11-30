package de.jguhlke.mpw.adapter.in.rest.mapper;

import de.jguhlke.mpw.application.exception.InvalidClientInputException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class InvalidClientInputExceptionMapper implements ExceptionMapper<InvalidClientInputException> {
  @Override
  public Response toResponse(InvalidClientInputException exception) {
    return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
  }
}
