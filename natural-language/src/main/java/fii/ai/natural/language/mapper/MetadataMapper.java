package fii.ai.natural.language.mapper;

import fii.ai.natural.language.model.Metadata;
import fii.ai.natural.language.model.MoveMetadata;
import fii.ai.natural.language.model.metadata.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataMapper {

    public MoveMetadata map(List<Metadata> metadatas) {
        MoveMetadata moveMetadata = new MoveMetadata();
        for (Metadata metadata : metadatas) {
            switch (metadata.getKey()) {
                case "CastlingState": {
                    CastlingStateMetadata castlingStateMetadata = (CastlingStateMetadata) metadata;
                    moveMetadata.setCastlingState(castlingStateMetadata.getCastlingState());
                    break;
                }
                case "MoveGrade": {
                    MoveGradeMetadata moveGradeMetadata = (MoveGradeMetadata) metadata;
                    moveMetadata.setMoveGrade(moveGradeMetadata.getMoveGrade());
                    break;
                }
                case "PieceColor": {
                    PieceColorMetadata pieceColorMetadata = (PieceColorMetadata) metadata;
                    moveMetadata.setColor(pieceColorMetadata.getColor());
                    break;
                }
                case "PieceName": {
                    PieceNameMetadata pieceNameMetadata = (PieceNameMetadata) metadata;
                    moveMetadata.setName(pieceNameMetadata.getChessPieceName());
                    break;
                }
                case "PieceTaken": {
                    PieceTakenMetadata pieceTakenMetadata = (PieceTakenMetadata) metadata;
                    moveMetadata.setPieceTaken(pieceTakenMetadata.getPieceTaken());
                    break;
                }
                case "EnPassant": {
                    EnPassantMetadata enPassantMetadata = (EnPassantMetadata) metadata;
                    moveMetadata.setEnPassantPossible(enPassantMetadata.getEnPassantPossible());
                    break;
                }
                case "GameState": {
                    GameStateMetadata gameStateMetadata = (GameStateMetadata) metadata;
                    moveMetadata.setState(gameStateMetadata.getState());
                    break;
                }
                case "Check": {
                    CheckMetadata checkMetadata = (CheckMetadata) metadata;
                    moveMetadata.setCheckPieces(checkMetadata.getCheckPieces());
                    break;
                }
                case "Promotion": {
                    PromotionMetadata promotionMetadata = (PromotionMetadata) metadata;
                    moveMetadata.setPromoteToPiece(promotionMetadata.getPromotedToPiece());
                    break;
                }
                case "EqualScope": {
                    EqualScopeMetadata equalScopeMetadata = (EqualScopeMetadata) metadata;
                    moveMetadata.setEqualScope(equalScopeMetadata.getPlayingForEqual());
                    break;
                }
                case "PreCheckMate": {
                    PreCheckMateMetadata preCheckMateMetadata = (PreCheckMateMetadata) metadata;
                    moveMetadata.setPreCheckMate(preCheckMateMetadata.getPreCheckMate());
                    break;
                }
            }
        }
        return moveMetadata;
    }
}
