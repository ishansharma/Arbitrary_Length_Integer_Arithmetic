package ixs171130;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Represents an arbitrary length number
 *
 * @author Ishan Sharma, Ravikiran Kolanpaka, Sharayu Mantri
 * @version 1.0
 * @since 1.0
 */
public class Num implements Comparable<Num> {

    // setting base to 1 followed by 9 zeroes because square root of (2^63 - 1) is 3037000448
    // which has 10 digits. So that's max safe base.
    // Using the nearest power of 10 because that's simpler to represent and test
    static final long toStringBase = 1000000000;
    static final long defaultBase = 1000000000L;
    long base = defaultBase;
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len = 0;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**
     * Copy constructor for functions that need a copy of current number
     *
     * @param another Num object to copy from
     */
    public Num(Num another) {
        this.base = another.base;
        this.arr = new long[another.arr.length];
        System.arraycopy(another.arr, 0, this.arr, 0, this.arr.length);  // manual copying is more lines

        this.isNegative = another.isNegative;
        this.len = another.len;
    }

    /**
     * Accepts a base 10 string, converts it to Num object in defaultBase
     *
     * @param s Input string
     */
    public Num(String s) {
        //check for negative and remove
        int size = s.length();
        isNegative = false;
        if (s.indexOf("-") == 0) {
            isNegative = true;
            size = size - 1;
            s = s.replace("-", "");
        } else {
            isNegative = false;
        }

        long[] arr = new long[size];
        if (s.length() == 0) {
            throw new ArithmeticException("Empty string given to constructor. Can't parse as a number");
        }

        Pattern pattern = Pattern.compile("\\d+");
        if(!pattern.matcher(s).matches()){
            throw new ArithmeticException("String should consist only digits");
        }
        int j = 0;
        char[] input = s.toCharArray();
        for (int i = s.length() - 1; i >= 0; i--) {
            arr[j++] = (int) input[i] - (int) '0';
        }

        Num base10Number = new Num(arr, 10, isNegative);

        Num result = base10Number.convertBase(defaultBase);
        this.arr = result.arr;
        this.len = result.len;
        this.base = result.base;
        this.isNegative = result.isNegative;

    }

    /**
     * Accepts a long and needs converts into a Num in default base
     * <p>
     * This constructor will call intializeArray(x,base) internally.
     *
     * @param x long number
     */

    public Num(long x) {
        initializeArray(x, defaultBase);
    }

    /**
     * Converts a long number to Num in an arbitrary base
     *
     * @param x    - long number which needs to be converted into a Num format.
     * @param base - the base which we want to be converted to.
     */
    public Num(long x, long base) {
        initializeArray(x, base);
    }


    /**
     * Internal function used by long constructor.
     *
     * @param x    - a long number which needs to be converted into a Num format.
     * @param base - the base which we want to be converted to.
     *             <p>
     *             Here I repeatedly divide the number of the base to get the size of the array.
     *             <p>
     *             Here There will be two while loops. One is to calculate the size of the array.
     *             The second is to store the numbers in the array.
     */

    public void initializeArray(long x, long base) {
        this.base = base;

        if (x == 0) {
            len = 1;
            this.arr = new long[len];
            this.arr[0] = 0L;
        } else {
            if (x < 0) {
                x = -x;
                this.isNegative = true;
            }

            int num_digits = 0;
            long x_dup = x;

            while (x_dup > 0) {
                num_digits++;
                x_dup = x_dup / this.base;
            }

            this.len = num_digits;

            this.arr = new long[this.len];


            for (int i = 0; i < this.len; i++) {
                this.arr[i] = x % this.base;
                x = x / this.base;
            }
        }
    }


    /**
     * Initialize from an array
     *
     * @param array      Array to initialize from
     * @param base       Base for the number
     * @param isNegative Flag whether number is negative or positive
     */
    public Num(long[] array, long base, boolean isNegative) {
        arr = array;
        len = array.length;
        this.base = base;
        this.isNegative = isNegative;
    }


    /**
     * Add two numbers
     *
     * @param a - First number to be added.
     * @param b - Second number to be added.
     * @return the added number in NUM format.
     * <p>
     * Iterating through both the arrays simultaneously and adding the digits
     * along with the carry.
     * <p>
     * If one of the arrays is not empty and other is empty, add takes only that array.
     */
    public static Num add(Num a, Num b) {
        Num result;

        if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for addition has to be same");
        }


        if (a.isNegative == b.isNegative) {
            Long sum;
            Long carry = 0L;

            int i = 0;
            int j = 0;

            long[] arr = new long[Math.max(a.len, b.len) + 1];

            int counter = 0;

            while (i < a.len && j < b.len) {

                sum = a.arr[i] + b.arr[j] + carry;

                arr[counter] = sum % a.base;
                carry = sum / a.base;

                i++;
                j++;
                counter++;

            }

            while (i < a.len) {
                sum = a.arr[i] + carry;
                arr[counter] = sum % a.base;
                carry = sum / a.base;
                i++;
                counter++;

            }

            while (j < b.len) {

                sum = b.arr[j] + carry;
                arr[counter] = sum % a.base;
                carry = sum / a.base;
                j++;
                counter++;

            }

            if (carry > 0)
                arr[counter] = carry;
            else
                arr[counter] = 0L;


            result = new Num(removeTrailingZeros(arr), a.base, a.isNegative && b.isNegative);
        } else {
            if (a.compareTo(b) > 0) {
                b.isNegative = false;
                result = subtract(a, b);
                b.isNegative = true;
            } else {
                a.isNegative = false;
                result = subtract(b, a);
                a.isNegative = true;
            }
        }

        return result;
    }

    /**
     * Subtract b from a and return a Num
     * <p>
     * Actual subtraction is handled in subtractInternal(), this method decides if we actually want to add (e.g. both
     * numbers are negative) or subtract the numbers and calls subtractInternal(Num larger, Num smaller)
     *
     * @param a First number
     * @param b Second number
     * @return result for a - b
     * @throws ArithmeticException If numbers aren't in same base.
     */
    public static Num subtract(Num a, Num b) {
        if (a.base != b.base) {
            throw new ArithmeticException("Numbers in different bases passed to subtract");
        }

        int comparison = a.compareTo(b);
        Num result;
        if (comparison == 0) {
            result = new Num(0);
        } else if (comparison > 0) {  // a is bigger
            if (!a.isNegative && b.isNegative) {
                b.isNegative = false;
                result = add(a, b);
                b.isNegative = true;
            } else if (a.isNegative && b.isNegative) {
                result = subtractInternal(b, a);
            } else {  // if we are here, both a and b are positive
                result = subtractInternal(a, b);
            }
        } else {  // b is bigger
            if (a.isNegative && !b.isNegative) {
                a.isNegative = false;
                result = add(a, b);
                a.isNegative = true;
                result.isNegative = true;
            } else if (!a.isNegative && !b.isNegative) {
                result = subtractInternal(b, a);
                result.isNegative = true;
            } else {  // both a and b are negative
                result = subtractInternal(a, b);
                result.isNegative = true;
            }
        }
        result.base = a.base;
        return result;
    }

    /**
     * Handles the subtraction for a - b. a has to be bigger than b.
     *
     * @param a Larger number
     * @param b Smaller number
     * @return result for a - b
     */
    private static Num subtractInternal(Num a, Num b) {
        int i = 0;
        long[] result = new long[(a.len > b.len) ? a.len : b.len];
        boolean borrow = false;

        // subtract till both arrays have numbers
        while (i < a.len && i < b.len) {
            if (a.arr[i] > b.arr[i]) {  // if number in a is bigger, we just subtract and save that to result
                if (borrow) {
                    result[i] = (a.arr[i] - 1) - b.arr[i];
                    borrow = false;
                } else {
                    result[i] = a.arr[i] - b.arr[i];
                }
            } else if (a.arr[i] == b.arr[i]) { // if both numbers are equal, check for borrow and take carry
                if (borrow) {
                    result[i] = (a.arr[i] - 1 + a.base) - b.arr[i];
                } else {
                    result[i] = a.arr[i] - b.arr[i];
                }
            } else { // if number in b is bigger, we take a borrow and then subtract
                if (borrow) {
                    result[i] = (a.arr[i] - 1 + a.base) - b.arr[i];
                } else {
                    result[i] = (a.arr[i] + a.base) - b.arr[i];
                    borrow = true;
                }
            }

            i++;
        }

        // copy rest of numbers from longer array to result
        long[] copySource = a.len < b.len ? b.arr : a.arr; // copySource will point to bigger array

        while (i < copySource.length) {
            if (borrow) {
                result[i] = copySource[i] - 1;
                borrow = false;
            } else {
                result[i] = copySource[i];
            }
            i++;
        }

        return new Num(removeTrailingZeros(result), a.base, false);
    }

    /**
     * Performs multiplication for two Num objects
     * <p>
     * Used basic long multiplication https://en.wikipedia.org/wiki/Multiplication_algorithm
     *
     * @param a Multiplication operand 1
     * @param b Multiplication operand 1
     * @return a * b ( for both and b in base b)
     * @throws ArithmeticException If bases are different
     */
    public static Num product(Num a, Num b) {
        if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for multiplication has to be same");
        }

        int size = a.len + b.len;
        long result[] = new long[size];
        long carry;
        int i = 0, j;
        for (long bi : b.arr) {
            carry = 0;
            j = 0;
            for (long aj : a.arr) {
                long num = result[i + j] + carry + (aj * bi);
                carry = num / a.base;
                result[i + j] = num % a.base;
                j++;
            }
            result[i + a.len] = result[i + a.len] + carry;
            i++;
        }

        return new Num(removeTrailingZeros(result), a.base, (a.isNegative ^ b.isNegative));
    }

    /**
     * Some operations leave trailing zeroes at end of the array. This function removes them and returns a new array
     *
     * @param arr long[] array
     * @return array without trailing zeroes
     */
    private static long[] removeTrailingZeros(long[] arr) {
        int size = arr.length;
        int newSize = size - 1;
        while (arr[newSize] == 0 && newSize > 0) {  // second condition is there to avoid going to -1 in some cases
            newSize--;
        }

        // don't do anything is there are no zeroes to be removed
        if (newSize == size - 1) {
            return arr;
        }

        long[] result;
        result = new long[newSize + 1];
        System.arraycopy(arr, 0, result, 0, newSize + 1);

        return result;
    }

    /**
     * Used divide and conquer method to calculate the power of the number recursively.
     * <p>
     * power(a,n/2) is called which is stored in temp.
     * <p>
     * if n is an even number - temp square is calculated and returned.
     * other wise a * temp square is calcualted.
     *
     * @param a - A number whose power needs to be calculated whose isNegative will be positive.
     * @param n - the power.
     * @return power of the number.
     */
    public static Num powerInternal(Num a, long n) {
        if (n < 0)
            throw new ArithmeticException("Power of negative number not possible");


        if (n == 0) {
            return new Num(1);
        }

        Num res = new Num(1);
        Num temp = new Num(a);
        while(n != 0) {
            if (n % 2 == 1) {
                res = product(res, temp);
            }
            n = n /2;
            temp = product(temp, temp);
        }
        return res;

//        temp = power(a, n / 2);
//        if (n % 2 == 0)
//            return (product(temp, temp));
//        else
//            return (product(a, product(temp, temp)));

    }

    /**
     * @param a - A number whose power needs to be calculated whose isNegative will be positive.
     * @param n - the power.
     * @return power of the number.
     * <p>
     * checks if n is positive number of not. if negative number, checks if it is even or not.
     */
    public static Num power(Num a, long n) {
        Num result;

        if (a.isNegative) {
            a.isNegative = false;
            result = powerInternal(a, n);
            if (n % 2 == 0) {
                result.isNegative = false;
            } else {
                result.isNegative = true;
            }
            result.base = a.base;
            result.len = result.arr.length;
            a.isNegative = true;

        } else {
            result = powerInternal(a, n);
            result.base = a.base;
            result.len = result.arr.length;
        }
        return result;

    }

    /**
     * Divide two Num Types dividend / divisor
     *
     * @param dividend : base b
     * @param divisor  : base b
     * @return a / b
     * returns null if divide by 0 and floor value for decimal divisions
     */
    public static Num divide(Num dividend, Num divisor) {

        boolean dividendNegative = dividend.isNegative;
        boolean divisorNegative = divisor.isNegative;
        boolean flag = false;

        if (dividend.isNegative ^ divisor.isNegative) {
            flag = true; // if this number is negative, return -1. Else return 1.
        }
        dividend.isNegative = false;
        divisor.isNegative = false;

        if (divisor.compareTo(new Num(0, divisor.base)) == 0){
            return null;
        }


        if (dividend.len < divisor.len || (dividend.len == divisor.len &&
                dividend.arr[dividend.len - 1] < divisor.arr[divisor.len - 1])) {
            Num result = new Num(0, dividend.base);
            result.base = dividend.base;
            return result;
        }

        Num lower = new Num(1, dividend.base);
        Num higher = new Num(dividend);
        Num ans = new Num(1, dividend.base);
        while (lower.compareTo(higher) <= 0) {

            //get mid  = (higher + lower)/2
            Num mid = add(lower, higher).by2();

            //Compare ( divisor * mid - dividend, 0)
            Num one = new Num(1, dividend.base);
            int compareToDM = product(divisor, mid).compareTo(dividend);

            //if compare returns 0 then mid is quotient
            if (compareToDM == 0) {
                mid.isNegative = flag;
                dividend.isNegative = dividendNegative;
                divisor.isNegative = divisorNegative;
                return mid;
            }
            //else if -1 then mid is lower half
            else if (compareToDM < 0) {
                lower = add(mid, one);
                ans = mid;
            }
            //else mid is in the upper half
            else {
                higher = subtract(mid, one);
            }
        }

        ans.isNegative = flag;
        dividend.isNegative = dividendNegative;
        divisor.isNegative = divisorNegative;
        return ans;
    }



    /**
     * Calculate mod.
     * Assumptions: a and b are non negative and b > 0
     * mod returns remainder else returns null if b = 0
     *
     * @param a : dividend
     * @param b : divisor
     * @return a % b (both in base b)
     */
    public static Num mod(Num a, Num b) {
        if (a.isNegative || b.isNegative) {
            throw new ArithmeticException(" Mod function arguments cannot be negative ");
        }
        if (a.compareTo(b) < 0) {
            return a;
        }
        Num quotient = divide(a, b);
        if (quotient == null) {
            return null;
        }
        Num product = product(b, quotient);
        product.isNegative = false;
        return subtract(a, product);
    }

    /**
     * @param a - The number whose square root needs to be calculated.
     * @return - square root of th number.
     * <p>
     * binary search is used from start = 1  till end = a/2.
     * <p>
     * every time midsquare is calculated and compared with the actual number.
     * <p>
     * if it is equal that number is returned.
     */
    public static Num squareRoot(Num a) {

        if (a.isNegative)
            throw new ArithmeticException("No Square root for Negative Numbers");

        if (a.compareTo(new Num("0")) == 0)
            return (new Num("0"));

        Num start = new Num("1");

        Num mid, midsq, sum;

        Num end = new Num(a.by2());
        Num ans = new Num("1");

        int comparision;

        while (start.compareTo(end) <= 0) {
            sum = add(start, end);
            mid = sum.by2();
            midsq = product(mid, mid);

            comparision = midsq.compareTo(a);

            if (comparision == 0)
                return (mid);
            else if (comparision < 0) {
                start = add(mid, new Num(1));
                ans = mid;
            } else {
                end = subtract(mid, new Num(1));
            }
        }

        return ans;
    }

    /**
     * Compare this number to other number.
     *
     * @param other Other number, also a Num
     * @return +1 if this number is greater, 0 if numbers are equal, -1 if other number is greater
     */
    public int compareTo(Num other) {
        int result = 0;

        if (base != other.base) {
            throw new ArithmeticException("Numbers of different bases given");
        }

        // ^ is bitwise XOR, will be true only if one of the numbers is negative
        if (isNegative ^ other.isNegative) {
            return isNegative ? -1 : 1;  // if this number is negative, return -1. Else return 1.
        }


        // If length of lists is different:
        //  Case 1: Numbers are positive: bigger list represents bigger number
        //  Case 2: Numbers are negative: smaller list represents bigger number
        if (len != other.len) {
            result = len > other.len ? 1 : -1;
            return isNegative ? result * -1 : result;
        }

        // If length of lists is same, we compare them starting at the tail. We stop when we find a smaller/larger
        // number and return accordingly
        for (int i = len - 1; i >= 0; i--) {
            if (arr[i] != other.arr[i]) {
                result = arr[i] > other.arr[i] ? 1 : -1;
                return isNegative ? result * -1 : result;
            }
        }

        // default condition, returning 0
        return result;
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

    /**
     * Returns string representation in base 10
     *
     * @return String representation of Num
     */
    public String toString() {
        Num result = this.convertBase(toStringBase);
        int noOfZeros = ((Long) toStringBase).toString().length() - ((Long) toStringBase).toString().replace("0", "").length();

        StringBuilder output = new StringBuilder();
        if (isNegative) {
            output.append("-");
        }
        for (int i = result.arr.length - 1; i >= 0; i--) {
            String res = Long.toString(result.arr[i]);
            if (res.length() < noOfZeros && i != result.arr.length - 1) {
                int subs = noOfZeros - res.length();
                for (int j =0; j< subs; j++) {
                    res = "0" + res;
                }
            }
            output.append(res);
        }
        return output.toString();
    }

    /**
     * Returns the base for this object
     *
     * @return base(long)
     */
    public long base() {
        return base;
    }

    /**
     * Utility function for testing in convertBase for base lower than 56. Used in tests.
     *
     * @return String
     */
    public String printNumberByBase() {
        StringBuilder output = new StringBuilder();
        if (isNegative) {
            output.append("-");
        }
        for (int i = this.len - 1; i >= 0; i--) {
            output.append(Character.forDigit((int) arr[i], (int) base));
        }
        return output.toString();
    }


    /**
     * Convert base of current Num and return a new Num object with new base
     *
     * @param newBase - the new base in which the number needs to be calculated.
     * @return the number in the new base.
     * <p>
     * Here each digit in the array is taken and converted into NUM format using the long constructor.
     * And then hornors method is calculated with each digit represented in the new base format.
     */
    public Num convertBase(long newBase) {
        if (newBase == this.base) {
            return this;
        }

        if (newBase < 2) {
            throw new ArithmeticException("Base can not be less than 2");
        }

        int length = this.arr.length;
        Num result = new Num(this.arr[length - 1], newBase);
        Num oldBase = new Num(this.base, newBase);
        Num digit;

        for (int i = length - 2; i >= 0; i--) {
            digit = new Num(this.arr[i], newBase);
            result = product(result, oldBase);
            result = add(result, digit);
        }

        result.isNegative = this.isNegative;
        return (result);
    }

    /**
     * Divide by 2 basic division logic
     * this function is used during binary search for normal division
     *
     * @return Num / 2
     */
    public Num by2() {
        if (len == 0) {
            return new Num(0);
        }

        long[] arr2 = new long[len];
        long carry = 0;

        for (int i = len - 1; i >= 0; i--) {
            arr2[i] = (carry * base + arr[i]) / 2;
            carry = arr[i] % 2;
        }
        return new Num(removeTrailingZeros(arr2), this.base, isNegative);
    }

    /**
     * @param expr - each string is either an operator or an operand.
     * @return the evaluation of that number.
     * <p>
     * Data structure used is a stack.
     * <p>
     * if the input is an operand(Num). it is pushed into the stack.
     * If the input is an operator. two NUMs are popped and their respective expression
     * is calculated.
     */
    public static Num evaluatePostfix(String[] expr) {

        Deque<Num> stack = new ArrayDeque<>();
        Num val1, val2;

        for (String c : expr) {
            if (Objects.equals(Operators.getType(c), Operators.type.NUMBER)) {
                stack.push(new Num(c));
            } else {
                val1 = stack.pop();
                val2 = stack.pop();

                switch (c) {
                    case "+":
                        stack.push(add(val2, val1));
                        break;
                    case "-":
                        stack.push(subtract(val2, val1));
                        break;
                    case "/":
                        stack.push(divide(val2, val1));
                        break;
                    case "*":
                        stack.push(product(val2, val1));
                        break;
                    case "^":
                        stack.push(power(val2, Long.parseLong(val1.toString())));
                        break;
                    case "%":
                        stack.push(mod(val2, val1));
                        break;
                }
            }

        }


        return stack.pop();
    }

    /**
     * Evaluate an infix expression given as string array.
     * Internally, just converts to postfix and then evaluates it.
     *
     * @param expr String array containing numbers and operators
     * @return result of the expression as a Num object
     */
    public static Num evaluateInfix(String[] expr) {
        return evaluatePostfix(convertInfixToPostfix(expr));
    }

    /**
     * Accept InFix expression and convert to PostFix using Shunting Yard algorithm
     * https://en.wikipedia.org/wiki/Shunting-yard_algorithm
     *
     * @param expr An array of strings containing expression in infix notation
     * @return Converted expression
     */
    public static String[] convertInfixToPostfix(String[] expr) {
        if (expr.length < 1) {
            throw new ArithmeticException("Empty expression given for evaluation.");
        }

        List<String> result = new LinkedList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push("~"); // dummy operation to determine that we've reached bottom of the stack


        for (String op : expr) {
            // if token is number, add it to output queue
            if (Operators.getType(op).equals(Operators.type.NUMBER)) {
                result.add(op);
            } else if (Operators.getType(op).equals(Operators.type.OPERATOR)) {
                while ((Operators.getPrecedence(op) < Operators.getPrecedence(stack.peek())
                        || (Operators.getPrecedence(op).equals(Operators.getPrecedence(stack.peek())) && !op.equals("^")))
                        && (!Operators.getType(op).equals(Operators.type.LEFT_BRACKET))
                ) {
                    result.add(stack.pop());
                }
                stack.push(op);
            } else if (Operators.getType(op).equals(Operators.type.LEFT_BRACKET)) {
                stack.push(op);
            } else if (Operators.getType(op).equals(Operators.type.RIGHT_BRACKET)) {
                while (!stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                stack.pop();
            }
        }


        // if there are more tokens to be read
        while (!stack.peek().equals("~")) {
            result.add(stack.pop());
        }


        String[] res = new String[result.size()];  // directly using toArray gives Object[], we want String[]
        return result.toArray(res);
    }

    /**
     * Child class for helping with shunting yard operations.
     */
    private static class Operators {
        private enum type {
            OPERATOR, LEFT_BRACKET, RIGHT_BRACKET, NUMBER
        }

        /**
         * Decide whether given string is an operator/bracket/number
         *
         * @param str Input string, either one of "*, +, -, /, %, ^, (, )" or a number
         * @return a string containing type of the input string
         */
        static type getType(String str) {
            switch (str) {
                case "*":
                case "+":
                case "-":
                case "/":
                case "%":
                case "^":
                    return type.OPERATOR;
                case "(":
                    return type.LEFT_BRACKET;
                case ")":
                    return type.RIGHT_BRACKET;
                default:
                    return type.NUMBER;
            }
        }

        /**
         * @param str Input symbol
         * @return An integer value for precedence
         */
        static Integer getPrecedence(String str) {
            switch (str) {
                case "^":
                    return 4;
                case "*":
                case "/":
                case "%":
                    return 3;
                case "+":
                case "-":
                    return 2;
                default:
                    return 0;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String input = in.nextLine();

            try {
                String[] inputAr = input.split("\\s+");
                System.out.println(Num.evaluateInfix(inputAr));
            } catch (PatternSyntaxException e) {
                System.out.println("Error while parsing expression");
            }
        }
    }
}