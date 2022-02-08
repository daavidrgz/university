package e2;

public class Code {
    /**
     * Determines whether a keypad is valid . To do this , it must be a rectangle and
     * the numbers must follow the alphanumeric sequence indicated . If the array
     * (or any of its subarrays ) is null it will be returned false .
     * @param keypad The keypad to be analyzed .
     * @return true if it is valid , false otherwise .
     */
    public static boolean isKeypadValid ( char [][] keypad ) {
        char iteratorChar = '1';

        // Check if keypad is null
        if ( keypad == null) {
            return false;
        }

        int keypadH = keypad[0].length;
        for (int i = 0; i < keypad.length; i++) {
            if ( keypad[i] == null || keypad[i].length != keypadH ) {
                return false;
            }
        }


        // Check if a 1 1 array is valid
        if( keypad.length == 1 && keypad[0].length == 1) {
            if (keypad[0][0] == '1') {
                return true;
            } else {
                return false;
            }
        }

        // Check if a 2D array has null rows
        /*
        for( int i = 0 ; i<keypad.length;i++){
            if(keypad[i] == null) {
                return false;
            }
        }
        */

        // Check keypad vertical orientation , if so, transpose and go recursive
        if (keypad.length > 1 && keypad[1][0] == '2') {
            char[][] transposed = new char[keypad[0].length][keypad.length];
            for( int i = 0 ; i<keypad[0].length;i++){
                for( int j = 0; j<keypad.length; j++ ){
                    transposed[i][j] = keypad[j][i];
                }
            }
            return isKeypadValid(transposed);

        // Check keypad horizontal orientation && check if keypad is valid
        }else if( keypad[0][1] == '2') {
            for(int i = 0 ; i< keypad.length; i++){
                for( int j = 0; j< keypad[0].length; j++){
                    if(keypad[i][j] == iteratorChar){
                        if(iteratorChar >= '0' && iteratorChar<= '9'+1 || iteratorChar >= 'A' && iteratorChar<= 'Z' ) {
                            iteratorChar++;
                            if (iteratorChar == '9' + 1) {
                                iteratorChar = '0';
                            }
                            if( iteratorChar == '0' + 1) {
                                iteratorChar = 'A';
                            }
                        }
                    }else{
                        return false;
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }
    /**
     * Checks if an array of movements is valid when obtaining a key on a keypad .
     * An array of movements is valid if it is formed by Strings that only have the
     * four capital letters U, D, L and R. If the array of Strings or any of the
     * Strings is null it will be returned false .
     * @param movements Array of Strings representing movements .
     * @return true if it is valid , false otherwise .
     */
    public static boolean areMovementsValid ( String [] movements ) {
        if (movements == null) {
            return false;
        }

        for ( int i = 0; i < movements.length; i++) {
            if( movements[i] == null) {
                return false;
            }
            for( int j = 0 ; j < movements[i].length(); j++) {
                if ( movements[i].charAt(j) == 'U'
                        || movements[i].charAt(j) == 'D'
                        || movements[i].charAt(j) == 'L'
                        || movements[i].charAt(j) == 'R') {

                } else {
                    return false;
                }
            }
        }

        return true;
    }
    /**
     * Given a keypad and an array of movements , it returns a String with the code
     * resulting from applying the movements on the keypad .
     * @param keypad alphanumeric keypad .
     * @param movements Array with the different movements to be made for each
    number of the key .
     * @return Resulting code .
     * @throws IllegalArgumentException if the keypad of the movements are invalid .
     */
    public static String obtainCode ( char [][] keypad , String [] movements ) throws IllegalArgumentException {
        StringBuilder str = new StringBuilder(movements.length);
        if( isKeypadValid(keypad) && areMovementsValid(movements)) {
            int x = 0;
            int y = 0;
            int xMax = keypad[0].length - 1;
            int yMax = keypad.length - 1;

            for (int i = 0; i < movements.length; i++) {
                for ( int j = 0; j < movements[i].length(); j++) {
                    if (movements[i].charAt(j) == 'D') {
                        if (y + 1 <= yMax) {
                            y++;
                        }
                    } else if (movements[i].charAt(j) == 'U') {
                        if (y - 1 >= 0) {
                            y--;
                        }
                    } else if (movements[i].charAt(j) == 'R') {
                        if (x + 1 <= xMax) {
                            x++;
                        }
                    } else if (movements[i].charAt(j) == 'L') {
                        if (x - 1 >= 0) {
                            x--;
                        }
                    }
                }
                str.append(keypad[y][x]);

            }
        } else {
            throw new IllegalArgumentException();

        }

        return str.toString();
    }
}
