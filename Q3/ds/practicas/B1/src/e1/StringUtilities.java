package e1;

import java.util.Random;

public class StringUtilities {
    /**
     * Checks if a given string is a valid c of two others . That is , it contains
     * all characters of the other two and order of all characters in the individual
     * strings is preserved .
     * @param a First String to be mixed
     * @param b Second String to be mixed
     * @param c Mix of the two other Strings
     * @return true if the c is valid , false otherwise
     */
    public static boolean isValidMix ( String a , String b , String c ) {
        int auxA = -1;
        int auxB = -1;
        if (c.length() != a.length() + b.length()) {
            return false;
        }
        for ( int i = 0; i < c.length(); i++) {
            int indexA = a.indexOf(c.charAt(i));
            if( indexA != -1) {
                if(auxA < indexA) {
                    auxA = indexA;
                } else {
                    return false;
                }
            }
            int indexB = b.indexOf(c.charAt(i));
            if( indexB != -1) {
                if(auxB < indexB) {
                    auxB = indexB;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Generates a random valid mix for two Strings
     * @param a First String to be mixed
     * @param b Second String to be mixed
     * @return A String that is a random valid mix of the other two .
     */
    public static String generateRandomValidMix ( String a , String b) {
        int totalLength = a.length() + b.length();
        StringBuilder str = new StringBuilder(totalLength);
        int  auxA = 0;
        int  auxB = 0;

        for( int i = 0 ; i < totalLength ; i++){
            int rand = (int)Math.round(Math.random());
            if ( rand == 0 ) {
                if (auxA >= a.length()){
                    str.append(b.charAt(auxB));
                    auxB++;
                } else {
                    str.append(a.charAt(auxA));
                    auxA++;
                }
            }else{
                if (auxB >= b.length()){
                    str.append(a.charAt(auxA));
                    auxA++;
                } else {
                    str.append(b.charAt(auxB));
                    auxB++;
                }
            }
        }

        return str.toString();
    }
}
