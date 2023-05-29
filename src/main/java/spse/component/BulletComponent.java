package spse.component;

import com.almasb.fxgl.entity.component.Component;
import spse.Config;

public class BulletComponent extends Component {
    @Override
    public void onUpdate(double tpf){
        getEntity().translateY(-Config.PLAYER_BULLET_SPEED * tpf);
    }
}
