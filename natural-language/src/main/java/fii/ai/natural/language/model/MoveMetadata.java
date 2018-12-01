package fii.ai.natural.language.model;

import java.util.List;

public class MoveMetadata {

    private String castlingState;

    private Integer moveGrade;

    private String color;

    private String name;

    private String pieceTaken;

    private Boolean enPassantPossible=false;

    private List<String> checkPieces;

    private String state;



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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPieceTaken() {
        return pieceTaken;
    }

    public void setPieceTaken(String pieceTaken) {
        this.pieceTaken = pieceTaken;
    }

    public Boolean getEnPassantPossible() {
        return enPassantPossible;
    }

    public void setEnPassantPossible(Boolean enPassantPossible) {
        this.enPassantPossible = enPassantPossible;
    }

    public List<String> getCheckPieces() {
        return checkPieces;
    }

    public void setCheckPieces(List<String> checkPieces) {
        this.checkPieces = checkPieces;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
