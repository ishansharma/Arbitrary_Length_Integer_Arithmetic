// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

package ixs171130;

import java.util.*;

public class Num implements Comparable<Num> {

    // setting base to 1 followed by 9 zeroes because square root of (2^63 - 1) is 3037000448
    // which has 10 digits. So that's max safe base. Using the nearest power of 10 because
    // that's simpler to represent internally and test
    static long defaultBase = 1000000000L;
    long base = defaultBase;
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len = 0;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    public Num() {
    }

    public Num(Num another)
    {
        //this.defaultBase = another.defaultBase;
        this.base = another.base;
        this.arr = new long[another.arr.length];
        for(int i = 0 ; i < this.arr.length; i++)
            this.arr[i] = another.arr[i];

        this.isNegative = another.isNegative;
        this.len = another.len;
    }
    /**
     * Accepts a string, breaks it in to smaller elements (based on base) and store into arr
     *
     * @param s Input string
     */
    public Num(String s) {
        //check for negative
        int size = s.length();
        isNegative = false;
        if (s.indexOf("-") == 0) {
            isNegative = true;
            size = size -1;
            s = s.replace("-", "");
        } else {
            isNegative = false;
        }

        long[] arr = new long[size];
        if (s.length() == 0) {
            throw new ArithmeticException("Empty string given to constructor. Can't parse as a number");
        }

        int  j= 0;
        for(int i = s.length() -1; i>= 0; i--) {
            arr[j++] = Long.parseLong(s.substring(i, i+1));
        }

        Num base10Number = new Num();
        base10Number.arr = arr;
        base10Number.base = 10;
        base10Number.isNegative = isNegative;
        base10Number.len = size;

        Num result = base10Number.convertBase(defaultBase);
        this.arr = result.arr;
        this.len = result.len;
        this.base = result.base;
        this.isNegative = result.isNegative;

    }

    public Num(long x) {
        if (x == 0)
    	{
    		len = 1;
    		arr = new long[len];
            arr[0] = 0L;
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
     * @param array Array to initialize from
     */
    public Num(long[] array) {
        arr = array;
        len = array.length;
    }


    public static Num add(Num a, Num b) {
        Num result;

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

                add.arr[counter] = sum%a.base;
                carry = sum/a.base;

                i++;
                j++;
                counter++;

            }

            while(i < a.len) {
                sum = a.arr[i] + carry;
                add.arr[counter] = sum%a.base;
                carry = sum/a.base;
                i++;
                counter++;

            }

            while(j < b.len) {

                sum = b.arr[j] + carry;
                add.arr[counter] = sum%a.base;
                carry = sum/a.base;
                j++;
                counter++;

            }

            if(carry > 0 )
                add.arr[counter] = carry;
            else
                add.arr[counter] = 0L;


            add.len = counter;

            if(a.isNegative && b.isNegative)
                add.isNegative = true;

            add.arr = removeTrailingZeros(add.arr);
            add.len = add.arr.length;
            add.base = a.base;
            return(add);

        } else {
            if (a.compareTo(b) > 0) {
                b.isNegative = false;
                result = subtract(a, b);
                b.isNegative = true;
            } else {
                a.isNegative = false;
                result = subtract(a, b);
                a.isNegative = true;
            }
            return result;
        }


    }

    /**
     * Subtract b from a and return a Num
     * @param a First number
     * @param b Second number
     * @return result for a - b
     */
    public static Num subtract(Num a, Num b) {
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
     * Handles the actual subtraction for a - b. a has to be bigger than b.
     *
     * @param a Larger number
     * @param b Smaller number
     * @return result for a - b
     */
    private static Num subtractInternal(Num a, Num b) {
        long[] result = new long[(a.len > b.len) ? a.len : b.len];
        int borrow = 0;
        Num resultNum;

        int i = 0;  // index for arrays
        // subtract till both arrays have numbers
        while (i < a.len && i < b.len) {
            result[i] = a.arr[i] - b.arr[i] - borrow;
            if (result[i] < 0) {
                result[i] += a.base;
                borrow = 1;
            } else {
                borrow = 0;
            }
            i++;
        }

        // copy rest of from longer array to result
        long[] copySource = a.len > b.len ? a.arr : b.arr;

        while (i < copySource.length) {
            result[i] = copySource[i] - borrow;
            if (result[i] < 0) {
                result[i] += a.base;
                borrow = 1;
            } else {
                borrow = 0;
            }
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
        product.base = a.base;
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
        System.arraycopy(arr, 0, result, 0, newSize + 1);

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
    		return(product(a, product(temp,temp) ) );
    }

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {

        Num dividend = new Num(a);
        Num divisor = new Num(b);
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
            Num result = new Num(0);
            result.base = dividend.base;
            return result;
        }

        Num lower = new Num(0);
        lower.base = dividend.base;

        Num higher = new Num(dividend);

        Num prevSub = new Num(0);
        prevSub.base = lower.base;

        while (true) {

            //subtract higher - lower
            Num sub = subtract(higher, lower);
            if (prevSub.compareTo(sub) == 0) {
                lower.isNegative = flag;
                return lower;
            }
            prevSub = sub;

            //get mid  = lower + (higher - lower)/2
            Num by2 = sub.by2();
            Num mid = add(lower, by2);
            
            //Compare ( divisor * mid - dividend, 0)
            Num zero = new Num(0);
            zero.base = dividend.base;
            int compareToDM = subtract(product(divisor, mid), dividend).compareTo(zero);

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
    		throw new Exception("No Square root for Negative Numbers");
    	
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

    public String toString() {
        Num result = this.convertToBase10();
        StringBuilder output = new StringBuilder();
        if (isNegative) {
            output.append("-");
        }
        for (int i = result.arr.length -1; i >= 0; i--) {
            output.append(result.arr[i]);
        }
        return output.toString();
    }

    public long base() { return base; }

    public String printNumberByBase() {
        StringBuilder output = new StringBuilder();
        if (isNegative) {
            output.append("-");
        }
        for (int  i = this.len - 1; i >= 0; i--) {
            output.append(Character.forDigit((int)arr[i], (int)base));
        }
        return output.toString();
    }

    public static Num convertEachBase10(long number) {
        long[] result = new long[10];
        long quotient = number;
        long remainder = number;
        int  i = 0;
        while (quotient != 0) {
            quotient = number / 10;
            remainder = number % 10;
            result[i++] = remainder;
            number = quotient;
        }
        Num resultNum = new Num();
        resultNum.arr = removeTrailingZeros(result);
        resultNum.len = i;
        resultNum.base = 10;
        resultNum.isNegative = false;
        return resultNum;
    }


    public Num convertToBase10() {
        Num base10 = convertEachBase10(base);
        Num result = convertEachBase10(this.arr[this.len -1]);
        for (int i = this.len -1; i > 0; i--) {
            result = product(result, base10);
            result = add(result, convertEachBase10(this.arr[i-1]));
        }
        result.len = result.arr.length;
        result.base =  10;
        return result;
    }

    // Return number equal to "this" number, in base=newBase
    public Num convertBase(long newBase) {
        if (newBase <= 0) {
            return null;
        }
        Num quotient, remainder;
        List<Long> result = new ArrayList<Long>();
        Num newBaseNum = convertEachBase10(newBase);
        Num input = this.convertToBase10();
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
        result.base = this.base;
        result.len = result.arr.length;
        result.isNegative = isNegative;
        return result;
    }

    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) {
    	Stack<Num> stack = new Stack<>();


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
                                && (!MathOperations.determineStringType(op).equals(MathOperations.type.LEFT_BRACKET))
                        ) {
                            result.add(stack.pop());

                            if (stack.peek().equals("~")) {
                                break;
                            }
                        }
                    }
                    stack.push(op);
                } else if (MathOperations.determineStringType(op).equals(MathOperations.type.LEFT_BRACKET)) {
                    stack.push(op);
                } else if (MathOperations.determineStringType(op).equals(MathOperations.type.RIGHT_BRACKET)) {
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
            OPERATOR, LEFT_BRACKET, RIGHT_BRACKET, NUMBER
        }

        /**
         * Decide whether given string is an operator/bracket/number
         *
         * @param str Input string, either one of "*, +, -, /, %, ^, (, )" or a number
         * @return a string containing type of the input string
         */
        static type determineStringType(String str) {
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
//        String[] arr2 = {"1","3","^","9","+","9","-"};
//
//
//
//        Num r = evaluatePostfix(arr2);
//
//        System.out.println(r.toString());

        Num x = new Num(-1234567);
        Num y = x.convertBase(16);
//        String data = y.toStringTest();
//        System.out.println(data);
//
//        Num z = y.convertBase(8);
//        System.out.println(z.printNumberByBase());
//        data = y.toStringTest();
//        System.out.println(data);;
//
//        z = z.convertBase(16);
//        System.out.println(z.printNumberByBase());
//        data = y.toStringTest();
//        System.out.println(data);
//
//        z = z.convertBase(10);
//        System.out.println(z.printNumberByBase());
//        data = y.toStringTest();
//        System.out.println(data);

//        Num result = x.convertToBase10();
//        System.out.println(result.printNumberByBase());
////        System.out.print(result);
//        Num z = y.convertBase(8);
//        System.out.println(z.printNumberByBase());
//        y = convertEachBase10(1234567);
//        System.out.println(y.printNumberByBase());

//        Num r = stringConstructor("-12345678912345678");
//        System.out.print("r : "+ r.toString());
    }
}