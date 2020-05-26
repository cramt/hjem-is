package hjem.is.utilities;

public class Parse {
    public static Integer integer32(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ignored) {

        }
        return null;
    }

    public static Double integer64(String str){
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ignored) {

        }
        return null;
    }
}
