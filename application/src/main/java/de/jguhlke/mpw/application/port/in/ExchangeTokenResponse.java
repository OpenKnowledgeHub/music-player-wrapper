package de.jguhlke.mpw.application.port.in;

public record ExchangeTokenResponse(String accessToken, String refreshToken, long expiresIn) {}
