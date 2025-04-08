package nextstep.qna.service;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.NotFoundException;
import nextstep.qna.domain.*;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("qnaService")
public class QnAService {
    private final QuestionRepository questionRepository;
    private final DeleteHistoryService deleteHistoryService;

    public QnAService() {
        this(null, null);
    }

    public QnAService(QuestionRepository questionRepository,
                    DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional
    public void deleteQuestion(NsUser loginUser, long questionId) throws CannotDeleteException {
        Question question =
                        questionRepository.findById(questionId).orElseThrow(NotFoundException::new);
        question.delete(loginUser);
        deleteHistoryService.saveAll(question.createDeleteHistories());
    }
}
