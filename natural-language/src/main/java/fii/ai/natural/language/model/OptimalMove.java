package fii.ai.natural.language.model;

import java.util.List;

public class OptimalMove {
    private String movement;
    private List<String> comments;

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
