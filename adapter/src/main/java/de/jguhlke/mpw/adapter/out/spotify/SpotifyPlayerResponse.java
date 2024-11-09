package de.jguhlke.mpw.adapter.out.spotify;

record SpotifyPlayerResponse(
    SpotifyDeviceResponse device, boolean is_playing, SpotifyTrackResponse item) {}
