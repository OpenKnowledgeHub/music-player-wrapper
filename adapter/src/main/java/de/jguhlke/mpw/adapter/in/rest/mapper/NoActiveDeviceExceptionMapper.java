package de.jguhlke.mpw.adapter.in.rest.mapper;

import de.jguhlke.mpw.application.exception.NoActiveDeviceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NoActiveDeviceExceptionMapper implements ExceptionMapper<NoActiveDeviceException> {
  @Override
  public Response toResponse(NoActiveDeviceException exception) {
    return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
  }
}
