package pl.allegro.atm;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MerlinLevelRepository {
    private final Map<Integer, MerlinLevel> levels;

    public MerlinLevelRepository(List<MerlinLevel> levels) {
        this.levels = levels.stream().collect(Collectors.toMap(MerlinLevel::getOrder, Function.identity()));
    }

    public MerlinLevel getLevel(int level) {
        if (level < 1 || level > levels.size()) {
            throw new IllegalArgumentException("Level %d does not exist" .formatted(level));
        }
        return levels.get(level);
    }

    public boolean isLastLevel(int level) {
        return level == levels.size();
    }

    public int count() {
        return levels.size();
    }
}
