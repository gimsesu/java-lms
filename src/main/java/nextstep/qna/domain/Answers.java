package nextstep.qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nextstep.users.domain.NsUser;

public class Answers {
    private List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers != null ? answers : new ArrayList<>();
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public boolean isOwner(NsUser loginUser) {
        return answers.stream().allMatch(answer -> answer.isOwner(loginUser));
    }

    public void deleteAll() {
        this.answers.forEach(Answer::delete);
    }
}
