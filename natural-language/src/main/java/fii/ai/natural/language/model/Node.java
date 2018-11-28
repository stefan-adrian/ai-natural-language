package fii.ai.natural.language.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

    /**
     * Move description string. A simple encoding would be:
     * - w if is white, b if is black to move
     * - start field, algebraic notation ex: 'e2'
     * - end field, algebraic notation ex: 'e4'
     * - in case the pawn is at the end of the table the chess piece to be switch with qrbn
     * <p>
     * Examples:
     * we2e4
     * be7e5
     * wg1f3
     * wa7a8q
     */
    private String move;

    /**
     * Position score: 0 means equilibrium, negative means black has advantage, positive means
     * white has advantage
     */
    private double score;


    /**
     * Variants to the current move
     */
    private List<MoveVariant> variants;


    /**
     * List of meta datas assigned to this move
     */
    private List<Metadata> metadata = new ArrayList<>();

    /**
     * A list of comments for the move, the list can have one comment or more, comments should be ordered by a criteria
     * Ex: First comment for a move can be something like: White moves King from d1 to c1.
     * The second comment can be: The move gave a big advantage to the White side.
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

    public List<MoveVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<MoveVariant> variants) {
        this.variants = variants;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
