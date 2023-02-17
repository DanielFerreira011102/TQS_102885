import com.example.project.TqsStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-16
 */
@DisplayName("A JUnit test suite for the TqsStack class.")
public class TqsStackTest {

    @Nested
    @DisplayName("Basic Tests")
    class BasicTests {
        @Test
        @DisplayName("Tests the push and peek methods")
        public void testPushAndPeek() {
            TqsStack<String> stack = new TqsStack<>();
            stack.push("one");
            stack.push("two");
            stack.push("three");
            assertEquals("three", stack.peek());  // top element should be "three"
            assertEquals(3, stack.size());  // stack should have 3 elements
        }

        @Test
        @DisplayName("Tests the pop method")
        public void testPop() {
            TqsStack<String> stack = new TqsStack<>();
            stack.push("one");
            stack.push("two");
            stack.push("three");
            assertEquals("three", stack.pop());  // top element should be removed and returned
            assertEquals("two", stack.peek());  // new top element should be "two"
            assertEquals(2, stack.size());  // stack should have 2 elements
        }

        @Test
        @DisplayName("Tests the size method")
        public void testSize() {
            TqsStack<String> stack = new TqsStack<>();
            assertEquals(0, stack.size());  // stack should be empty
            stack.push("one");
            assertEquals(1, stack.size());  // stack should have 1 element
            stack.push("two");
            assertEquals(2, stack.size());  // stack should have 2 elements
        }

        @Test
        @DisplayName("Tests the isEmpty method")
        public void testIsEmpty() {
            TqsStack<String> stack = new TqsStack<>();
            assertTrue(stack.isEmpty());  // stack should be empty
            stack.push("one");
            assertFalse(stack.isEmpty());  // stack should not be empty
            stack.pop();
            assertTrue(stack.isEmpty());  // stack should be empty again
        }

        @Test
        @DisplayName("Popping from an empty stack does throw a NoSuchElementException")
        public void testPopEmptyStack() {
            TqsStack<Double> stack = new TqsStack<>();
            assertThrows(NoSuchElementException.class, stack::pop);  // pop should throw an exception
        }

        @Test
        @DisplayName("Peeking into an empty stack does throw a NoSuchElementException")
        public void testPeekEmptyStack() {
            TqsStack<String> stack = new TqsStack<>();
            assertThrows(NoSuchElementException.class, stack::peek);  // peek should throw an exception
        }
    }

    @Nested
    @DisplayName("Other Tests")
    class OtherTests {
        @Test
        @DisplayName("A stack is empty on construction")
        public void testEmptyOnConstruction() {
            TqsStack<Integer> stack = new TqsStack<>();
            assertTrue(stack.isEmpty());  // stack should be empty
        }

        @Test
        @DisplayName("A stack has size 0 on construction")
        public void testSizeOnConstruction() {
            TqsStack<Integer> stack = new TqsStack<>();
            assertEquals(0, stack.size());  // stack should have size 0
        }

        @Test
        @DisplayName("Pushing n elements to an empty stack results in a non-empty stack of size n")
        public void testPushNElements() {
            TqsStack<Integer> stack = new TqsStack<>();
            int n = 3;
            for (int i = 1; i <= n; i++) {
                stack.push(i);
                assertFalse(stack.isEmpty());  // stack should not be empty
                assertEquals(i, stack.size());  // stack should have size i
            }
        }

        @Test
        @DisplayName("Popping after pushing x returns x")
        public void testPushThenPop() {
            TqsStack<String> stack = new TqsStack<>();
            String x = "foo";
            stack.push(x);
            assertEquals(x, stack.pop());  // x should be "foo"
        }

        @Test
        @DisplayName("Peeking after pushing x returns x, but size stays the same")
        public void testPushThenPeek() {
            TqsStack<String> stack = new TqsStack<>();
            String x = "bar";
            stack.push(x);
            assertEquals(x, stack.peek());  // x should be "bar"
            assertEquals(1, stack.size());  // stack should have size 1
        }

        @Test
        @DisplayName("Popping n times from a stack of size n results in an empty stack of size 0")
        public void testPopNElements() {
            TqsStack<Integer> stack = new TqsStack<>();
            int n = 3;
            for (int i = 1; i <= n; i++) {
                stack.push(i);
            }
            for (int i = n; i > 0; i--) {
                assertFalse(stack.isEmpty());  // stack should not be empty
                assertEquals(i, stack.size());  // stack should have size i
                stack.pop();
            }
            assertTrue(stack.isEmpty());  // stack should be empty
            assertEquals(0, stack.size());  // stack should have size 0
        }

        @Test
        @DisplayName("Pushing onto a full stack throws an IllegalStateException")
        public void testPushToFullStack() {
            TqsStack<Integer> stack = new TqsStack<>(2);
            stack.push(1);
            stack.push(2);
            assertThrows(IllegalStateException.class, () -> stack.push(3));  // push should throw an exception
        }
    }
}
