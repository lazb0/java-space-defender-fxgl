package spse.level;

public class Level3 extends Level {
    /*
     * 0 - empty
     * 1 - pawn
     * 2 - armorer
     * 3 - shooter
     */
    private static final int[][] enemies = {
            { 0, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 1, 3, 0, 0, 0, 0, 0, 0, 3, 1 },
            { 0, 2, 3, 3, 3, 3, 3, 3, 2, 0 },
            { 0, 0, 2, 2, 2, 2, 2, 2, 0, 0 },
    };

    public Level3() {
        super(enemies);
    }
}
