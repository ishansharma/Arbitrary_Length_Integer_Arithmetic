// Starter code for lp1.
// Version 1.0 (8:00 PM, Wed, Sep 5).

package ixs171130;

import java.util.ArrayList;
import java.util.List;

public class Num {

    static long defaultBase = 100;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**
     * Accepts a string, breaks it in to smaller elements (based on base) and store into arr
     *
     * @params Input string
     */

    public Num() {
        len = 0;
        isNegative = false;
    }

    public Num(String s) {
        if (s.indexOf("-") == 0) {
            isNegative = true;
            s = s.replace("-", "");
        } else {
            isNegative = false;
        }

        int size;
        size = 1 + (s.length() / (((Long) base).toString().length() - 1));

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
                while (toAdd.indexOf("0") == 0) {
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


//	public static<Long> Long next(Iterator<Long> it)
//    	{
//    		if(it.hasNext())
//    		{
//    			return(it.next());
//    		}
//    		else
//    		{
//    			return(null);
//    		}
//
//   	 }

//    public static Num add(Num a, Num b) {
//
//        if (a.base != b.base) {
//            throw new ArithmeticException("Bases of two number for addition has to be same");
//        }
//
//
//    	if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative)) {
//
//    		Iterator<Long> it1 = a.arr.iterator();
//        	Iterator<Long> it2 = b.arr.iterator();
//
//        	 List<Long> outList = new ArrayList<>();
//
//        	 Num add = new Num(0);
//
//        	Long x1 =  next(it1);
//        	Long x2 =  next(it2);
//
//        	Long sum;
//        	Long carry = 0L;
//
//        	while(x1 != null && x2 != null)
//        	{
//
//        		sum = x1 + x2 + carry;
//        		add.arr.add(sum%defaultBase);
//        		carry = sum/defaultBase;
//        		x1  = next(it1);
//        		x2 = next(it2);
//
//        	}
//
//        	while(x1 != null)
//        	{
//        		sum = x1 + carry;
//        		add.arr.add(sum%defaultBase);
//        		carry = sum/defaultBase;
//        		x1 = next(it1);
//        	}
//
//        	while(x2 != null)
//        	{
//        		sum = x2 + carry;
//        		add.arr.add(sum%defaultBase);
//        		carry = sum/defaultBase;
//        		x2 = next(it2);
//        	}
//
//        	if(carry > 0 )
//        		add.arr.add(carry);
//
//
//        	add.len = add.arr.size();
//
//        	if(a.isNegative && b.isNegative)
//        	{
//        		add.isNegative = true;
//        	}
//
//            return add;
//
//        }
//    	else
//    	{
//    		return(subtract(a,b));
//    	}
//
//    }

//    public static Num subtract(Num a, Num b) {
//        return null;
//    }

    //need to work on optimization, currently O(n^2)
//    public static Num product(Num a, Num b) {
//        if (a.base != b.base) {
//            throw new ArithmeticException("Bases of two number for multiplication has to be same");
//        }
//        Num product = new Num("0");
//        long carry =  0;
//        int  i=0, j=0;
//        for (long bi : b.arr) {
//            carry = 0;
//            j = 0;
//            for (long aj: a.arr) {
//                if (product.arr.size() < i + j + 1) {
//                    product.arr.add(i+j, 0l);
//                }
//                long num = product.arr.get(i + j) + carry + (aj * bi);
//                carry = num / a.base;
//                product.arr.set(i + j, num % a.base);
//                j++;
//            }
//            if (product.arr.size() < i + a.len + 1) {
//                product.arr.add(i + a.len, 0l);
//            }
//            product.arr.set(i + a.len , product.arr.get(i + a.len) + carry);
//            i++;
//        }
//
//        //updating len of product and negative sign of the product
//        product.len = product.arr.size();
//        if(a.isNegative && b.isNegative || (!a.isNegative && !b.isNegative)) {
//            product.isNegative = false;
//        }
//        else {
//            product.isNegative = true;
//        }
//
//        //removing trialing zeros are the end of list
//        while(product.len - 1 > 0 && product.arr.get(product.len -1) == 0) {
//            product.arr.remove(product.len-1);
//            product.len = product.arr.size();
//        }
//        return product;
//    }

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

//    /**
//     * Compare this number to other number.
//     * <p>
//     * This is possible using subtraction as well. But we don't have subtraction and covering the edge cases first
//     * should be a little faster.
//     *
//     * @param other Other number, also a Num
//     * @return +1 if this number is greater, 0 if numbers are equal, -1 if other number is greater
//     */
//    public int compareTo(Num other) {
//        // ^ is bitwise XOR.
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
//
//        return 0;
//    }

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
            if (n != null) {
                output.append(n.toString()).append(" ");
            }
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
//        Num x = new Num(10965);
//        x.printList();
    }
}