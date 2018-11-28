package fii.ai.natural.language.model;

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
     * Is null in case no piece was taken or the name of the piece in case a piece was taken in this move.
     */
    private String pieceTaken;

    /**
     * Indicates on what side the castling can be made(if can be made in any side)
     * kq for when the castling can be made on both queen and king side
     * k for king side
     * q fot queen side
     * null for when castling can't be made in any side
     */
    private String castlingState;

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

    /**
     * The complete name of the chess piece Ex: King, Queen
     */
    private String chessPieceName;

    /**
     * The color of the piece that is moved ( White or Black )
     */
    private String color;

    /**
     * A list of comments for the move, the list can have one comment or more, comments should be ordered by a criteria
     * Ex: First comment for a move can be something like: White moves King from d1 to c1.
     *     The second comment can be: The move gave a big advantage to the White side.
     */
    private List<String> comments;

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

    public String getCastlingState() {
        return castlingState;
    }

    public void setCastlingState(String castlingState) {
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

    public String getChessPieceName() {
        return chessPieceName;
    }

    public void setChessPieceName(String chessPieceName) {
        this.chessPieceName = chessPieceName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
