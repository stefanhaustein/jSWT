package java.text;

public class MessageFormat {

    public static String format(String pattern, Object... args) {
        for (int i = 0; i < args.length; i++) {
            pattern = pattern.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return pattern;
    }

}
