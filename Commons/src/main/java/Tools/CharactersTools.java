package Tools;

public class CharactersTools {
    public static boolean isLetterOrDigit(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c >= '0' && c <= '9');
    }

    public static boolean isStringContainingOnlyLetterOrDigit(String string) {
        for (Character c : string.toCharArray())
            if (!isLetterOrDigit(c))
                return false;
        return true;
    }

    public static boolean isValidPassword(String pass){
        return pass.length() < 8 && isStringContainingOnlyLetterOrDigit(pass);
    }

}
