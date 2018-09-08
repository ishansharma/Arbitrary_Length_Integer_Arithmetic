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
    void testStringConstructorExceptions() {
        // nothing passed
        assertThrows(ArithmeticException.class, () -> {
            Num a = new Num("");
        });
    }
}