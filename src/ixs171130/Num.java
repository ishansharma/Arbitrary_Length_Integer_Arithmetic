// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

package ixs171130;

import java.util.ArrayList;
import java.util.List;

public class Num  implements Comparable<Num> {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    List<Long> arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**
     * Accepts a string, breaks it in to smaller elements (based on base) and store into arr
     *
     * @param s Input string
     */
    public Num(String s) {
        arr = new ArrayList<>();

        // if we are given an empty string, throw an exception
        if (s.length() == 0) {
            throw new ArithmeticException("Empty string given to constructor. Can't parse as a number");
        }

        // if number is negative, remove negative sign and mark isNegative true
        if (s.indexOf("-") == 0) {
            isNegative = true;
            s = s.replace("-", "");
        }

        // getting number of zeroes in our base, this will only support bases that are power of 10.
        // doing this for now because otherwise, we need to divide numbers and that's not implemented yet!
        Integer zeros = ((Long) base).toString().length() - ((Long) base).toString().replace("0", "").length();

        // if only zeroes are passed, store a zero in list
        if (s.replace("0", "").length() == 0) {
            arr.add(Long.parseLong("0"));
        } else {
            for (int i = s.length(); i > 0; i = i - zeros) {
                int j = i - zeros;

                // if we go below zero, we will be out of index
                if (j < 0) {
                    j = 0;
                }

                String toAdd = s.substring(j, i);

                // initial zeroes are not needed
                while (toAdd.indexOf("0") == 0) {
                    toAdd = toAdd.substring(1);
                    // don't remove last zero
                    if (toAdd.length() == 1) {
                        break;
                    }
                }

                arr.add(Long.parseLong(toAdd));
            }
        }
        len = arr.size();
    }

    public Num(long x) {
    }

    public static Num add(Num a, Num b) {
        return null;
    }

    public static Num subtract(Num a, Num b) {
        return null;
    }

    public static Num product(Num a, Num b) {
        return null;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        return null;
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
        return null;
    }

    // return a%b
    public static Num mod(Num a, Num b) {
        return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        return null;
    }


    // Utility functions
    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) {
        return 0;
    }

    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
        System.out.println(returnListAsString());
    }

    /**
     * Returns a string in base: num1, num2 num3... format
     * <p>
     * This is implemented instead of printList because string value is required for unit testing. printList merely
     * prints whatever this method returns
     *
     * @return String A string representation of the list
     */
    public String returnListAsString() {
        StringBuilder output = new StringBuilder();

        output.append(String.valueOf(base)).append(": ");
        for (Long n : arr) {
            output.append(n.toString()).append(" ");
        }

        return output.toString().trim();
    }

    // Return number to a string in base 10
    public String toString() {
        return null;
    }

    public long base() { return base; }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {
        return null;
    }

    // Divide by 2, for using in binary search
    public Num by2() {
        return null;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
        return null;
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
        return null;
    }


    public static void main(String[] args) {
//        Num x = new Num(999);
//        Num y = new Num("8");
//        Num z = Num.add(x, y);
//        System.out.println(z);
//        Num a = Num.power(x, 8);
//        System.out.println(a);
//        if(z != null) z.printList();
        Num x = new Num("10965");
        x.printList();
    }
}