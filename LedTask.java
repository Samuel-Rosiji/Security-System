package eecs1021;

import org.firmata4j.Pin;
import java.io.IOException;

public class LedTask {

    private final Pin ledPin;

    public LedTask(Pin ledPin) {
        this.ledPin = ledPin;
    }

    public void turnOn() throws IOException {
        ledPin.setValue(1); // LED on
    }

    public void turnOff() throws IOException {
        ledPin.setValue(0); // LED off
    }

    public void flicker(int times, int delayMs) throws IOException, InterruptedException {
        for (int i = 0; i < times; i++) {
            turnOn();
            Thread.sleep(delayMs);
            turnOff();
            Thread.sleep(delayMs);
        }
    }
}