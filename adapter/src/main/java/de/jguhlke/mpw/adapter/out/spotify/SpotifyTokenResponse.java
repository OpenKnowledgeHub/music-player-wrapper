package de.jguhlke.mpw.adapter.out.spotify;

public record SpotifyTokenResponse(
    String access_token, String token_type, String scope, long expires_in, String refresh_token) {}
