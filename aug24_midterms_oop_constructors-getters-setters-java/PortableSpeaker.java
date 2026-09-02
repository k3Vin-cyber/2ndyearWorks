public class PortableSpeaker {
    // Encapsulated fields representing real-life attributes
    private String modelName;
    private int currentVolume;
    private String equalizerMode;

    // Constructor: This is like unboxing and turning on the speaker for the first time
    public PortableSpeaker(String modelName, int currentVolume, String equalizerMode) {
        this.modelName = modelName;
        this.currentVolume = currentVolume;
        this.equalizerMode = equalizerMode;
    }

    // Getter for modelName
    public String getModelName() {
        return modelName;
    }

    // Setter for modelName
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    // Getter for currentVolume (Checking the current volume level)
    public int getCurrentVolume() {
        return currentVolume;
    }

    // Setter for currentVolume (Pressing the volume up/down buttons)
    public void setCurrentVolume(int currentVolume) {
        // Adding real-world logic so volume doesn't exceed limits
        if (currentVolume >= 0 && currentVolume <= 100) {
            this.currentVolume = currentVolume;
        } else {
            System.out.println("Error: Volume must be between 0 and 100.");
        }
    }

    // Getter for equalizerMode
    public String getEqualizerMode() {
        return equalizerMode;
    }

    // Setter for equalizerMode (Changing the sound profile in an app)
    public void setEqualizerMode(String equalizerMode) {
        this.equalizerMode = equalizerMode;
    }

    // Main method to test the IRL example
    public static void main(String[] args) {
        // 1. Using the Constructor to set up our device
        PortableSpeaker mySpeaker = new PortableSpeaker("Soundcore Select 4 Go", 40, "Default Profile");

        System.out.println("Device Connected: " + mySpeaker.getModelName());
        System.out.println("Initial Volume: " + mySpeaker.getCurrentVolume() + "%");
        System.out.println("Initial EQ: " + mySpeaker.getEqualizerMode());

        System.out.println("\n--- Adjusting Settings for Outdoors ---");

        // 2. Using Setters to change the device's state
        mySpeaker.setCurrentVolume(85);
        mySpeaker.setEqualizerMode("Custom 9-Band Outdoor");

        // 3. Using Getters to verify the new state
        System.out.println("Updated Volume: " + mySpeaker.getCurrentVolume() + "%");
        System.out.println("Updated EQ: " + mySpeaker.getEqualizerMode());
    }
}