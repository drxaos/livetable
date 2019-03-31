package com.github.drxaos.livetable.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Selection implements Serializable {
    private String uid;
    private int left, top, bottom, right;
    private boolean select;
}
