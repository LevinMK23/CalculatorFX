package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField textArea;
    public TextField calcArea;
    private Double leftArg, rightArg;
    private String op = null;
    private boolean isLeftArgInputed = false;


    private void appendTextIfCan(String text) {
        if (textArea.getText().length() <= 18) {
            textArea.appendText(text);
        } else {
            calcArea.setText("Very long value");
        }
    }

    public void clickNumber(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        appendTextIfCan(button.getText());
    }

    public void clearText(ActionEvent actionEvent) {
        System.out.println(actionEvent.getEventType());
        calcArea.setText("");
        textArea.setText("");
        isLeftArgInputed = false;
    }

    public void keyListener(KeyEvent keyEvent) {
        System.out.println(keyEvent.getText());
        //keyEvent.getCode().equals(KeyCode.ENTER)
        if (keyEvent.getCode().isDigitKey()) {
            appendTextIfCan(keyEvent.getText());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void paneClick(MouseEvent mouseEvent) {
        System.out.println(mouseEvent);
        calcArea.setText(String.format("(%f, %f)", mouseEvent.getX(), mouseEvent.getY()));
    }

    public void processOperation(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String operation = button.getText();
        if (!isLeftArgInputed) {
            if (!textArea.getText().isEmpty()) {
                leftArg = Double.parseDouble(textArea.getText());
                isLeftArgInputed = true;
                textArea.setText("");
                calcArea.setText(leftArg + " " + operation + " ");
                op = operation;
            }
        }
        switch (operation) {
            case "+":
                calcArea.setText(leftArg + " + ");
                op = "+";
                break;
            case "-":
                calcArea.setText(leftArg + " - ");
                op = "-";
                break;
            case "*":
                calcArea.setText(leftArg + " * ");
                op = "*";
                break;
            case "/":
                calcArea.setText(leftArg + " / ");
                op = "/";
                break;
            case "%":
                calcArea.setText(leftArg + " % ");
                op = "%";
        }
    }

    public void calculate(ActionEvent actionEvent) {
        if (isLeftArgInputed && op != null) {
            if (!textArea.getText().isEmpty()) {
                rightArg = Double.parseDouble(textArea.getText());
                Double result = null;
                switch (op) {
                    case "+":
                        result = leftArg + rightArg;
                        break;
                    case "-":
                        result = leftArg - rightArg;
                        break;
                    case "*":
                        result = leftArg * rightArg;
                        break;
                    case "/":
                        result = leftArg / rightArg;
                        break;
                    case "%":
                        result = leftArg % rightArg;
                }
                calcArea.appendText(rightArg + " = " + result);
                textArea.setText(String.valueOf(result));
                isLeftArgInputed = false;
            }
        } else {
            if (!calcArea.getText().isEmpty()) {
                ScriptEngineManager factory = new ScriptEngineManager();
                ScriptEngine engine = factory.getEngineByName("JavaScript");
                try {
                    Object scriptResult = engine.eval(calcArea.getText());
                    textArea.setText(String.valueOf(scriptResult));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
