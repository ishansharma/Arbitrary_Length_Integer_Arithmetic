package ixs171130;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LP1DriverTest {

    @Test
    void testDummy() {
        LP1Driver test = new LP1Driver();
        assertEquals("Hello world!", test.dummy());
    }
}