package ixs171130;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumTest {
    @Test
    void testStringConstructorWithDefaultBase() {
        // testing for a multiple of base
        Num x = new Num("10000");
        assertEquals("100: 0 0 1", x.returnListAsString());

        // testing for a (small) non-multiple of the base
        Num y = new Num("567");
        assertEquals("100: 67 5", y.returnListAsString());

        // testing for a (long) non-multiple of the base
        Num z = new Num("45895361142");
        assertEquals("100: 42 11 36 95 58 4", z.returnListAsString());

        // testing for negative number
        Num a = new Num("-1");
        assertEquals("100: 1", a.returnListAsString());
        assertTrue(a.isNegative);

        // testing for a large negative number
        Num b = new Num("-12546731");
        assertEquals("100: 31 67 54 12", b.returnListAsString());
        assertTrue(b.isNegative);

        // testing for zero
        Num c = new Num("0");
        assertEquals("100: 0", c.returnListAsString());

        // long string of zeroes
        Num d = new Num("00000000000");
        assertEquals("100: 0", d.returnListAsString());
    }

    @Test
    void testLongConstructorWithDefaultBase() {
        // testing for a multiple of a base
        Num x = new Num(10000);
        assertEquals("100: 0 0 1", x.returnListAsString());

        // testing for a small non-multiple of base
        x = new Num(57);
        assertEquals("100: 57", x.returnListAsString());
        assertFalse(x.isNegative);

        // testing for a small negative number
        x = new Num(-5);
        assertEquals("100: 5", x.returnListAsString());
        assertTrue(x.isNegative);

        // testing for a large negative number
        x = new Num(-6589423);
        assertEquals("100: 23 94 58 6", x.returnListAsString());
        assertTrue(x.isNegative);

        // testing for 0
        x = new Num(0);
        assertEquals("100: 0", x.returnListAsString());
        assertFalse(x.isNegative);
    }

//    @Test
//    void testStringConstructorExceptions() {
//        // nothing passed
//        assertThrows(ArithmeticException.class, () -> {
//            Num a = new Num("");
//        });
//    }

//    @Test
//    void testProduct() {
//        Num x, y, z;
//
//        x = new Num("123");
//        y = new Num("456");
//        z = Num.product(x, y);
//        assertEquals("100: 88 60 5", z.returnListAsString());
//        assertFalse(z.isNegative);
//
//        x = new Num("0");
//        y = new Num("456");
//        z = Num.product(x, y);
//        assertEquals("100: 0", z.returnListAsString());
//
//        x = new Num("-123");
//        y = new Num("456");
//        z = Num.product(x, y);
//        assertEquals("100: 88 60 5", z.returnListAsString());
//        assertTrue(z.isNegative);
//
//        x = new Num("123");
//        y = new Num("-456");
//        z = Num.product(x, y);
//        assertEquals("100: 88 60 5", z.returnListAsString());
//        assertTrue(z.isNegative);
//
//        x = new Num("-123456789101112");
//        y = new Num("-456784658437654366");
//        z = Num.product(x, y);
//        assertEquals("100: 92 49 25 82 88 13 2 51 97 60 13 24 67 31 39 56", z.returnListAsString());
//        assertFalse(z.isNegative);
//
//        x = new Num("0");
//        y = new Num("0");
//        z = Num.product(x, y);
//        assertEquals("100: 0", z.returnListAsString());
//    }

//    @Test
//    void testCompareTo() {
//        // testing with same numbers
//        Num x, y;
//        x = new Num("24536789456123");
//        y = new Num("24536789456123");
//
//        assertEquals(0, x.compareTo(y));
//
//        // testing with one positive and one negative number
//        x = new Num("24536789456123");
//        y = new Num("-24536789456123");
//        assertEquals(1, x.compareTo(y));
//
//        // testing with one negative and one positive number
//        x = new Num("-24536789456123");
//        y = new Num("24536789456123");
//        assertEquals(-1, x.compareTo(y));
//
//        // testing with both negative numbers
//        x = new Num("-24536789456123");
//        y = new Num("-24536789456123");
//        assertEquals(0, x.compareTo(y));
//
//        // testing when first number is different length
//        x = new Num("1548964165749845");
//        y = new Num("145215");
//        assertEquals(1, x.compareTo(y));
//
//        // testing when second number is different length
//        x = new Num("126453165");
//        y = new Num("458198765165798435198794");
//        assertEquals(-1, x.compareTo(y));
//
//        // testing with different length numbers where one is +ve and one is -ve
//        x = new Num("15894561984651");
//        y = new Num("-5498124984231543516549846");
//        assertEquals(1, x.compareTo(y));
//
//        // just reverse of previous
//        assertEquals(-1, y.compareTo(x));
//
//        // both negative numbers
//        x = new Num("-15489165987");
//        y = new Num("-15489165987");
//        assertEquals(0, x.compareTo(y));
//        assertEquals(0, y.compareTo(x));
//
//        // two zeroes
//        x = new Num("0");
//        y = new Num("0");
//        assertEquals(0, x.compareTo(y));
//        assertEquals(0, y.compareTo(x));
//    }
}