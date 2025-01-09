package eecs1021;

import org.firmata4j.Pin;

import java.io.IOException;

public class BuzzerTask {

    private final Pin buzzerPin;

    public BuzzerTask(Pin buzzerPin) throws IOException {
        this.buzzerPin = buzzerPin;
        buzzerPin.setMode(Pin.Mode.OUTPUT);
    }


    public void beep(int durationMs, int frequencyHz) {
        long endTime = System.currentTimeMillis() + durationMs;
        long delay = 1000 / (2 * frequencyHz);

        while (System.currentTimeMillis() < endTime) {
            try {
                buzzerPin.setValue(1);
                Thread.sleep(delay);
                buzzerPin.setValue(0);
                Thread.sleep(delay);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOn() {
        beep(5000, 1000);
    }

    public void turnOff() {
        try {
            buzzerPin.setValue(0); // The buzzer is turned  off
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}