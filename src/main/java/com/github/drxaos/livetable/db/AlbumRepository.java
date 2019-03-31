package com.github.drxaos.livetable.db;

import com.github.drxaos.livetable.db.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

}