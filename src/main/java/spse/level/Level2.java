package spse.level;

public class Level2 extends Level{
    /*
     * 0 - empty
     * 1 - pawn
     * 2 - armorer
     * 3 - shooter
     */
    private static final int[][] enemies = {
            { 0, 1, 1, 1, 1, 1, 1, 1, 0 },
            { 1, 3, 0, 3, 0, 3, 0, 3, 1 },
            { 1, 0, 3, 0, 3, 0, 3, 0, 1 },
            { 1, 2, 2, 2, 2, 2, 2, 2, 1 },
    };

    public Level2() {
        super(enemies);
    }
}
