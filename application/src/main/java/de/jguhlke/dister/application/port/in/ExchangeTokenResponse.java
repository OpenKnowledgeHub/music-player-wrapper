package de.jguhlke.dister.application.port.in;

public record ExchangeTokenResponse(String accessToken, String refreshToken, long expiresIn) {}
