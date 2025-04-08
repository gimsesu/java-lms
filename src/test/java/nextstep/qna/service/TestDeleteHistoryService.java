package nextstep.qna.service;

import java.util.List;
import nextstep.qna.domain.DeleteHistory;
import nextstep.qna.domain.InMemoryDeleteHistoryRepository;

public class TestDeleteHistoryService extends DeleteHistoryService {
    private final InMemoryDeleteHistoryRepository deleteHistoryRepository;

    public TestDeleteHistoryService() {
        this(new InMemoryDeleteHistoryRepository());
    }

    public TestDeleteHistoryService(InMemoryDeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Override
    public void saveAll(List<DeleteHistory> deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories);
    }

    public List<DeleteHistory> findAll() {
        return deleteHistoryRepository.findAll();
    }
}
