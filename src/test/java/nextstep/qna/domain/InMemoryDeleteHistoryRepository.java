package nextstep.qna.domain;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDeleteHistoryRepository implements DeleteHistoryRepository {
    private List<DeleteHistory> database = new ArrayList<>();

    @Override
    public void saveAll(List<DeleteHistory> deleteHistories) {
        database.addAll(deleteHistories);
    }

    public List<DeleteHistory> findAll() {
        return database;
    }

    public void clear() {
        database.clear();
    }
}
