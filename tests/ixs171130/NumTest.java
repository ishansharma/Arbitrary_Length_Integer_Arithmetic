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

        // large number with single digits in middle
        b = new Num("1015578");
        assertEquals("100: 78 55 1 1", b.returnListAsString());
        assertEquals("1015578", b.toString());
        assertFalse(b.isNegative);

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

    @Test
    void testStringConstructorExceptions() {
        // nothing passed
        assertThrows(ArithmeticException.class, () -> {
            Num a = new Num("");
        });
    }

    @Test
    void testAdd() {
        Num x, y, result;

        // add two zeroes
        x = new Num(0);
        y = new Num(0);
        result = Num.add(x, y);
        assertEquals("100: 0", result.returnListAsString());
        assertFalse(result.isNegative);

        // add two small numbers
        x = new Num(123);
        y = new Num("4567");
        result = Num.add(x, y);
        assertEquals("4690", result.toString());
        assertFalse(result.isNegative);

        // add two large numbers
        x = new Num("15248695712464651165");
        y = new Num("125485784");
        result = Num.add(x, y);
        assertEquals("15248695712590136949", result.toString());
        assertFalse(result.isNegative);
    }

    @Test
    void testSubtractionWhenNumbersAreEqual() {
        Num x, y, result;

        // subtract 0 from 0
        x = new Num(0);
        y = new Num("0");
        result = Num.subtract(x, y);
        assertEquals("100: 0", result.returnListAsString());

        // subtract two equal +ve numbers
        x = new Num(100);
        y = new Num("100");
        result = Num.subtract(x, y);
        assertEquals("100: 0", result.returnListAsString());

        // subtract two equal -ve numbers
        x = new Num("-123456789");
        y = new Num(-123456789);
        result = Num.subtract(x, y);
        assertEquals("100: 0", result.returnListAsString());
    }

    @Test
    void testSubtractionWhenXIsGreaterThanY() {
        Num x, y, result;

        // testing when y is zero
        x = new Num("460");
        y = new Num(0);
        result = Num.subtract(x, y);
        assertEquals("460", result.toString());
        assertFalse(result.isNegative);

        // when x is positive and y is negative, with a smaller number
        x = new Num("5");
        y = new Num("-3");
        result = Num.subtract(x, y);
        assertEquals("100: 8", result.returnListAsString());

        // when x is positive and y is negative, with a large number
        x = new Num("15248597586485754648891564");
        y = new Num("-48915198495619845641984");
        result = Num.subtract(x, y);  // 15297512784981374494533548
        assertEquals("15297512784981374494533548", result.toString());
        assertFalse(result.isNegative);

        // when x and y both are negative
        x = new Num("-15");
        y = new Num("-30");
        result = Num.subtract(x, y);
        assertEquals("15", result.toString());
        assertFalse(result.isNegative);

        // when x and y are both negative, differ by 1
        x = new Num("-16");
        y = new Num("-17");
        result = Num.subtract(x, y);
        assertEquals("1", result.toString());
        assertFalse(result.isNegative);

        // when x and y both are negative, large numbers
        x = new Num("-152485618");
        y = new Num("-15949549846514987951598798484987");
        result = Num.subtract(x, y);
        assertEquals("15949549846514987951598645999369", result.toString());
        assertFalse(result.isNegative);

        // when x and y are both positive
        x = new Num("15249846519841321984984");
        y = new Num("15489156949841897");
        result = Num.subtract(x, y);
        assertEquals("15249831030684372143087", result.toString());
        assertFalse(result.isNegative);

        // x, y both positive, with smaller number
        x = new Num(10);
        y = new Num(9);
        result = Num.subtract(x, y);
        assertEquals("1", result.toString());
        assertFalse(result.isNegative);
    }

    @Test
    void testSubtractionWhenYIsGreaterThanX() {
        Num x, y, result;

        // testing when x is zero
        x = new Num(0);
        y = new Num(460);
        result = Num.subtract(x, y);
        assertEquals("-460", result.toString());
        assertTrue(result.isNegative);

        // with a smaller number
        x = new Num(-5);
        y = new Num("3");
        result = Num.subtract(x, y);
        assertEquals("-8", result.toString());
        assertTrue(result.isNegative);

        // with large numbers
        x = new Num("-54787498494159798465456465498168798165498465198798465");
        y = new Num("549879851894548978951987891489789459549545594554564");
        result = Num.subtract(x, y);
        assertEquals("-55337378346054347444408453389658587625048010793353029", result.toString());
        assertTrue(result.isNegative);

        // when both are positive
        x = new Num(10);
        y = new Num(20);
        result = Num.subtract(x, y);
        assertEquals("-10", result.toString());
        assertTrue(result.isNegative);

        // when both are positive, large numbers
        x = new Num("4854894654984651978964598798444");
        y = new Num("65469849468489484749654897894151654651654654656");
        result = Num.subtract(x, y);
        assertEquals("-65469849468489479894760242909499675687055856212", result.toString());
        assertTrue(result.isNegative);

        // edge case
        x = new Num(685050);
        y = new Num(1234567);
        result = Num.subtract(x, y);
        assertEquals("-549517", result.toString());
        assertTrue(result.isNegative);

        // when both are positive, with only 1 digit more in second
        x = new Num("4564984351654321894132165743516546546");
        y = new Num("45649843516543218941321657435165465462");
        result = Num.subtract(x, y);
        assertEquals("-41084859164888897047189491691648918916", result.toString());
        assertTrue(result.isNegative);

        // when both are negative
        x = new Num("-4519816518498165496546");
        y = new Num("-959849465");
        result = Num.subtract(x, y);
        assertEquals("-4519816518497205647081", result.toString());

        // when both are negative, small difference
        x = new Num("1234567");
        y = new Num("925925");
        result = Num.subtract(x, y);
        assertEquals("308642", result.toString());
        //result.printList();
        //System.out.println(" len " + result.len);
        assertFalse(result.isNegative);
    }

    @Test
    void testProduct() {
        Num x, y, z;

        x = new Num("123");
        y = new Num("456");
        z = Num.product(x, y);
        assertEquals("56088", z.toString());
        assertFalse(z.isNegative);

        x = new Num("0");
        y = new Num("456");
        z = Num.product(x, y);
        assertEquals("100: 0", z.returnListAsString());

        x = new Num("-123");
        y = new Num("456");
        z = Num.product(x, y);
        assertEquals("-56088", z.toString());
        assertTrue(z.isNegative);

        x = new Num("12");
        y = new Num("-456");
        z = Num.product(x, y);
        assertEquals("-5472", z.toString());
        assertTrue(z.isNegative);

        x = new Num("-123456789101112");
        y = new Num("-456784658437654366");
        z = Num.product(x, y);
        assertEquals("56393167241360975102138882254992", z.toString());
        assertFalse(z.isNegative);

        x = new Num("4567");
        y = new Num("1080246");
        z = Num.product(x, y);
        assertEquals("4933483482", z.toString());
    }

    @Test
    void testCompareTo() {
        // testing with same numbers
        Num x, y;
        x = new Num("24536789456123");
        y = new Num("24536789456123");

        assertEquals(0, x.compareTo(y));

        // testing with one positive and one negative number
        x = new Num("24536789456123");
        y = new Num("-24536789456123");
        assertEquals(1, x.compareTo(y));

        // testing with one negative and one positive number
        x = new Num("-24536789456123");
        y = new Num("24536789456123");
        assertEquals(-1, x.compareTo(y));

        // testing with both negative numbers
        x = new Num("-24536789456123");
        y = new Num("-24536789456123");
        assertEquals(0, x.compareTo(y));

        // testing when first number is different length
        x = new Num("1548964165749845");
        y = new Num("145215");
        assertEquals(1, x.compareTo(y));

        // testing when second number is different length
        x = new Num("126453165");
        y = new Num("458198765165798435198794");
        assertEquals(-1, x.compareTo(y));

        // testing with different length numbers where one is +ve and one is -ve
        x = new Num("15894561984651");
        y = new Num("-5498124984231543516549846");
        assertEquals(1, x.compareTo(y));

        // just reverse of previous
        assertEquals(-1, y.compareTo(x));

        // both negative numbers
        x = new Num("-15489165987");
        y = new Num("-15489165987");
        assertEquals(0, x.compareTo(y));
        assertEquals(0, y.compareTo(x));

        // two zeroes
        x = new Num("0");
        y = new Num("0");
        assertEquals(0, x.compareTo(y));
        assertEquals(0, y.compareTo(x));

        // two negative numbers smaller than base
        x = new Num("-15");
        y = new Num("-30");
        assertEquals(1, x.compareTo(y));
        assertEquals(-1, y.compareTo(x));

        // one positive, one negative. Both smaller than base
        x = new Num("15");
        y = new Num("-30");
        assertEquals(1, x.compareTo(y));
        assertEquals(-1, y.compareTo(x));

        x = new Num("-152485618");
        y = new Num("-15949549846514987951598798484987");
        assertEquals(1, x.compareTo(y));
        assertEquals(-1, y.compareTo(x));
    }

    @Test
    void testToString() {
        // zero!
        Num x = new Num(0);
        assertEquals("0", x.toString());

        // negative number
        x = new Num("-15981587984132549845216879841321063846321068949684");
        assertEquals("-15981587984132549845216879841321063846321068949684", x.toString());

        // a large random number
        x = new Num("1123446891891879512198946152197987494");
        assertEquals("1123446891891879512198946152197987494", x.toString());
    }

    @Test
    void testBy2() {
        Num x, y, z;

        x = new Num("1234567891234567891230");
        assertEquals("617283945617283945615" , x.by2().toString());

        x = new Num(0);
        assertEquals("0", x.by2().toString());

        x = new Num("-256");
        assertEquals("-128", x.by2().toString());
    }

    @Test
    void testDivide() {
        Num x, y, z;

        x = new Num("123456789");
        y = new Num("4567");
        assertEquals("27032", Num.divide(x, y).toString());

        x = new Num("-123456789");
        y = new Num("4567");
        assertEquals("-27032", Num.divide(x, y).toString());

        x = new Num("123456789");
        y = new Num("-4567");
        assertEquals("-27032", Num.divide(x, y).toString());

        x = new Num("-123456789");
        y = new Num("-4567");
        assertEquals("27032", Num.divide(x, y).toString());

        x = new Num("0");
        y = new Num("-4567");
        assertEquals("0", Num.divide(x, y).toString());

        x = new Num("-4567");
        y = new Num("0");
        assertEquals(null, Num.divide(x, y));

        x = new Num("0");
        y = new Num("0");
        assertEquals(null, Num.divide(x, y));

    }

    @Test
    void testMod() {
        Num x, y, z;

        x = new Num("123456789");
        y = new Num("4567");
        assertEquals("1645", Num.mod(x, y).toString());

        x = new Num("0");
        y = new Num("4567");
        assertEquals("0", Num.mod(x, y).toString());

        x = new Num("4567");
        y = new Num("0");
        assertEquals(null, Num.mod(x, y));

        x = new Num("123456789123456789123456789123456789123456789");
        y = new Num("123456789");
        assertEquals("0", Num.mod(x, y).toString());

        x = new Num("123456789123456789123456789123456789123456789");
        y = new Num("1234567891");
        assertEquals("781866112", Num.mod(x, y).toString());
    }

    @Test
    void testConvertBase() {

        Num x = new Num(1234);
        Num y = x.convertBase(10);
        assertEquals("1234", y.printNumberByBase());

        y = y.convertBase(16);
        assertEquals("4d2", y.printNumberByBase());

        y = y.convertBase(10);
        assertEquals("1234", y.printNumberByBase());

        x = new Num(12365);
        y = x.convertBase(8);
        assertEquals("30115", y.printNumberByBase());

        y = y.convertBase(10);
        assertEquals("12365", y.printNumberByBase());
    }
}