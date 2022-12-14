import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static String calc(String input) throws IOException, IllegalArgumentException {

        //Checking if we have any fractional arabic number
        if(input.contains(".")) {
            throw new IOException("Only integer values are allowed.");
        }
        //Checking if string contains symbols other than numbers and operands
        if (!input.matches(".[0-9|M|D|C|L|X|V|I|\\*|\\+|\\/|\\-| ]*")) {
            throw new IOException("Only arabic OR roman numbers are allowed.");
        }

        boolean romanOtput = false;
        String operands[];
        int agregate;
        String result;

        //Concidering checks above, removes spaces. Without them removes all special symbols like /n, /t
        input = input.replaceAll("\\s+","");

        String operators[]=input.split("[0-9|M|D|C|L|X|V|I]+");

        //Checking if we have more than one arithmetical operations
        if (operators.length != 2) {
            throw new IOException("Only one operator is allowed.");
        }

        if (input.matches("[M|D|C|L|X|V|I\\*\\+\\/\\-]+")) {
            romanOtput = true;
            operands=input.split("[\\*\\+\\/\\-]+");
            agregate = RomanNumber.romanToArabic(operands[0]);

            for(int i=1;i<operands.length;i++){
                switch (operators[i]) {
                    case "+":
                        agregate += RomanNumber.romanToArabic(operands[i]);
                        break;
                    case "-":
                        agregate -= RomanNumber.romanToArabic(operands[i]);
                        break;
                    case "*":
                        agregate *= RomanNumber.romanToArabic(operands[i]);
                        break;
                    case "/":
                        agregate /= RomanNumber.romanToArabic(operands[i]);
                        break;
                }

            }

        } else if (input.matches("[0-9\\*\\+\\/\\-]+")){
            operands =input.split("[\\*\\+\\/\\-]+");
            agregate = Integer.parseInt(operands[0]);

            for(int i=1;i<operands.length;i++){
                switch (operators[i]) {
                    case "+":
                        agregate += Integer.parseInt(operands[i]);
                        break;
                    case "-":
                        agregate -= Integer.parseInt(operands[i]);
                        break;
                    case "*":
                        agregate *= Integer.parseInt(operands[i]);
                        break;
                    case "/":
                        agregate /= Integer.parseInt(operands[i]);
                        break;
                }

            }
        } else {
            throw new IOException("Only arabic OR roman numbers are allowed. Strictly arabic or strictly roman.");
        }


        if (romanOtput) {
            result = RomanNumber.arabicToRoman(agregate);
        } else {
            result = String.valueOf(agregate);
        }
        return result;
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String input = br.readLine();
            System.out.println(calc(input));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}

class RomanNumber {
    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException(input + " cannot be converted to a Roman Numeral");
        }

        return result;
    }

    static String arabicToRoman(int number) throws IllegalArgumentException {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}
enum RomanNumeral {
    I(1), IV(4), V(5), IX(9), X(10),
    XL(40), L(50), XC(90), C(100),
    CD(400), D(500), CM(900), M(1000);

    private int value;

    RomanNumeral(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }

    static List<RomanNumeral> getReverseSortedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparing((RomanNumeral e) -> e.value).reversed())
                .collect(Collectors.toList());
    }
}