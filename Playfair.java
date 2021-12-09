import java.util.Arrays;

public class Playfair {

    private static final Character[][] playfairSquare = new Character[5][5];

    private static final String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    public static boolean characterInString(String s, Character character) {
        boolean check = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == character) {
                check = true;
            }
        }
        return check;
    }

    public Playfair(String codeword) {

        // switch to upper case letters
        codeword = codeword.toUpperCase();

        // switch J with I
        codeword = codeword.replace('J', 'I');

        System.out.println("Codeword: " + codeword);

        // building codeword without doubled chars
        String codewordShort = codeword.substring(0, 1);
        for (int i = 1; i < codeword.length(); i++) {
            if (!characterInString(codewordShort, codeword.charAt(i))) {
                codewordShort += codeword.charAt(i);
            }
        }

        // building codeword without doubled chars filled with missing chars of alphabet
        String codewordLong = codewordShort;
        for (int i = 0; i < alphabet.length(); i++) {
            if (!characterInString(codewordLong, alphabet.charAt(i))) {
                codewordLong += alphabet.charAt(i);
            }
        }

        // filling playfairSquare with codewordLong
        for (int j = 0; j < 5; j++) {
            for (int k = 0; k < 5; k++) {
                playfairSquare[j][k] = codewordLong.charAt(j*5+k);
            }
        }

        // print square
        System.out.println("");
        printSquare();

    }

    public static void printSquare() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (j < 4) {
                    System.out.print(playfairSquare[i][j]);
                } else {
                    System.out.println(playfairSquare[i][j]);
                }
            }
        }
    }

    private static String cleanWord(String word) {

        // switch to upper case letters
        word = word.toUpperCase();

        // switch J with I
        word = word.replace('J', 'I');

        // clean word from special characters
                String wordClean = "";
        for (int i = 0; i < word.length(); i++) {
            if (characterInString(alphabet, word.charAt(i))) {
                wordClean += word.charAt(i);

            }
        }

        // build pairs of chars
        String wordPairs = "";
        int counter = 1;
        while (counter < wordClean.length()) {
            if (wordClean.charAt(counter-1) != wordClean.charAt(counter)) {
                wordPairs = wordPairs + wordClean.charAt(counter-1) + wordClean.charAt(counter) + " ";
                counter += 2;
            } else {
                wordPairs = wordPairs + wordClean.charAt(counter-1) + "X ";
                counter += 1;
            }
        }
        if (counter%2 == 0 || wordClean.length()%2 == 1) {
            wordPairs = wordPairs + wordClean.charAt(wordClean.length()-1) + "A";
        }

        // printing finished word pairs
        System.out.println("Word pairs: " + wordPairs);

        return wordPairs;
    }

    // get coordinates of a char in the Playfair Square
    private static Position findInSquare(Character c) {
        int a = 0, b = 0;
        for (int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                if (playfairSquare[i][j] == c) {
                    a = i;
                    b = j;
                }
            }
        }
        Position pos = new Position(a,b);
        return pos;
    }

    // final program to encode
    public static String encode(String word) {

        // print original search word
        System.out.println("");
        System.out.println("Searchword: " + word);

        // bring search word into pairs in order to encode
        String wordPairs = cleanWord(word);

        String result = "";
        for (int i = 0; i < wordPairs.length(); i += 3) {

            // find position
            Position first = findInSquare(wordPairs.charAt(i));
            Position second = findInSquare(wordPairs.charAt(i+1));

            // rows are equal
            if (first.x == second.x) {
                int cf = first.y + 1;
                if (cf == 5) {
                    cf = 0;
                }
                int cs = second.y + 1;
                if (cs == 5) {
                    cs = 0;
                }
                result = result + playfairSquare[first.x][cf] + playfairSquare[second.x][cs] + " ";
            }

            // columns are equal
            if (first.y == second.y) {
                int rf = first.x + 1;
                if (rf == 5) {
                    rf = 0;
                }
                int rs = second.x + 1;
                if (rs == 5) {
                    rs = 0;
                }
                result = result + playfairSquare[rf][first.y] + playfairSquare[rs][second.y] + " ";
            }

            // nor rows neither columns are equal
            if (first.x != second.x && first.y != second.y) {
                result = result + playfairSquare[first.x][second.y] + playfairSquare[second.x][first.y] + " ";
            }

        }

        // print encoded result
        System.out.println("Encoded: " + result);

        return result;
    }

    public static void main(String args[]) {
        Playfair apfelstrudel = new Playfair("Apfelstrudel");
        encode("Apfelstrudel");
        encode("Klopapier");
        System.out.println("--------------------------");
        System.out.println("--------------------------");

        Playfair codeword = new Playfair("Codeword");
        encode("Mittwoch");
        encode("Otto");
        System.out.println("--------------------------");
    }
}

/*
Codeword: APFELSTRUDEL

APFEL
STRUD
BCGHI
KMNOQ
VWXYZ

Searchword: Apfelstrudel
Word pairs: AP FE LS TR UD EL 
Encoded: PF EL AD RU DS LA 

Searchword: Klopapier
Word pairs: KL OP AP IE RA
Encoded: QA ME PF HL SF 
--------------------------
--------------------------
Codeword: CODEWORD

CODEW
RABFG
HIKLM
NPQST
UVXYZ

Searchword: Mittwoch
Word pairs: MI TX TW OC HA
Encoded: HK QZ ZG DO IR 

Searchword: Otto
Word pairs: OT TO 
Encoded: WP PW 
--------------------------
*/