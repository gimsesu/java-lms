package nextstep.qna.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryQuestionRepository implements QuestionRepository {
    private Map<Long, Question> database = new HashMap<>();

    public InMemoryQuestionRepository() {}

    public InMemoryQuestionRepository(Question... questions) {
        for (Question question : questions) {
            save(question);
        }
    }

    Question save(Question question) {
        database.put(question.getId(), question);
        return question;
    }

    @Override
    public Optional<Question> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    public void clear() {
        database.clear();
    }
}
