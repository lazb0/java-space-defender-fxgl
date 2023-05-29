package spse.level;

import com.almasb.fxgl.dsl.FXGL;

public abstract class Level {
    protected final int[][] enemies;

    public Level(int[][] enemies){
        this.enemies = enemies;
    }

    public void init(){
        for(int row = 0; row < enemies.length; row++){
            for(int column = 0; column < enemies[row].length; column++){
                int enemyType = enemies[row][column];
                if(enemyType <= 0 || enemyType > 3) continue;

                int x = column * 70 + 20;
                int y = row * 70 + 70;

                switch (enemies[row][column]){
                    case 1 ->
                        FXGL.spawn("Pawn", x, y);

                    case 2 ->
                        FXGL.spawn("Armorer", x, y);

                    case 3 ->
                        FXGL.spawn("Shooter", x, y);
                }
            }
        }
    }
}
