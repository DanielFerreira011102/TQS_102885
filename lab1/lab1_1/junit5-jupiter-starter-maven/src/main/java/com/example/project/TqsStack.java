package com.example.project;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author Daniel Ferreira
 * @version 1.0
 * @since 2023-02-16
 *
 * A stack data structure that uses a LinkedList as its underlying implementation.
 * This stack operates using the "first-in last-out" (FILO) principle.
 *
 * @param <T> the type of elements stored in the stack
 */
public class TqsStack<T> {

	/**
	 * The underlying LinkedList used to store the stack elements.
	 */
	private final LinkedList<T> stack;

	/**
         * The maximum number of elements that can be stored in the stack.
         */
        private final int maxSize;

	/**
	 * Constructs a new empty stack.
	 */
	public TqsStack() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * Constructs a new bounded empty stack.
	 */
	public TqsStack(int maxSize) {
		this.stack = new LinkedList<>();
		this.maxSize = maxSize;
	}

	/**
	 * Adds an element to the top of the stack.
	 *
	 * @param item the element to be added
	 * @throws IllegalStateException if the stack is full
	 */
	public void push(T item) {
		if (stack.size() >= maxSize) {
			throw new IllegalStateException("Stack is full");
		}
		stack.addFirst(item);
	}

	/**
	 * Removes and returns the top element from the stack.
	 *
	 * @return the top element of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	public T pop() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack is empty");
		}
		return stack.removeFirst();
	}

	/**
	 * Returns the top element of the stack without removing it.
	 *
	 * @return the top element of the stack
	 * @throws NoSuchElementException if the stack is empty
	 */
	public T peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("Stack is empty");
		}
		return stack.getFirst();
	}

	/**
	 * Returns the number of elements currently in the stack.
	 *
	 * @return the number of elements in the stack
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * Returns true if the stack is empty, false otherwise.
	 *
	 * @return true if the stack is empty, false otherwise
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
}


