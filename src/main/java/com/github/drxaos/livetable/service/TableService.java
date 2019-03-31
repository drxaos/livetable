package com.github.drxaos.livetable.service;

import com.github.drxaos.livetable.controller.model.TableLoadResponse;
import com.github.drxaos.livetable.db.AlbumRepository;
import com.github.drxaos.livetable.db.TrackRepository;
import com.github.drxaos.livetable.db.model.Album;
import com.github.drxaos.livetable.db.model.Track;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TableService {
    public static final String PLACE_PREFIX = "album:";

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final AuthService authService;

    public TableLoadResponse load(AuthService.Auth auth) {
        if (!auth.place.startsWith(PLACE_PREFIX)) {
            throw new IllegalArgumentException("wrong request");
        }

        long albumId = Long.parseLong(auth.place.substring(PLACE_PREFIX.length()));

        Album album = albumRepository.findById(albumId).orElseGet(() -> new Album(null, "New album"));
        List<Track> tracks = trackRepository.findAllByAlbumOrderByPositionAsc(album);
        List<AuthService.Auth> sessions = authService.getSessionsByPlace(auth.getPlace());

        return new TableLoadResponse(
                album.getName(),
                tracks.stream()
                        .map((t) -> new TableLoadResponse.Row(
                                t.getArtist(),
                                t.getTitle(),
                                t.getIsrc())
                        ).collect(Collectors.toList()),
                sessions.stream()
                        .map(AuthService.Auth::getSelection)
                        .collect(Collectors.toList())
        );
    }

}
