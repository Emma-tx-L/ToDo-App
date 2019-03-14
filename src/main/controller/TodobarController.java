package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Task;
import utility.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private final File todoOptionsPopUpFile = new File(todoOptionsPopUpFXML);
    private final File todoActionsPopUpFile = new File(todoActionsPopUpFXML);
    
    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;

    private JFXPopup optionsbarPopUp;
    private JFXPopup actionsbarPopUp;
    
    private Task task;
    
    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTaskOptionsPopUp();
        loadTaskOptionsPopUpActionListener();
        loadTaskActionsPupUp();
        loadTaskActionsPopUpActionListener();
        // TODO: complete this method
    }

    //EFFECTS: load task management options (edit, delete)
    private void loadTaskOptionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoOptionsPopUpFile.toURI().toURL());
            fxmlLoader.setController(new TaskOptionsPopUpController());
            optionsbarPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTaskActionsPupUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoActionsPopUpFile.toURI().toURL());
            fxmlLoader.setController(new TaskActionsPopUpController());
            actionsbarPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTaskOptionsPopUpActionListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                optionsbarPopUp.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15);
            }
        });
    }

    private void loadTaskActionsPopUpActionListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                actionsbarPopUp.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }


    // Inner class: option pop up controller
    class TaskOptionsPopUpController {
        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TaskOptionsPopUpController", "No functionality has been implemented yet!");
                    break;
                case 1:
                    Logger.log("TaskOptionsPopUpController", "No functionality has been implemented yet!");
                    break;
                default:
                    Logger.log("TaskOptionsPopUpController", "No action is implemented for the selected option");
            }
            optionsbarPopUp.hide();
        }
    }

    // Inner class: action pop up controller
    class TaskActionsPopUpController {
        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TaskActionsPopUpController", "No functionality has been implemented yet!");
                    break;
                case 1:
                    Logger.log("TaskActionsPopUpController", "No functionality has been implemented yet!");
                    break;
                case 2:
                    Logger.log("TaskActionsPopUpController", "No functionality has been implemented yet!");
                    break;
                case 3:
                    Logger.log("TaskActionsPopUpController", "No functionality has been implemented yet!");
                    break;
                default:
                    Logger.log("TaskActionsPopUpController", "No action is implemented for the selected option");
            }
            actionsbarPopUp.hide();
        }
    }
}
