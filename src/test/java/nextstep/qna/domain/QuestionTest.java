package nextstep.qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        answer = new Answer(11L, NsUserTest.JAVAJIGI, question, "답변 내용");
        question.addAnswer(answer);
    }

    @Test
    @DisplayName("질문 작성자와 로그인 사용자가 같으면 삭제 가능")
    void delete_when_question_owner() throws Exception {
        question.delete(NsUserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문 작성자와 로그인 사용자가 다르면 예외 발생")
    void delete_when_not_question_owner() {
        assertThatThrownBy(() -> question.delete(NsUserTest.SANJIGI))
                        .isInstanceOf(CannotDeleteException.class).hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("답변이 없는 경우 삭제 가능")
    void delete_when_no_answers() throws Exception {
        Question emptyQuestion = new Question(2L, NsUserTest.JAVAJIGI, "제목", "내용");

        emptyQuestion.delete(NsUserTest.JAVAJIGI);

        assertThat(emptyQuestion.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 작성자와 로그인 사용자가 다르면 예외 발생")
    void delete_when_different_answer_owner() {
        Answer differentOwnerAnswer = new Answer(12L, NsUserTest.SANJIGI, question, "다른 사람의 답변");
        question.addAnswer(differentOwnerAnswer);

        assertThatThrownBy(() -> question.delete(NsUserTest.JAVAJIGI))
                        .isInstanceOf(CannotDeleteException.class)
                        .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("deleteHistories 생성 검증")
    void createDeleteHistories() {
        List<DeleteHistory> deleteHistories = question.createDeleteHistories();

        assertThat(deleteHistories).hasSize(2);
        DeleteHistory questionHistory = deleteHistories.get(0);
        DeleteHistory answerHistory = deleteHistories.get(1);

        assertThat(questionHistory.getContentType()).isEqualTo(ContentType.QUESTION);
        assertThat(questionHistory.getContentId()).isEqualTo(question.getId());

        assertThat(answerHistory.getContentType()).isEqualTo(ContentType.ANSWER);
        assertThat(answerHistory.getContentId()).isEqualTo(answer.getId());
    }
}
