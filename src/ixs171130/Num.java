// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

package ixs171130;

import java.math.BigInteger;
import java.util.*;

public class Num implements Comparable<Num> {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len = 0;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]
    static final Num MAX_VALUE = new Num(123456);

    public Num() {
    }

    /**
     * Accepts a string, breaks it in to smaller elements (based on base) and store into arr
     *
     * @params Input string
     */
    public Num(String s) {
        if (s.indexOf("-") == 0) {
            isNegative = true;
            s = s.replace("-", "");
        } else {
            isNegative = false;
        }

        /* Why next two lines are the way they are:
         *  We need to consider the decimal digits. Initially, I was blindly adding 1 to result of expression inside
         *  Math.ceil() but when there are even number of digits, we needlessly added an extra slot in the array
         *  which would have caused issues with operations that need size to function properly e.g. subtraction
         */
        int size;
        size = (int) Math.ceil(((double) s.length() / (((Long) base).toString().length() - 1)));

        arr = new long[size];

        // if we are given an empty string, throw an exception
        if (s.length() == 0) {
            throw new ArithmeticException("Empty string given to constructor. Can't parse as a number");
        }

        // getting number of zeroes in our base, this will only support bases that are power of 10.
        // doing this for now because otherwise, we need to divide numbers and that's not implemented yet!
        int zeros = ((Long) base).toString().length() - ((Long) base).toString().replace("0", "").length();

        // if only zeroes are passed, store a zero
        if (s.replace("0", "").length() == 0) {
            arr = new long[1];
        } else {
            int index = 0;
            for (int i = s.length(); i > 0; i = i - zeros) {
                int j = i - zeros;

                // if we go below zero, we will be out of index
                if (j < 0) {
                    j = 0;
                }

                String toAdd = s.substring(j, i);

                // initial zeroes are not needed
                while ((toAdd.indexOf("0") == 0) && (toAdd.length() > 1)) {
                    toAdd = toAdd.substring(1);
                    // don't remove last zero
                    if (toAdd.length() == 1) {
                        break;
                    }
                }

                arr[index] = (Long.parseLong(toAdd));
                index++;
            }
        }
        len = arr.length;
    }

    public Num(long x) {
        if (x == 0)
    	{
    		len = 1;
    		arr = new long[len];
    		arr[0] = 0l;
    	}
    	else
    	{


    		if(x < 0 )
        	{
        		x = -x;
        		isNegative = true;

        	}

        	int num_digits = 0;
        	long x_dup = x;



        	while(x_dup > 0 )
        	{
        		num_digits++;
        		x_dup = x_dup/base;
        	}

        	len = num_digits;

        	arr = new long[len];


        	for(int i = 0 ; i < len ; i++)
        	{
        		arr[i] = x%base;
        		x = x/base;
        	}

    	}


    	//System.out.println(len);

    	//System.out.println(Arrays.toString(arr));

    }

    /**
     * Initialize from an array
     *
     * @param array
     */
    public Num(long[] array) {
        arr = array;
        len = array.length;
    }


    public static Num add(Num a, Num b) {

        if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for addition has to be same");
        }


        if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative)) {
            Long sum;
            Long carry = 0L;

            int i = 0;
            int j = 0;

            Num add = new Num();

            add.arr = new long[Math.max(a.len,b.len) + 1];

            int counter = 0;

            while( i < a.len &&  j < b.len) {

                sum = a.arr[i] + b.arr[j] + carry;

                add.arr[counter] = sum%defaultBase;
                carry = sum/defaultBase;

                i++;
                j++;
                counter++;

            }

            while(i < a.len) {
                sum = a.arr[i] + carry;
                add.arr[counter] = sum%defaultBase;
                carry = sum/defaultBase;
                i++;
                counter++;

            }

            while(j < b.len) {

                sum = b.arr[j] + carry;
                add.arr[counter] = sum%defaultBase;
                carry = sum/defaultBase;
                j++;
                counter++;

            }

            if(carry > 0 )
                add.arr[counter] = carry;
            else
                add.arr[counter] = 0l;


            add.len = counter;

            if(a.isNegative && b.isNegative)
                add.isNegative = true;

            add.arr = removeTrailingZeros(add.arr);
            add.len = add.arr.length;
            return(add);

        } else {
            return(subtract(a,b));
        }


    }

    /**
     * Subtract b from a and return a Num
     * @param a First number
     * @param b Second number
     * @return result for a - b
     */
    public static Num subtract(Num a, Num b) {
        // creating new instances because I'm modifying objects
        // and that modified our original objects
        Num x = new Num(a.toString());
        Num y = new Num(b.toString());
        int comparison = x.compareTo(y);
        Num result;
        if (comparison == 0) {
            result = new Num(0);
        } else if (comparison > 0) {  // a is bigger
            if (!x.isNegative && y.isNegative) {
                y.isNegative = false;
                result = add(x, y);
            } else if (x.isNegative && y.isNegative) {
                result = subtractInternal(y, x);
            } else {  // if we are here, both a and b are positive
                result = subtractInternal(x, y);
            }
        } else {  // b is bigger
            if (x.isNegative && !y.isNegative) {
                x.isNegative = false;
                result = add(x, y);
                result.isNegative = true;
            } else if (!x.isNegative && !y.isNegative) {
                result = subtractInternal(y, x);
                result.isNegative = true;
            } else {  // both a and b are negative
                result = subtractInternal(x, y);
                result.isNegative = true;
            }
        }
        return result;
    }

    /**
     * Handles the actual subtraction for a - b. a has to be bigger than b.
     *
     * @param a Larger number
     * @param b Smaller number
     * @return result for a - b
     */
    private static Num subtractInternal(Num a, Num b) {
        int i = 0;
        long[] result = new long[(a.len > b.len) ? a.len : b.len];
        Num resultNum;

        // subtract till both arrays have numbers
        while (i < a.len && i < b.len) {
            if (a.arr[i] >= b.arr[i]) {  // if number in a is bigger, we just subtract and save that to result
                result[i] = a.arr[i] - b.arr[i];
            } else { // if number in b is bigger, we take a carry and then subtract
                a.arr[i] += a.base;
                a.arr[i + 1]--;  // carry is taken from next digit of a
                result[i] = a.arr[i] - b.arr[i];
            }

            i++;
        }

        // copy rest of from longer array to result
        long[] copySource;
        if (a.len < b.len) {
            copySource = b.arr;
        } else {
            copySource = a.arr;
        }

        while (i < copySource.length) {
            result[i] = copySource[i];
            i++;
        }

        resultNum = new Num(result);
        resultNum.arr = removeTrailingZeros(resultNum.arr);
        resultNum.len = resultNum.arr.length;
        resultNum.isNegative = false;

        return resultNum;
    }

    //need to work on optimization, currently O(n^2)
    public static Num product(Num a, Num b) {
        if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for multiplication has to be same");
        }

        Num product;
        int size = a.len + b.len;
        long result[] = new long[size];
        long carry =  0;
        int  i=0, j=0;
        for (long bi : b.arr) {
            carry = 0;
            j = 0;
            for (long aj: a.arr) {
                long num = result[i + j] + carry + (aj * bi);
                carry = num / a.base;
                result[i + j] = num % a.base;
                j++;
            }
            result[i + a.len] = result[i + a.len] + carry;
            i++;
        }

        //updating len of product and negative sign of the product
        product = new Num();
        product.arr = removeTrailingZeros(result);
        product.len = product.arr.length;
        if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative)) {
            product.isNegative = false;
        }
        else {
            product.isNegative = true;
        }

        return product;
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
        for (int k = 0; k <= newSize; k++) {
            result[k] = arr[k];
        }

        return result;
    }

    /**
     * Remove Null elements from the array end and return a new array
     *
     * @param arr Array of string, potentially with some null elements at the end
     * @return A new array which is copy of current array, without any null elements
     */
    private static String[] removeTrailingNulls(String[] arr) {
        int size = arr.length;
        int newSize = size - 1;

        while (arr[newSize].equals(null) && newSize > 0) {
            newSize--;
        }

        // if no nulls to remove
        if (newSize == size - 1) {
            return arr;
        }

        String[] result = new String[newSize + 1];

        for (int k = 0; k <= newSize; k++) {
            result[k] = arr[k];
        }

        return result;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
    	
    	if(n== 0 )
    	{
    		return new Num(1);
    	}
    	
    	Num temp;
    	temp = power(a,n/2);
    	if(n%2 == 0)
    		return(product(temp,temp));
    	else
    		return( product(a, product(temp,temp) ) );
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {

        Num dividend = new Num(a.toString());
        Num divisor = new Num(b.toString());
        boolean flag = false;

        if (dividend.isNegative ^ divisor.isNegative) {
            flag = true; // if this number is negative, return -1. Else return 1.
        }
        dividend.isNegative = false;
        divisor.isNegative = false;

        if (divisor.len == 1 && divisor.arr[0] == 0) {
            return null;
        }

        if (dividend.len < divisor.len || (dividend.len == divisor.len &&
                dividend.arr[dividend.len -1] < divisor.arr[divisor.len - 1])) {
            return new Num(0);
        }

        Num lower = new Num(0);
        Num higher = new Num(dividend.toString());

        Num prevSub = new Num(0);

        while (true) {

            //subtract higher - lower

            Num sub = subtract(higher, lower);
            if (prevSub.compareTo(sub) == 0) {
                lower.isNegative = flag;
                return lower;
            }
            prevSub = sub;

            //get mid  = lower + (higher - lower)/2
            Num mid = add(lower, sub.by2());
            
            //Compare ( divisor * mid - dividend, 0)
            int compareToDM = subtract(product(divisor, mid), dividend).compareTo(new Num(0));

            //if compare returns 0 then mid is quotient
            if (compareToDM == 0) {
                mid.isNegative = flag;
                return mid;
            }
            //else if -1 then mid is lower half
            else if (compareToDM == -1) {
                lower = mid;
            }
            //else mid is in the upper half
            else {
                higher = mid;
            }
        }
    }

    // return a%b
    //Assumption a and b are non negative and b > 0
    //mod return remainder else returns null if b = 0
    public static Num mod(Num a, Num b)
    {
        if (a.isNegative || b.isNegative) {
            throw new ArithmeticException(" Mod function arguments cannot be negative ");
        }
        Num quotient = divide(a, b);
        if ( quotient == null) {
            return null;
        }
        Num product = product(b, quotient);
        product.isNegative = false;
        Num remainder =  subtract(a, product);
        return remainder;
    }

    // Use binary search
    public static Num squareRoot(Num a) throws Exception {
    	
    	if(a.isNegative == true)
    		throw new Exception("No Squareroot for Negative Numbers");
    	
    	if(a.compareTo(new Num("0")) == 0)
    		return(new Num("0"));
    	
    	Num start = new Num("1");
    	
    	//System.out.println(len);

    	//System.out.println(Arrays.toString(start.arr));
    	
    	Num mid,midsq,sum; 
    	
    	Num end = new Num(a.toString());
    	//System.out.println(Arrays.toString(end.arr));
    	Num ans = new Num("-1");
    	
    	int comparision;
    	
    	while(start.compareTo(end) <= 0)
    	{
    		sum = add(start, end);
    		//System.out.println("sum :" + Arrays.toString(sum.arr));
    		mid = sum.by2();
    		//System.out.println("mid :" + Arrays.toString(mid.arr));
    		midsq = product(mid,mid);
    		//System.out.println("midsq :" + Arrays.toString(midsq.arr));
    		
    		comparision = midsq.compareTo(a);
    		
    		if(comparision == 0)
    			return(mid);
    		else if(comparision < 0 )
    		{
    			start = add(mid, new Num(1));
    			//System.out.println("midsq :" + Arrays.toString(start.arr));
    			ans = mid;
    			//System.out.println("midsq :" + Arrays.toString(ans.arr));
    		}
    		else
    		{
    			end = subtract(mid, new Num(1));
    			//System.out.println("midsq :" + Arrays.toString(end.arr));
    		}
    		
    		
    		
    	}
    	
        return ans;
    }



    // Utility functions

    /**
     * Compare this number to other number.
     * <p>
     * This is possible using subtraction as well. But we don't have subtraction and covering the edge cases first
     * should be a little faster.
     *
     * @param other Other number, also a Num
     * @return +1 if this number is greater, 0 if numbers are equal, -1 if other number is greater
     */
    public int compareTo(Num other) {
//         ^ is bitwise XOR.
        if (isNegative ^ other.isNegative) {
            return (isNegative ? -1 : 1);  // if this number is negative, return -1. Else return 1.
        }

        // if we have same base for both
        if (base == other.base) {
            // If length of lists is different:
            //  Case 1: Numbers are positive: bigger list represents bigger number
            //  Case 2: Numbers are negative: smaller list represents bigger number
            if (len != other.len) {
                if (!isNegative) {
                    return (len > other.len ? 1 : -1);
                } else {
                    return (len > other.len ? -1 : 1);
                }
            }

            // If length of lists is same, we compare them starting at the tail. We stop when we find a smaller/larger
            // number and return accordingly
            if (!isNegative) {
                for (int i = len - 1; i >= 0; i--) {
                    if (arr[i] != other.arr[i]) {
                        return (arr[i] > other.arr[i] ? 1 : -1);
                    }
                }
            } else {
                for (int i = len - 1; i >= 0; i--) {
                    if (arr[i] != other.arr[i]) {
                        return (arr[i] > other.arr[i] ? -1 : 1);
                    }
                }
            }
        } else {
            // TODO: Implement different base comparison.
            throw new ArithmeticException("Numbers of different bases given");
        }

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
        BigInteger result, currentMultiplication, currentTerm, currentBase;
        currentBase = BigInteger.valueOf(base);
        result = BigInteger.valueOf(0);

        for (int i = 0; i < arr.length; i++) {
            currentTerm = BigInteger.valueOf(arr[i]);
            currentMultiplication = currentBase.pow(i).multiply(currentTerm);
            result = result.add(currentMultiplication);
        }

        // handle the sign
        StringBuilder output = new StringBuilder();
        if (isNegative) {
            output.append("-");
        }
        output.append(result.toString());
        return output.toString();
    }

    public long base() { return base; }

    public String printNumberByBase() {

        StringBuilder output = new StringBuilder();
        for (int  i = this.len - 1; i >= 0; i--) {
            output.append(evaluate(this.arr[i], this.base));
        }
        return output.toString();
    }

    public static char evaluate(long num, long base) {
//        if (num >= 0 && num <=base) {
//            System.out.println((char)(num + '0'));
////            System.out.println((char)num);
//            return (char)(num + '0');
//        }
//        else if (num >= 10 && num <= 36){
//            System.out.println("data : " + (int)(num-base));
////            System.out.println((char)('A' + (num - base)));
//            return (char)('A' + (num - base));
//        }
//        else if(num >= 37 && num <= 62) {
//            System.out.println("data2 : " + (int)(num - base));
////            System.out.println((char)('a' + (num - base)));
//            return (char)('a' + (num - base));
//        }
//        else{
//            return (char)(num-base);
//        }
        return Character.forDigit((int)num, (int)base);
    }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(int newBase) {

        Num quotient, remainder;
        List<Long> result = new ArrayList<Long>() ;
        Num newBaseNum = new Num(newBase);
        Num input = new Num(this.toString());
        input.isNegative = false;
        while(true) {
            quotient = divide(input, newBaseNum);
            remainder = mod(input, newBaseNum);
            if (quotient == null || remainder == null) {
                throw new ArithmeticException("Some issues with quotient and remainder");
            }
            result.add(Long.parseLong(remainder.toString()));
            if (quotient.toString().equals("0")) {
                break;
            }
            input = quotient;
        }
        Num resultNum = new Num();
        long finalResult[] = new long[result.size()];
        int index = 0;
        for(Long l : result) {
            finalResult[index++] = l;
        }
        resultNum.arr = finalResult;
        resultNum.len = result.size();
        resultNum.base = newBase;
        resultNum.isNegative = this.isNegative;
        return resultNum;
    }

    // Divide by 2, for using in binary search
    public Num by2()
    {
        if (len == 0) {
            return new Num(0);
        }

        long[] arr2 = new long[len];
        Num result = new Num();
        long carry = 0;
        //printList();

        for (int  i = len-1; i >= 0; i--) {
            arr2[i] = (carry * base + arr[i])/2;
            carry = arr[i] % 2;
        }
        result.arr = removeTrailingZeros(arr2);
        result.len = result.arr.length;
        result.isNegative = isNegative;
        return result;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
    	
    	Stack<Num> stack = new Stack<>();

    	
    	System.out.println(expr.length);
    	
    	String regex = "\\d+";
    	Num val1,val2;

        for (String c : expr)
    	{
    		//System.out.println(c);
    		
    		if(c.matches(regex))
    		{
    			stack.push(new Num(c));
    		}
    		else
    		{
    			val1 = stack.pop();
    			val2 = stack.pop();
    			
    			switch(c)
    			{
    				case "+":
    					stack.push(add(val2,val1));
    					break;
    				case "-":
    					stack.push(subtract(val2, val1));
    					break;
    				case "/":
    					stack.push(divide(val2, val1));
    				case "*":
    					stack.push(product(val2, val1));
    					break;
    				case "^":
    					stack.push( power(val2, Long.parseLong(val1.toString())  ) );
    					break;
    			}
    		}
    		
    	}
    	
    	
        return stack.pop();
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) {
        return evaluatePostfix(convertInfixToPostfix(expr));
    }

    /**
     * Accept InFix expression and convert to PostFix.
     *
     * @param expr Expression in infix notation
     * @return Converted expression
     */
    public static String[] convertInfixToPostfix(String[] expr) {
        if (expr.length < 1) {
            throw new ArithmeticException("Empty expression given for evaluation.");
        }

        List<String> result = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push("~"); // dummy operation to determine that we've reached bottom of the stack

        try {
            for (String op : expr) {
                // if token is number, add it to output queue
                if (MathOperations.determineStringType(op).equals(MathOperations.type.NUMBER)) {
                    result.add(op);
                } else if (MathOperations.determineStringType(op).equals(MathOperations.type.OPERATOR)) {
                    if (!stack.peek().equals("~")) {
                        while ((MathOperations.getPrecedence(op) < MathOperations.getPrecedence(stack.peek())
                                || (MathOperations.getPrecedence(op).equals(MathOperations.getPrecedence(stack.peek())) && !op.equals("^")))
                                && (!MathOperations.determineStringType(op).equals(MathOperations.type.LEFTBRACKET))
                        ) {
                            result.add(stack.pop());

                            if (stack.peek().equals("~")) {
                                break;
                            }
                        }
                    }
                    stack.push(op);
                } else if (MathOperations.determineStringType(op).equals(MathOperations.type.LEFTBRACKET)) {
                    stack.push(op);
                } else if (MathOperations.determineStringType(op).equals(MathOperations.type.RIGHTBRACKET)) {
                    while (!stack.peek().equals("(")) {
                        result.add(stack.pop());
                    }
                    stack.pop();
                }
            }


            // if there are more tokens to be read
            if (stack.size() != 1) {
                while (!stack.peek().equals("~")) {
                    result.add(stack.pop());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Invalid expression:, unclosed parenthesis left after end of expression.");
            e.printStackTrace();
            System.exit(0);
        }

        String[] res = new String[result.size()];  // directly using toArray gives Object[], we want String[]
        return result.toArray(res);
    }

    private static class MathOperations {
        private enum type {
            OPERATOR, LEFTBRACKET, RIGHTBRACKET, NUMBER
        }

        /**
         * Decide whether given string is an operator/bracket/number
         *
         * @param str Input string, either one of "*, +, -, /, %, ^, (, )" or a number
         * @return a string containing type of the input string
         */
        static type determineStringType(String str) {
            String[] operators = {"*", "+", "-", "/", "%", "^"};

            if (Arrays.asList(operators).contains(str)) {
                return type.OPERATOR;
            }

            if (str.equals("(")) {
                return type.LEFTBRACKET;
            }

            if (str.equals(")")) {
                return type.RIGHTBRACKET;
            }

            return type.NUMBER;
        }

        /**
         * @param str Input symbol
         * @return An integer value for precedence
         */
        static Integer getPrecedence(String str) {
            switch (str) {
                case "+":
                case "-":
                    return 2;
                case "*":
                case "/":
                case "%":
                    return 3;
                case "^":
                    return 4;
                default:
                    return 0;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String[] arr2 = {"1","3","^","9","+","9","-"};
        
        
        
        Num r = evaluatePostfix(arr2);
        
        System.out.println(r.toString());
        



    }
}