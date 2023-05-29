package spse.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import spse.Config;

public class PlayerComponent extends Component {

    private double deltaX = 0;

    private boolean canShoot = true;
    private double lastTimeShot = 0;

    private static final int moveSpeed = Config.PLAYER_MOVE_SPEED;
    private static final int attackSpeed = Config.PLAYER_ATTACK_SPEED;

    @Override
    public void onUpdate(double tpf){
        deltaX = moveSpeed * tpf;


        if (!canShoot) {
            if ((FXGL.getGameTimer().getNow() - lastTimeShot) >= 1.0 / attackSpeed) {
                canShoot = true;
            }
        }
    }

    public void moveLeft() {
        if(getEntity().getX() - deltaX >= 0)
            entity.translateX(-deltaX);
    }

    public void moveRight() {
        if(getEntity().getX() + getEntity().getWidth() + deltaX <= Config.APP_WIDTH)
            entity.translateX(deltaX);
    }

    public void shoot() {
        if(!canShoot) return;
        canShoot = false;

        FXGL.play("player_shot.wav");
        FXGL.spawn("Bullet", new SpawnData(getEntity().getCenter().getX() - 3, getEntity().getY() - 30));
        lastTimeShot = FXGL.getGameTimer().getNow();
    }

    public void hit(){
        if(FXGL.getWorldProperties().getInt("lives") == 0){
            FXGL.play("player_death.wav");
            FXGL.getGameScene().getViewport().shakeTranslational(20);

            getEntity().setVisible(false);
            return;
        }

        FXGL.play("player_hit.wav");
        FXGL.getGameScene().getViewport().shakeTranslational(10);

        FXGL.getWorldProperties().increment("lives", -1);
    }
}
