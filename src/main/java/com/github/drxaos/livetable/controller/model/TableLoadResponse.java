package com.github.drxaos.livetable.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableLoadResponse implements Serializable {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row implements Serializable {
        private String artist;
        private String title;
        private String isrc;
    }

    private String album;
    private List<Row> rows;
    private List<Selection> sessions;
}
