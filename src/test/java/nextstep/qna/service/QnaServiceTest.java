package nextstep.qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.qna.NotFoundException;
import nextstep.qna.domain.*;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QnaServiceTest {
    @Test
    @DisplayName("질문 삭제 시 DeleteHistory가 생성되고 저장된다")
    public void deleteQuestion_SavesDeleteHistories() throws Exception {
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title1", "contents1");
        Answer answer = new Answer(11L, NsUserTest.JAVAJIGI, question, "답변 내용");
        question.addAnswer(answer);

        QuestionRepository questionRepository = new InMemoryQuestionRepository(question);
        InMemoryDeleteHistoryRepository deleteHistoryRepository =
                        new InMemoryDeleteHistoryRepository();
        DeleteHistoryService deleteHistoryService =
                        new TestDeleteHistoryService(deleteHistoryRepository);
        QnAService qnAService = new QnAService(questionRepository, deleteHistoryService);

        qnAService.deleteQuestion(NsUserTest.JAVAJIGI, question.getId());

        List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();
        assertThat(deleteHistories).hasSize(2);

        DeleteHistory questionDeleteHistory = deleteHistories.get(0);
        assertThat(questionDeleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
        assertThat(questionDeleteHistory.getContentId()).isEqualTo(question.getId());

        DeleteHistory answerDeleteHistory = deleteHistories.get(1);
        assertThat(answerDeleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(answerDeleteHistory.getContentId()).isEqualTo(answer.getId());
    }

    @Test
    @DisplayName("존재하지 않는 질문을 삭제하려고 하면 NotFoundException이 발생한다")
    public void deleteQuestion_ThrowsNotFoundException_WhenQuestionNotFound() {
        QuestionRepository questionRepository = new InMemoryQuestionRepository();
        DeleteHistoryService deleteHistoryService = new TestDeleteHistoryService();
        QnAService qnAService = new QnAService(questionRepository, deleteHistoryService);

        assertThatThrownBy(() -> qnAService.deleteQuestion(NsUserTest.JAVAJIGI, 9999L))
                        .isInstanceOf(NotFoundException.class);
    }
}
