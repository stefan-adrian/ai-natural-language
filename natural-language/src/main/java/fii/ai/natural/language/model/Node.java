package fii.ai.natural.language.model;

import fii.ai.natural.language.input.InputVariant;

import java.util.List;

public class Node {

    /**
     * Move description string. A simple encoding would be:
     * - w if is white, b if is black to move
     * - start field, algebraic notation ex: 'e2'
     * - end field, algebraic notation ex: 'e4'
     * - in case the pawn is at the end of the table the chess piece to be switch with qrbn
     *
     * Examples:
     *  we2e4
     *  be7e5
     *  wg1f3
     *  wa7a8q
     *
     */
    private String move;

    /**
     * Position score: 0 means equilibrium, negative means black has advantage, positive means
     * white has advantage
     */
    private double score;

    /**
     * Is null in case no pice was taken or the name of the piece in case a piece was taken in this move.
     */
    private String pieceTaken;

    /**
     * Indicates if the castling can be made.
     */
    private Boolean castlingState;

    /**
     * Indicates how good the move is
     * Examples:
     * - 0 for a move that is considered to be unimportant
     * - 1 for a move that is considered to be a small improvment
     * - 2 for a move that is considered to be very good
     * - 3 for a move that is considered to be game changing
     * -1,-2,-3 the reverse of 1, 2, 3
     */
    private Integer moveGrade;

    /**
     * Variants to the current move
     */
    private List<MoveVariant> variants;

    private String comment;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getPieceTaken() {
        return pieceTaken;
    }

    public void setPieceTaken(String pieceTaken) {
        this.pieceTaken = pieceTaken;
    }

    public Boolean getCastlingState() {
        return castlingState;
    }

    public void setCastlingState(Boolean castlingState) {
        this.castlingState = castlingState;
    }

    public Integer getMoveGrade() {
        return moveGrade;
    }

    public void setMoveGrade(Integer moveGrade) {
        this.moveGrade = moveGrade;
    }

    public List<MoveVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<MoveVariant> variants) {
        this.variants = variants;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
