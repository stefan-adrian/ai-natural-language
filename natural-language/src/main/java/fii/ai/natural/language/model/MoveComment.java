package fii.ai.natural.language.model;

import java.util.List;

public class MoveComment {
    Integer moveIndex;
    List<String> comments;

    public MoveComment(Integer moveIndex, List<String> comments) {
        this.moveIndex = moveIndex;
        this.comments = comments;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public Integer getMoveIndex() {
        return moveIndex;

    }

    public void setMoveIndex(Integer moveIndex) {
        this.moveIndex = moveIndex;
    }
}
