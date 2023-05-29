package spse;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import spse.component.EnemyComponent;
import spse.component.PlayerComponent;
import spse.level.Level;
import spse.level.Level1;
import spse.level.Level2;
import spse.level.Level3;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpaceDefender extends GameApplication {


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Space Defender");
        settings.setVersion("0.2");
        settings.setApplicationMode(ApplicationMode.RELEASE);

        settings.setWidth(Config.APP_WIDTH);
        settings.setHeight(Config.APP_HEIGHT);
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);

        settings.setSceneFactory(new SpaceDefenderSceneFactory());
        settings.setMainMenuEnabled(true);
        settings.setFontGame("8bit.ttf");
        settings.setFontText("8bit.ttf");
        settings.setFontUI("8bit.ttf");
    }

    @Override
    protected void initInput(){
        Input input = FXGL.getInput();


        input.addAction(new UserAction("Move Left") {
            @Override
            protected void onAction(){
                playerComponent.moveLeft();
            }
        }, KeyCode.LEFT);

        input.addAction(new UserAction("Move Right") {
            @Override
            protected void onAction(){
                playerComponent.moveRight();
            }
        }, KeyCode.RIGHT);

        input.addAction(new UserAction("Shoot") {
            @Override
            protected void onActionBegin(){
                playerComponent.shoot();
            }
        }, KeyCode.SPACE);
    }

    private Music mainTheme;
    private Music currentTrack;
    @Override
    protected void onPreInit(){
        mainTheme = FXGL.getAssetLoader().loadMusic("main_theme.wav");
        FXGL.getAudioPlayer().loopMusic(mainTheme);

        FXGL.getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(@NotNull DataFile data) {
                Bundle bundle = new Bundle("gameData");

                bundle.put("highscore", FXGL.geti("score"));
                bundle.put("playerName", FXGL.gets("playerName"));

                data.putBundle(bundle);
            }

            @Override
            public void onLoad(@NotNull DataFile data) {
                Bundle bundle = data.getBundle("gameData");

                FXGL.set("highscore" , bundle.get("highscore"));
                FXGL.set("playerName" , bundle.get("playerName"));
            }
        });
    }

    private PlayerComponent playerComponent;
    private List<Level> levels;
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("level", 0);
        vars.put("lives", Config.START_LIVES);
        vars.put("highscore", 0);
        vars.put("playerName", "");
        vars.put("time", 0);
        vars.put("timeUI", "0:00");
    }

    @Override
    protected void initPhysics(){
        FXGL.onCollisionBegin(SpaceDefenderTypes.BULLET, SpaceDefenderTypes.ENEMY, (bullet, enemy) -> {
            EnemyComponent enemyComponent = enemy.getComponent(EnemyComponent.class);

            if(enemyComponent.getHp() == 1) increaseScore(enemyComponent.getType());

            bullet.removeFromWorld();
            enemyComponent.hit();
        });

        FXGL.onCollisionBegin(SpaceDefenderTypes.LASER, SpaceDefenderTypes.PLAYER, (laser, player) -> {
            laser.removeFromWorld();
            playerComponent.hit();
            if(FXGL.getWorldProperties().getInt("lives") == 0){
                showGameOver();
            }
        });
    }

    @Override
    protected void initGame(){
        FXGL.getSaveLoadService().readAndLoadTask("highscore.sav").run();

        FXGL.getGameWorld().addEntityFactory(new SpaceDefenderSpawner());

        levels = Arrays.asList(
                new Level1(),
                new Level2(),
                new Level3()
        );

        loadNextLevel();

        FXGL.run(this::increaseTime, Duration.seconds(1));

        FXGL.spawn("Background");

        FXGL.run(this::debrisCleanup, Duration.seconds(1));

        playerComponent = FXGL.spawn("Player").getComponent(PlayerComponent.class);
    }

    @Override
    protected void initUI(){
        Rectangle ribbon = new Rectangle(Config.APP_WIDTH+40, 50, Color.BLACK);

        FXGL.getGameScene().addUINode(ribbon);

        Text[] texts = {
                FXGL.getUIFactoryService().newText("", Color.WHITE,  26),
                FXGL.getUIFactoryService().newText("", Color.WHITE, 26),
                FXGL.getUIFactoryService().newText("", Color.WHITE, 26),
                FXGL.getUIFactoryService().newText("SCORE", Color.WHITE, 26),
                FXGL.getUIFactoryService().newText("LIVES", Color.WHITE, 26)
        };

        texts[0].textProperty().bind(FXGL.getip("score").asString());
        texts[1].textProperty().bind(FXGL.getip("lives").asString());
        texts[2].textProperty().bind(FXGL.getsp("timeUI"));

        texts[0].setTranslateX(105);
        texts[1].setTranslateX(Config.APP_WIDTH-110);
        texts[2].setTranslateX(Config.APP_WIDTH /2.0-20);
        texts[3].setTranslateX(30);
        texts[4].setTranslateX(Config.APP_WIDTH-80);

        for (Text text : texts) {
            text.setTranslateY(30);
            FXGL.getGameScene().addUINode(text);
        }
    }

    @Override
    protected void onUpdate(double tpf){
        checkForEnemies();
        moveEnemies();
    }

    private void loadNextLevel(){
        FXGL.getAudioPlayer().stopMusic(mainTheme);
        if(currentTrack != null) FXGL.getAudioPlayer().stopMusic(currentTrack);
        FXGL.getInput().setProcessInput(false);

        FXGL.inc("level", 1);
        if(levels.size() < FXGL.geti("level")){
            showVictory();
        } else {
            resetTime();
            initLevel();
        }
    }

    private void initLevel() {
        getCurrentLevel().init();

        currentTrack = FXGL.getAssetLoader().loadMusic("track1.wav");

        FXGL.getAudioPlayer().loopMusic(currentTrack);
        FXGL.getInput().setProcessInput(true);
    }


    public Level getCurrentLevel(){
        return levels.get(FXGL.geti("level") - 1);
    }


    private void checkForEnemies(){
        if(FXGL.getGameWorld().getEntitiesByType(SpaceDefenderTypes.ENEMY).isEmpty()) loadNextLevel();
    }

    private void increaseScore(int enemyType){
        int score = 0;

        switch (enemyType) {
            case 1 -> score = Config.SCORE_FOR_PAWN;
            case 2 -> score = Config.SCORE_FOR_ARMORER;
            case 3 -> score = Config.SCORE_FOR_SHOOTER;
        }

        if(FXGL.geti("time")/10 != 0){
            score = score / (FXGL.geti("time")/10);
        }

        FXGL.inc("score", score);
    }

    private double lastEnemyMovement;
    private void moveEnemies(){
        if ((FXGL.getGameTimer().getNow() - lastEnemyMovement) >= 1.0 / Config.ENEMY_MOVE_SPEED) {
            lastEnemyMovement = FXGL.getGameTimer().getNow();
            FXGL.getGameWorld().getEntitiesByType(
                    SpaceDefenderTypes.ENEMY
            ).forEach(enemy -> {
                if(FXGL.getWorldProperties().getInt("lives") == 0) showGameOver();
                enemy.getComponent(EnemyComponent.class).move();
            });
        }

    }

    private void increaseTime(){
        FXGL.inc("time", 1);
        int time = FXGL.geti("time");

        int minutes = time/60;
        if(time%60 >= 10){
            FXGL.set("timeUI", minutes+":"+time%60);
        } else {
            FXGL.set("timeUI", minutes+":0"+time%60);
        }

    }

    private void resetTime(){
        FXGL.set("timeUI", "0:00");
        FXGL.set("time", 0);
    }

    private void debrisCleanup() {
        FXGL.getGameWorld().getEntitiesByType(
                SpaceDefenderTypes.BULLET,
                SpaceDefenderTypes.LASER
        ).forEach(debris -> {
            if (debris.getY() - 40 >= Config.APP_HEIGHT || debris.getY() + 40 <= 0) debris.removeFromWorld();
        });
    }

    private void showGameOver() {
        FXGL.getAudioPlayer().stopMusic(currentTrack);
        FXGL.getAudioPlayer().playMusic(mainTheme);
        FXGL.getDialogService().showConfirmationBox("Game Over. Play Again?", yes -> {
            if (yes) {
                FXGL.getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                FXGL.getGameController().startNewGame();
            } else {
                FXGL.getGameController().gotoMainMenu();
            }
        });

        if(FXGL.geti("highscore") < FXGL.geti("score")) showHighscoreDialog();
    }

    private void showVictory() {
        FXGL.getAudioPlayer().stopMusic(currentTrack);
        FXGL.getAudioPlayer().playMusic(mainTheme);
        if(FXGL.geti("highscore") < FXGL.geti("score")) showHighscoreDialog();

        FXGL.getDialogService().showConfirmationBox("You have defended the space from the aliens.\n Do you wanna play again?", yes -> {
            if (yes) {
                FXGL.getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                FXGL.getGameController().startNewGame();
            } else {
                FXGL.getGameController().gotoMainMenu();
            }
        });
        if(FXGL.geti("highscore") < FXGL.geti("score")) showHighscoreDialog();
    }

    private void showHighscoreDialog(){
        FXGL.getDialogService().showInputBox("You hit HIGHSCORE! Please enter your name", playerName -> {
            FXGL.set("playerName", playerName);
            FXGL.getSaveLoadService().saveAndWriteTask("highscore.sav").run();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
