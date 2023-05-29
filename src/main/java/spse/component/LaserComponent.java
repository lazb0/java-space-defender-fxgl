package spse.component;

import com.almasb.fxgl.entity.component.Component;
import spse.Config;

public class LaserComponent extends Component {
    @Override
    public void onUpdate(double tpf){
        getEntity().translateY(Config.SHOOTER_BULLET_SPEED * tpf);
    }
}
