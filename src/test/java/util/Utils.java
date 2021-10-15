package util;

public final class Utils {
    private final static int MIN_ID = 1000;
    private final static int MAX_ID = 9999;

    private Utils() {
    }

    public static int generateRandomFourDigitNumber() {
        return (int) ((Math.random() * (MAX_ID - MIN_ID)) + MIN_ID);
    }
}
