package de.jguhlke.mpw.adapter.in.rest.mapper;

import de.jguhlke.mpw.model.exception.MusicPlayerWrapperException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class MusicPlayerWrapperExceptionMapper implements ExceptionMapper<MusicPlayerWrapperException> {
  @Override
  public Response toResponse(MusicPlayerWrapperException exception) {
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(exception.getMessage())
        .build();
  }
}
