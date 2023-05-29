package spse.level;

public class Level1 extends Level{

    /*
    * 0 - empty
    * 1 - pawn
    * 2 - armorer
    * 3 - shooter
    */
    private static final int[][] enemies = {
            { 1, 3, 3, 3, 1, 3, 3, 3, 1 },
            { 1, 1, 3, 1, 1, 1, 3, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    };

    public Level1() {
        super(enemies);
    }
}
