package nextstep.qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswersTest {

    @Test
    @DisplayName("기본 생성자로 생성 시 빈 컬렉션이 생성된다")
    void createWithDefaultConstructor() {
        Answers answers = new Answers();

        assertThat(answers.getAnswers()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("리스트를 인자로 받는 생성자로 생성 시 해당 리스트로 초기화된다")
    void createWithList() {
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        Answer answer1 = new Answer(1L, NsUserTest.JAVAJIGI, question, "답변1");
        Answer answer2 = new Answer(2L, NsUserTest.JAVAJIGI, question, "답변2");
        List<Answer> answerList = Arrays.asList(answer1, answer2);

        Answers answers = new Answers(answerList);

        assertThat(answers.getAnswers()).containsExactly(answer1, answer2);
    }

    @Test
    @DisplayName("null 리스트로 생성 시 빈 컬렉션으로 초기화된다")
    void createWithNullList() {
        Answers answers = new Answers(null);

        assertThat(answers.getAnswers()).isEmpty();
    }

    @Test
    @DisplayName("getAnswers는 수정 불가능한 리스트를 반환한다")
    void getAnswersReturnsUnmodifiableList() {
        Answers answers = new Answers();
        List<Answer> returnedList = answers.getAnswers();

        assertThatThrownBy(() -> returnedList.add(new Answer()))
                        .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("add 메서드로 답변을 추가할 수 있다")
    void addAnswer() {
        Answers answers = new Answers();
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        Answer answer = new Answer(1L, NsUserTest.JAVAJIGI, question, "답변");

        answers.add(answer);

        assertThat(answers.getAnswers()).containsExactly(answer);
    }

    @Test
    @DisplayName("모든 답변의 작성자가 같으면 isNotOwner는 false를 반환한다")
    void isNotOwnerReturnsFalseWhenAllAnswersHaveSameOwner() {
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        Answer answer1 = new Answer(1L, NsUserTest.JAVAJIGI, question, "답변1");
        Answer answer2 = new Answer(2L, NsUserTest.JAVAJIGI, question, "답변2");
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        Answers answers = new Answers(answerList);

        assertThat(answers.isNotOwner(NsUserTest.JAVAJIGI)).isFalse();
    }

    @Test
    @DisplayName("하나라도 다른 작성자가 있으면 isNotOwner는 true를 반환한다")
    void isNotOwnerReturnsTrueWhenSomeAnswersHaveDifferentOwner() {
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        Answer answer1 = new Answer(1L, NsUserTest.JAVAJIGI, question, "답변1");
        Answer answer2 = new Answer(2L, NsUserTest.SANJIGI, question, "답변2");
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        Answers answers = new Answers(answerList);

        assertThat(answers.isNotOwner(NsUserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변이 없으면 isNotOwner는 false를 반환한다")
    void isNotOwnerReturnsFalseWhenNoAnswers() {
        Answers answers = new Answers();

        assertThat(answers.isNotOwner(NsUserTest.JAVAJIGI)).isFalse();
    }

    @Test
    @DisplayName("deleteAll 메서드는 모든 답변을 비활성화 상태로 변경한다")
    void deleteAll() {
        Question question = new Question(1L, NsUserTest.JAVAJIGI, "title", "contents");
        Answer answer1 = new Answer(1L, NsUserTest.JAVAJIGI, question, "답변1");
        Answer answer2 = new Answer(2L, NsUserTest.JAVAJIGI, question, "답변2");
        List<Answer> answerList = Arrays.asList(answer1, answer2);
        Answers answers = new Answers(answerList);

        answers.deleteAll();

        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }
}
