package spse.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import spse.Config;

public class EnemyComponent extends Component {
    private int hp;
    private final int type;
    public EnemyComponent(int hp, int type){
        this.hp = hp;
        this.type = type;
    }

    public void hit(){
        if(hp == 1){
            FXGL.getGameScene().getViewport().shakeTranslational(1);
            FXGL.play("enemy_death.wav");
            getEntity().removeFromWorld();
        } else {
            FXGL.getGameScene().getViewport().shakeTranslational(0.5);
            FXGL.play("enemy_hit.wav");
            hp--;
        }
    }
    private boolean goingRight = true;
    public void move(){
        if((getEntity().getX()+70 >= Config.APP_WIDTH-20 && goingRight)  || (getEntity().getX()-70 <= 0 && !goingRight)){
            if(getEntity().getY() + 320 >= Config.APP_HEIGHT){
                FXGL.getWorldProperties().increment("lives", -1);
                getEntity().removeFromWorld();
            }
            moveDown();
            goingRight = !goingRight;
        } else if (goingRight) {
            moveRight();
        } else {
            moveLeft();
        }
    }

    public int getType(){
        return type;
    }

    public int getHp(){return hp;}

    private void moveRight(){
        getEntity().translateX(70);
    }

    private void moveLeft(){
        getEntity().translateX(-70);
    }

    private void moveDown(){
        getEntity().translateY(280);
    }
}
