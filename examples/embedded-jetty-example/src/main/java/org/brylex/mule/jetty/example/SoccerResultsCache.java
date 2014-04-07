package org.brylex.mule.jetty.example;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class SoccerResultsCache {

    private final Logger logger = LoggerFactory.getLogger(SoccerResultsCache.class);

    private final Map<String, LocalDate> dateMap;
    private final Map<LocalDate, Set<SoccerResult>> map;

    public SoccerResultsCache() {
        this.dateMap = Maps.newHashMap();
        this.map = Maps.newHashMap();
    }

    public void apply(SoccerResult result) {

        LocalDate date = getMatchDate(result);

        Set<SoccerResult> results = map.get(date);
        if (results == null) {
            map.put(date, results = Sets.<SoccerResult>newHashSet());
        }

        results.add(result);
        logger.debug("Caching {} -> {}", date, result);
    }

    public Set<SoccerResult> all() {
        Set<SoccerResult> results = map.get(new LocalDate());
        if (results == null) {
            return Sets.newHashSet();
        }

        return Sets.newHashSet(results);
    }

    private LocalDate getMatchDate(SoccerResult result) {
        String dateString = result.getSoccerMatch().getDate();
        LocalDate date = dateMap.get(dateString);
        if (date == null) {
            dateMap.put(dateString, date = new LocalDate(dateString));
        }
        return date;
    }
}
