package org.bnp.id.service;

import org.bnp.id.model.info.Chapter;
import org.bnp.id.model.info.Country;

import java.sql.SQLException;
import java.util.Collection;

public interface ChapterService extends Service<Chapter> {

    Collection<Chapter> findByCountry(Country country) throws SQLException;
}
