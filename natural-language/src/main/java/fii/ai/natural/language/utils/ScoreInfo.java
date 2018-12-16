package fii.ai.natural.language.utils;

/**
 * We are gonna use this class for the score values and other possible values that we do not know for sure yet and
 * are possible to change
 */
public class ScoreInfo {
    /**
     * The value after a side will start playing in scope of equal
     * For example since black wins at -1 if the score is bigger than equalLimit the black side will start to play
     * with a strategy for equal
     */
    private static final Double equalLimit = -0.8;

    /**
     * If a move has a impact on the score bigger than smallImpactMove and smaller than mediumImpactMove then it means
     * that the move had a small impact on the game
     * Anything less then smallImpactMove it means that the move had no significant impact on the game
     */
    private static final Double smallImpactMove = 0.2;

    /**
     * If a move has a impact on the score bigger than mediumImpactMove and smaller than decisiveImpactMove then it means
     * that the move had a medium impact on the game
     */
    private static final Double mediumImpactMove = 0.5;

    /**
     * If you have a move with the score lower than checkMateLimit the other side can check mate you at the next move
     */
    private static final Double checkMateLimit = -0.9;

    private static final Double equality = 0.0;

    public static Double getEqualLimit() {
        return equalLimit;
    }

    public static Double getSmallImpactMove() {
        return smallImpactMove;
    }

    public static Double getMediumImpactMove() {
        return mediumImpactMove;
    }

    public static Double getCheckMateLimit() {
        return checkMateLimit;
    }

    public static Double getEquality() {
        return equality;
    }
}
