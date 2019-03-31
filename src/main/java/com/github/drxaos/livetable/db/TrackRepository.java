package com.github.drxaos.livetable.db;

import com.github.drxaos.livetable.db.model.Album;
import com.github.drxaos.livetable.db.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findAllByAlbumOrderByPositionAsc(Album album);

}