package spse.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import spse.Config;

public class CustomMenu extends FXGLMenu {
    public CustomMenu(MenuType type) {
        super(type);

        getContentRoot().getChildren().setAll(new Rectangle(Config.APP_WIDTH, Config.APP_HEIGHT));

        Text title = FXGL.getUIFactoryService().newText("Space Defender", Color.WHITE, 52);
        title.setStrokeWidth(1.75);

        FXGL.centerTextBind(title, getAppWidth() / 2.0, 200);

        getContentRoot().getChildren().add(1, title);

        showMenu();
    }

    private void showMenu(){
        VBox menuBox;
        if(super.getType() == MenuType.GAME_MENU){
            menuBox = new VBox(
                    25,
                    new MenuButton("Resume", this::fireResume),
                    new MenuButton("Restart" , this::fireNewGame),
                    new MenuButton("Main Menu", this::fireExitToMainMenu),
                    new MenuButton("Exit", this::fireExit)
            );
        } else {
            menuBox = new VBox(
                    25,
                    new MenuButton("New Game" , this::fireNewGame),
                    new MenuButton("Exit", this::fireExit)
            );
        }


        menuBox.setAlignment(Pos.CENTER);

        menuBox.setTranslateX(Config.APP_WIDTH / 2.0 - 90);
        menuBox.setTranslateY(Config.APP_HEIGHT / 2.0);

        getContentRoot().getChildren().add(2, menuBox);
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            Text text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 40);
            text.setStrokeWidth(1.25);
            text.strokeProperty().bind(text.fillProperty());

            text.scaleXProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(1.15)
                            .otherwise(1)
            );

            text.scaleYProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(1.15)
                            .otherwise(1)
            );

            setOnMouseClicked(e -> action.run());

            setPickOnBounds(true);

            getChildren().add(text);
        }
    }
}
