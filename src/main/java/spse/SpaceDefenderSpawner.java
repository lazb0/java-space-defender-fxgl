package spse;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import spse.component.*;

public class SpaceDefenderSpawner implements EntityFactory {

    @Spawns("Background")
    public Entity spawnBackground(SpawnData data){
        return FXGL.entityBuilder()
                .at(-20,-20)
                .view(new Rectangle(Config.APP_WIDTH+40, Config.APP_HEIGHT+40, Config.BG_COLOR))
                .zIndex(-500)
                .build();

    }

    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data){
        return FXGL.entityBuilder()
                .type(SpaceDefenderTypes.PLAYER)
                .at((double) Config.APP_WIDTH /2-14, Config.APP_HEIGHT-50)
                .viewWithBBox("player.png")
                .with(new PlayerComponent())
                .collidable()
                .build();
    }

    @Spawns("Bullet")
    public Entity spawnBullet(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(SpaceDefenderTypes.BULLET)
                .viewWithBBox("bullet.png")
                .with(new BulletComponent())
                .collidable()
                .build();
    }

    @Spawns("Laser")
    public Entity spawnLaser(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(SpaceDefenderTypes.LASER)
                .viewWithBBox("laser.png")
                .with(new LaserComponent())
                .collidable()
                .build();
    }

    @Spawns("Pawn")
    public Entity spawnPawn(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(SpaceDefenderTypes.ENEMY)
                .viewWithBBox(FXGL.texture("pawn.png")
                        .toAnimatedTexture(2, Duration.seconds(2))
                        .loop())
                .collidable()
                .with(new EnemyComponent(Config.PAWN_HP, 1))
                .build();
    }

    @Spawns("Armorer")
    public Entity spawnArmorer(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(SpaceDefenderTypes.ENEMY)
                .viewWithBBox(FXGL.texture("armorer.png")
                        .toAnimatedTexture(2, Duration.seconds(2))
                        .loop())
                .collidable()
                .with(new EnemyComponent(Config.ARMORER_HP, 2))
                .build();
    }

    @Spawns("Shooter")
    public Entity spawnShooter(SpawnData data){
        return FXGL.entityBuilder(data)
                .type(SpaceDefenderTypes.ENEMY)
                .viewWithBBox(FXGL.texture("shooter.png")
                        .toAnimatedTexture(2, Duration.seconds(2))
                        .loop())
                .collidable()
                .with(new EnemyComponent(Config.SHOOTER_HP, 3), new ShooterComponent(Config.SHOOTER_ATTACK_SPEED))
                .build();
    }
}
