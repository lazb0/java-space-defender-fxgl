package spse.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

public class ShooterComponent extends Component {

    private boolean canShoot = false;
    private double lastTimeShot = 0;

    private final int attackSpeed;

    public ShooterComponent(int attackSpeed){
        this.attackSpeed = attackSpeed;
    }

    @Override
    public void onUpdate(double tpf){
        if (!canShoot) {
            if ((FXGL.getGameTimer().getNow() - lastTimeShot) >= 1.0 / attackSpeed) {
                canShoot = true;
            }
        }

        if(!canShoot) return;
        if(Math.random() * 100 < 99) return;
        canShoot = false;

        FXGL.play("enemy_shot.wav");
        FXGL.spawn("Laser", new SpawnData(getEntity().getCenter().getX()-3, getEntity().getY() + 30));
        lastTimeShot = FXGL.getGameTimer().getNow();
    }
}
