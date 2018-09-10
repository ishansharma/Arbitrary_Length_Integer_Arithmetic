// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

package ixs171130;

import java.util.ArrayList;
import java.util.Arrays;

public class Num  implements Comparable<Num> {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len = 0;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]
//    static final Num MAX_VALUE = new Num("9223372036854775807");

    /**
     * Accepts a string, breaks it in to smaller elements (based on base) and store into arr
     *
     * @params Input string
     */

    public Num()
    {
    }

//    public Num(String s) {
//        arr = new ArrayList<>();
//
//        // if we are given an empty string, throw an exception
//        if (s.length() == 0) {
//            throw new ArithmeticException("Empty string given to constructor. Can't parse as a number");
//        }
//
//        // if number is negative, remove negative sign and mark isNegative true
//        if (s.indexOf("-") == 0) {
//            isNegative = true;
//            s = s.replace("-", "");
//        } else {
//            isNegative = false;
//        }
//
//        // getting number of zeroes in our base, this will only support bases that are power of 10.
//        // doing this for now because otherwise, we need to divide numbers and that's not implemented yet!
//        Integer zeros = ((Long) base).toString().length() - ((Long) base).toString().replace("0", "").length();
//
//        // if only zeroes are passed, store a zero in list
//        if (s.replace("0", "").length() == 0) {
//            arr.add(Long.parseLong("0"));
//        } else {
//            for (int i = s.length(); i > 0; i = i - zeros) {
//                int j = i - zeros;
//
//                // if we go below zero, we will be out of index
//                if (j < 0) {
//                    j = 0;
//                }
//
//                String toAdd = s.substring(j, i);
//
//                // initial zeroes are not needed
//                while (toAdd.indexOf("0") == 0) {
//                    toAdd = toAdd.substring(1);
//                    // don't remove last zero
//                    if (toAdd.length() == 1) {
//                        break;
//                    }
//                }
//
//                arr.add(Long.parseLong(toAdd));
//            }
//        }
//        len = arr.size();
//    }

    public Num(long x) {


    	if(x == 0)
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



    public static Num add(Num a, Num b) {

    	if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for addition has to be same");
        }
    	
    	
    	if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative))
    	{
    		Long sum;
        	Long carry = 0L;
        	
        	int i = 0;
        	int j = 0;

        	Num add = new Num();

        	add.arr = new long[Math.max(a.len,b.len) + 1];

        	int counter = 0;

        	while( i < a.len &&  j < b.len)
        	{
        		
        		sum = a.arr[i] + b.arr[j] + carry;

        		add.arr[counter] = sum%defaultBase;
        		carry = sum/defaultBase;

        		i++;
        		j++;
        		counter++;
        		
        	}
        	
        	while(i < a.len)
        	{
        		sum = a.arr[i] + carry;
        		add.arr[counter] = sum%defaultBase;
        		carry = sum/defaultBase;
        		i++;
        		counter++;

        	}
        	
        	while(j < b.len)
        	{

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

        System.out.println(add.len);

    	System.out.println(Arrays.toString(add.arr));
        	
        return(add);

    	}
    	else
    	{
    		return(subtract(a,b));
    	}
    	

    }

    public static Num subtract(Num a, Num b) {
        return null;
    }

    //need to work on optimization, currently O(n^2)
    public static Num product(Num a, Num b) {
        if (a.base != b.base) {
            throw new ArithmeticException("Bases of two number for multiplication has to be same");
        }

        Num product;
        long result[] = new long[a.len + b.len];
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
        product.arr = result;
        product.len = product.arr.length;
        if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative)) {
            product.isNegative = false;
        }
        else {
            product.isNegative = true;
        }

        //removing trialing zeros are the end of list
//        while(product.len - 1 > 0 && product.arr[product.len -1] == 0) {
//            product.arr.remove(product.len-1);
//            product.len = product.arr.size();
//        }

        return product;
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
        return null;
    }

//    // Use binary search to calculate a/b
//    public static Num divide(Num dividend, Num divisor) {
//        if (divisor.len == 1 && divisor.arr.get(0) == 0) {
//            return Num.MAX_VALUE;
//        }
//
//        if (dividend.len < divisor.len || (dividend.len == divisor.len &&
//                dividend.arr.get(dividend.len -1) < divisor.arr.get(divisor.len - 1))) {
//            return new Num(0);
//        }
//
//        Num lower = new Num(0);
//        Num higher = divisor;
//
//        while (true) {
//
//        }
//
//        return null;
//    }

    // return a%b
    public static Num mod(Num a, Num b) {
        return null;
    }

    // Use binary search
    public static Num squareRoot(Num a) {
        return null;
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
        // ^ is bitwise XOR.
//        if (isNegative ^ other.isNegative) {
//            return (isNegative ? -1 : 1);  // if this number is negative, return -1. Else return 1.
//        }
//
//        // if we have same base for both
//        if (base == other.base) {
//            // If length of lists is different:
//            //  Case 1: Numbers are positive: bigger list represents bigger number
//            //  Case 2: Numbers are negative: smaller list represents bigger number
//            if (len != other.len) {
//                if (!isNegative && !other.isNegative) {
//                    return (len > other.len ? 1 : -1);
//                } else {
//                    return (len < other.len ? -1 : 1);
//                }
//            }
//
//            // If length of lists is same, we compare them starting at the tail. We stop when we find a smaller/larger
//            // number and return accordingly
//            for (int i = len - 1; i > 0; i--) {
//                if (!arr.get(i).equals(other.arr.get(i))) {
//                    return (arr.get(i) > other.arr.get(i) ? 1 : -1);
//                }
//            }
//        } else {
//            // TODO: Implement different base comparison.
//            throw new ArithmeticException("Numbers of different bases given");
//        }

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
    public Num by2()
    {
        if (len == 0) {
            return new Num(0);
        }

        System.out.println(" length : " + len);
        long[] arr2 = new long[len];
        Num result = new Num();
        long carry = 0;

        //need to test for different bases
        for (int  i = len-1; i >= 0; i--) {
            arr2[i] = (carry * base + arr[i])/2;
            carry = arr[i] % 2;
        }
        result.arr = arr2;
        result.len = arr2.length;
        result.isNegative = isNegative;
        return result;
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
        //Num x = new Num(10965);
        //x.printList();


        Num x = new Num(123);
        Num y = new Num(456);

        Num z = product(x, y);
//        z.printList();
//
//
//        x = new Num(0);
//        y = new Num(456);
//        z = product(x, y);
//        z.printList();
//
//        x = new Num(-123);
//        y = new Num(456);
//        z = product(x, y);
//        z.printList();
//        System.out.println(z.isNegative);
//
//        x = new Num(-1234567888);
//        y = new Num(-456676878);
//        z = product(x, y);
//        z.printList();
//        System.out.println(z.isNegative);
//
//        x = new Num(0);
//        y = new Num(0);
//        z = product(x, y);
//        z.printList();
//        System.out.println(z.isNegative);

        x = new Num(34364374);
        z = x.by2();
        z.printList();
        System.out.println(z.isNegative);

    }
}