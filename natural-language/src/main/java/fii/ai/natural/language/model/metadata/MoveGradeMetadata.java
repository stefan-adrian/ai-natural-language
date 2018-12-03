package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class MoveGradeMetadata implements Metadata {

    private static final String KEY = "MoveGrade";

    public MoveGradeMetadata(int moveGrade) {
        this.moveGrade = moveGrade;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * Indicates how good the move is
     * Examples:
     * - 0 for a move that is considered to be unimportant
     * - 1 for a move that is considered to be good
     * - 2 for a move that is considered to be very good
     * -1,-2 the reverse of 1, 2
     */
    private final Integer moveGrade;

    public Integer getMoveGrade() {
        return moveGrade;
    }
}
