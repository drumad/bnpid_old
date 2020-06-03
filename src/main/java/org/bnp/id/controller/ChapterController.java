package org.bnp.id.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.bnp.id.model.info.Chapter;
import org.bnp.id.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Log4j
public class ChapterController {

    @Getter
    private Map<Integer, Chapter> chapters;

    private ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {

        this.chapterService = chapterService;

        try {
            loadChapters();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void loadChapters() throws SQLException {

        log.debug("Loading chapters...");

        if (chapters != null) {
            chapters.clear();
        }
        chapters = chapterService.findAll().stream().collect(Collectors.toMap(Chapter::getId, Function.identity()));
    }
}
