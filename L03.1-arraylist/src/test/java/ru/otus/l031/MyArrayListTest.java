package ru.otus.l031;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyArrayListTest {

	List<String> myList;
	List<String> myCopyList;

	@Before
	public void setUp() throws Exception {
		myList = new MyArrayList<>(Arrays.asList("one", "two", "three", "four"));
		myCopyList = new MyArrayList<>();
	}

	@After
	public void tearDown() throws Exception {
		myList = null;
		myCopyList = null;
	}

	@Test
	public void testSize() {
		assertEquals(4, myList.size());
		assertEquals(0, myCopyList.size());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(myCopyList.isEmpty());
		assertFalse(myList.isEmpty());
	}

	@Test
	public void testContains() {
		assertTrue(myList.contains("one"));
	}

	@Test
	public void testIterator() {
		Iterator<String> iterator = myList.iterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
	}

	@Test
	public void testToArray() {
		Object[] myArray = myList.toArray();
		assertEquals("one", myArray[0]);
		assertEquals("two", myArray[1]);
		assertEquals("three", myArray[2]);
		assertEquals("four", myArray[3]);
	}

	@Test
	public void testToArrayTArray() {
		String[] myArray = myList.toArray(new String[0]);
		assertEquals("one", myArray[0]);
		assertEquals("two", myArray[1]);
		assertEquals("three", myArray[2]);
		assertEquals("four", myArray[3]);
	}

	@Test
	public void testAddE() {
		assertEquals(4, myList.size());
		myList.add("five");
		assertEquals(5, myList.size());
		assertEquals("five", myList.get(4));
	}

	@Test
	public void testRemoveObject() {
		assertEquals("two", myList.get(1));
		myList.remove("two");
		assertEquals("three", myList.get(1));
	}

	@Test
	public void testContainsAll() {
		myCopyList.add("one");
		myCopyList.add("three");
		assertTrue(myList.containsAll(myCopyList));
	}

	@Test
	public void testAddAllCollectionOfQextendsE() {
		myCopyList.add("five");
		myCopyList.add("six");
		myList.addAll(myCopyList);
		assertEquals(6, myList.size());
		assertEquals("six", myList.get(5));
	}

	@Test
	public void testAddAllIntCollectionOfQextendsE() {
		myCopyList.add("one_and_a_half");
		myCopyList.add("one_three_quarters");
		myList.addAll(1, myCopyList);
		assertEquals(6, myList.size());
		assertEquals(1, myList.indexOf("one_and_a_half"));
		assertEquals("two", myList.get(3));
	}

	@Test
	public void testRemoveAll() {
		myCopyList.add("one");
		myCopyList.add("three");
		assertTrue(myList.removeAll(myCopyList));
		assertEquals(2, myList.size());
		assertEquals("two", myList.get(0));
		assertEquals("four", myList.get(1));
	}

	@Test
	public void testRetainAll() {
		myCopyList.add("one");
		myCopyList.add("three");
		assertTrue(myList.retainAll(myCopyList));
		assertEquals(2, myList.size());
		assertEquals("one", myList.get(0));
		assertEquals("three", myList.get(1));
	}

	@Test
	public void testClear() {
		assertEquals(4, myList.size());
		myList.clear();
		assertEquals(0, myList.size());
	}

	@Test
	public void testGet() {
		assertEquals("one", myList.get(0));
		assertEquals("two", myList.get(1));
	}

	@Test
	public void testSet() {
		assertEquals("one", myList.get(0));
		myList.set(0, "ten");
		assertEquals("ten", myList.get(0));
	}

	@Test
	public void testAddIntE() {
		assertEquals(4, myList.size());
		myList.add(0, "five");
		assertEquals(5, myList.size());
		assertEquals("five", myList.get(0));
	}

	@Test
	public void testRemoveInt() {
		assertEquals(4, myList.size());
		myList.remove(0);
		assertEquals(3, myList.size());
		assertEquals("two", myList.get(0));
	}

	@Test
	public void testIndexOf() {
		assertEquals(0, myList.indexOf("one"));
	}

	@Test
	public void testLastIndexOf() {
		assertEquals(3, myList.lastIndexOf("four"));
		myList.add("four");
		assertEquals(4, myList.lastIndexOf("four"));
	}

	@Test
	public void testListIterator() {
		ListIterator<String> iterator = myList.listIterator();
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		assertFalse(iterator.hasPrevious());
		iterator.next();
		assertTrue(iterator.hasPrevious());
		assertTrue(iterator.hasNext());
	}

	@Test
	public void testListIteratorInt() {
		ListIterator<String> iterator = myList.listIterator(3);
		assertNotNull(iterator);
		assertEquals(3, iterator.nextIndex());
		iterator.next();
		assertFalse(iterator.hasNext());
		assertTrue(iterator.hasPrevious());
		iterator.previous();
		assertTrue(iterator.hasPrevious());
		assertTrue(iterator.hasNext());
	}

	@Test
	public void testSubList() {
		assertNotNull(myList.subList(0, myList.size()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubListInvalidRange() {
		myList.subList(myList.size(), 0);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testSubListInvalidIndex() {
		myList.subList(-1, myList.size());
	}
}
