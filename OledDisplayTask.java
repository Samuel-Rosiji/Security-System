package eecs1021;

import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;

public class OledDisplayTask {

    private final SSD1306 oledDisplay;

    public OledDisplayTask(SSD1306 oledDisplay) {
        this.oledDisplay = oledDisplay;
    }

    public void displayMessage(String message) {
        // Print the message to the console
        System.out.println(message);


        try {
            MonochromeCanvas canvas = oledDisplay.getCanvas();
            canvas.clear();
            canvas.drawString(0, 0, message); // Draw message
            oledDisplay.display(); // Update the display
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}