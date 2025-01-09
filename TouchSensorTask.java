package eecs1021;

import org.firmata4j.Pin;
import java.io.IOException;
import java.util.TimerTask;

public class TouchSensorTask extends TimerTask {

    private final Pin touchPin;
    private final Pin overrideButtonPin;  // Button pin for override
    private final OledDisplayTask oledDisplayTask;
    private final LedTask ledTask;
    private final BuzzerTask buzzerTask;
    private final int[] passcode = {1, 2, 3, 4}; // Password is {1, 2, 3, 4}
    private int[] inputPin = new int[4]; // Array to store user input
    private int inputIndex = 0; //
    private int attempts = 0;

    public TouchSensorTask(Pin touchPin, Pin overrideButtonPin, OledDisplayTask oledDisplayTask, LedTask ledTask, BuzzerTask buzzerTask) {
        this.touchPin = touchPin;
        this.overrideButtonPin = overrideButtonPin;  // Initialize the override button
        this.oledDisplayTask = oledDisplayTask;
        this.ledTask = ledTask;
        this.buzzerTask = buzzerTask;
    }

    @Override
    public void run() {
        long sensorValue = touchPin.getValue();
        long buttonValue = overrideButtonPin.getValue();

        // Handle the override button press
        if (buttonValue == 1) {
            oledDisplayTask.displayMessage("Override! Access granted");
            System.out.println("Override! Access granted");
            attempts = 0;
            return;
        }

        // Handle the touch sensor input
        if (sensorValue > 0) {
            int digit = getDigitFromSensor(sensorValue);
            inputPin[inputIndex] = digit;
            System.out.println("Input digit: " + digit);  //Print
            inputIndex++;

            if (inputIndex == 4) {
                checkPin();
                inputIndex = 0;
            }
        }
    }


    private int getDigitFromSensor(long sensorValue) {

        if (sensorValue == 100) {
            return 1;
        } else if (sensorValue == 200) {
            return 2;
        } else if (sensorValue == 300) {
            return 3;
        } else if (sensorValue == 400) {
            return 4;
        } else {
            return (int) sensorValue;
        }

    }

    private void checkPin() {
        boolean isCorrect = true;
        for (int i = 0; i < 4; i++) {
            if (inputPin[i] != passcode[i]) {
                isCorrect = false;
                break;
            }
        }

        if (isCorrect) {
            oledDisplayTask.displayMessage("Input correct! Welcome General");
            System.out.println("Input correct! Welcome General");
            attempts = 0;
        } else {
            attempts++;
            if (attempts == 1) {
                oledDisplayTask.displayMessage("Wrong 2 more attempts");
                System.out.println("Wrong 2 more attempts");
            } else if (attempts == 2) {
                oledDisplayTask.displayMessage("One last try");
                System.out.println("One last try");
            } else if (attempts == 3) {
                oledDisplayTask.displayMessage("Failed");
                System.out.println("Failed");
                triggerAlert();
            }
        }
    }

    private void triggerAlert() {
        try {
            ledTask.turnOn();
            buzzerTask.turnOn();
            Thread.sleep(5000);
            ledTask.turnOff();
            buzzerTask.turnOff();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}