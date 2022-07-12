package org.infoshare.rekinyfinansjeryweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private int numberOfPages;
    private long totalDailyTables;
    private List<T> tables;
}
