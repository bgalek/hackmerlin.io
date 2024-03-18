package com.github.bgalek;

import com.github.bgalek.levels.MerlinLevel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class MerlinLevelRepository {
    private final Map<Integer, MerlinLevel> levels;

    MerlinLevelRepository(List<MerlinLevel> levels) {
        this.levels = levels.stream().collect(Collectors.toMap(MerlinLevel::getOrder, Function.identity()));
    }

    MerlinLevel getLevel(int level) {
        if (level < 1 || level > levels.size()) {
            throw new IllegalArgumentException("Level %d does not exist".formatted(level));
        }
        return levels.get(level);
    }

    int count() {
        return levels.size();
    }
}
