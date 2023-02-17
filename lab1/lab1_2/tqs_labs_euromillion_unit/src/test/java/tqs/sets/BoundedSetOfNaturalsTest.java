/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.sets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ico0
 */
public class BoundedSetOfNaturalsTest {

    private BoundedSetOfNaturals setA;
    private BoundedSetOfNaturals setB;
    private BoundedSetOfNaturals setC;

    @BeforeEach
    public void setUp() {
        setA = new BoundedSetOfNaturals(2);
        setB = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        setC = BoundedSetOfNaturals.fromArray(new int[]{50, 60});
    }

    @AfterEach
    public void tearDown() {
        setA = setB = setC = null;
    }

    @Test
    public void testAddElement() {
        setA.add(99);
        assertTrue(setA.contains(99), "add: added element not found in set.");
        assertEquals(1, setA.size());

        setA.add(11);
        assertTrue(setA.contains(11), "add: added element not found in set.");
        assertEquals(2, setA.size(), "add: elements count not as expected.");
    }

    @Test
    public void testAddFromBadArray() {
        int[] elems = new int[]{10, -20, -30};

        // must fail with exception
        assertThrows(IllegalArgumentException.class, () -> setA.add(elems));

        // Test adding an array with a negative element
        int[] elems1 = new int[]{10, -20, -30};
        assertThrows(IllegalArgumentException.class, () -> BoundedSetOfNaturals.fromArray(elems1));
    }

    @Test
    public void testAddDuplicates() {
        setA.add(10);
        assertTrue(setA.contains(10));
        assertThrows(IllegalArgumentException.class, () -> setA.add(10));
    }

    @Test
    public void testAddNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> setB.add(-1));
    }

    @Test
    public void testAddBeyondCapacity() {
        setA.add(1);
        setA.add(2);
        assertThrows(IllegalArgumentException.class, () -> setA.add(3));
        assertEquals(2, setA.size(), "add: elements count not as expected.");
    }

    @Test
    public void testContains() {
        assertTrue(setB.contains(50));
        assertFalse(setB.contains(99));
    }

    @Test
    public void testIntersects() {
        assertTrue(setB.intersects(setC));
        assertFalse(setA.intersects(setB));
    }

    @Test
    public void testIterator() {
        int[] expected = {10, 20, 30, 40, 50, 60};
        int i = 0;

        for (int element : setB) {
            assertEquals(expected[i++], element);
        }
    }

    @Test
    public void testEquals() {
        BoundedSetOfNaturals set = BoundedSetOfNaturals.fromArray(new int[]{10, 20, 30, 40, 50, 60});
        assertEquals(setB, set);
        assertNotEquals(setA, setB);
    }
}