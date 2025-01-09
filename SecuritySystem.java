package eecs1021;

import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;

import java.io.IOException;
import java.util.Timer;

public class SecuritySystem {


    static final int D4 = 4;  // LED
    static final int D5 = 5;  // Buzzer
    static final int D12 = 12;  // Touch Sensor Pin 1

    static final int D6 = 6; // For button
    static final byte I2C0 = 0x3C; // OLED Display address

    public static void main(String[] args) throws InterruptedException, IOException {
        // Initialize Firmata device
        FirmataDevice device = new FirmataDevice("/dev/cu.usbserial-0001");
        device.start();
        device.ensureInitializationIsDone();

        // Initialize pins
        Pin ledPin = device.getPin(D4);
        ledPin.setMode(Pin.Mode.OUTPUT);

        Pin buzzerPin = device.getPin(D5);
        buzzerPin.setMode(Pin.Mode.OUTPUT);

        Pin touchPin = device.getPin(D12);
        touchPin.setMode(Pin.Mode.INPUT);

        Pin overrideButtonPin = device.getPin(D6);
        overrideButtonPin.setMode(Pin.Mode.INPUT);

        // Set up the OLED display
        SSD1306 oledDisplay = new SSD1306(device.getI2CDevice(I2C0), SSD1306.Size.SSD1306_128_64);
        oledDisplay.init();

        // Create tasks
        OledDisplayTask oledDisplayTask = new OledDisplayTask(oledDisplay);
        LedTask ledTask = new LedTask(ledPin);
        BuzzerTask buzzerTask = new BuzzerTask(buzzerPin);
        TouchSensorTask touchSensorTask = new TouchSensorTask(touchPin, overrideButtonPin, oledDisplayTask, ledTask, buzzerTask);

        // Create Timer and schedule tasks
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(touchSensorTask, 0, 100);
    }

}