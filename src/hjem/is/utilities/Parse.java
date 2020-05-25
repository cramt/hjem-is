package hjem.is.utilities;

public class Parse {
    public static Integer integer(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ignored) {

        }
        return null;
    }
}
