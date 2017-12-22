package ru.otus.l031;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class MyArrayList<E> implements List<E> {

	private static final Object[] EMPTY_LIST = {};
	private static final int CAPACITY_INCREMENT = 10;
	
	private Object[] elements;
	private int size;
	
	public MyArrayList() {
		elements = EMPTY_LIST;
	}
	
	public MyArrayList(int capacity) {
		if (capacity > 0) {
			elements = new Object[capacity];
		} else if (capacity == 0) {
			elements = EMPTY_LIST;
		} else {
			throw new IllegalArgumentException("Illegal capacity: " + capacity);
		}
	}
	
	public MyArrayList(Collection<? extends E> c) {
		elements = c.toArray();
		size = elements.length;
	}
	
	private void checkIndex(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
		}
	}
	
	private void increaseCapacity(int newCapacity) {
		elements = Arrays.copyOf(elements, newCapacity);
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		boolean result = false;
		int i = 0;
		while (i < size && !result) {
			result = (o == null ? elements[i] == null : o.equals(elements[i]));
			i++;
		}
		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return new MyListIterator();
	}

	@Override
	public Object[] toArray() {
		Object[] copy = new Object[size];
		
		for (int i = 0; i < copy.length; i++) {
			copy[i] = elements[i];
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		T[] copy = a.length < size ? Arrays.copyOf(a, size) : a;
				
		for (int i = 0; i < size; i++) {
			copy[i] = (T) elements[i];
		}

		if (a.length > size) {
			for (int i = size; i < a.length; i++) {
				copy[i] = null;
			}
		}	
		return copy;
	}

	@Override
	public boolean add(E e) {
		if (size == elements.length) {
			increaseCapacity(size + CAPACITY_INCREMENT);
		}
		elements[size++] = e;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++) {
				if (elements[i] == null) {
					remove(i);
					return true;
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (o.equals(elements[i])) {
					remove(i);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c.size() == 0) {
			return false;
		}
		
		Object[] containElements = c.toArray();
		for (int i = 0; i < containElements.length; i++) {
			if (!contains(containElements[i])) {
				return false;
			};
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.size() == 0) {
			return false;
		}
		
		Object[] addElements = c.toArray();
		
		int capacity = elements.length;
		int newCapacity = size + addElements.length;
		
		if (capacity < newCapacity) {
			increaseCapacity(newCapacity);
		}
		for (int i = 0; i < addElements.length; i++) {
			elements[i + size] = addElements[i];
		}
		size += addElements.length;
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		checkIndex(index);
		
		if (c.size() == 0) {
			return false;
		}
		
		Object[] addElements = c.toArray();
		
		int capacity = elements.length;
		int newCapacity = size + addElements.length;
		
		if (capacity < newCapacity) {
			increaseCapacity(newCapacity);
		}
		for (int i = size - 1; i >= index; i--) {
			elements[i + addElements.length] = elements[i];
		}
		for (int i = 0; i < addElements.length; i++) {
			elements[i + index] = addElements[i];
		}
		size += addElements.length;
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (c.size() == 0) {
			return false;
		}

		boolean result = false;
		
		Object[] removeElements = c.toArray();
		for (int i = 0; i < removeElements.length; i++) {
			while (remove(removeElements[i])) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int oldSize = size;
		
		if (c.size() == 0) {
			clear();
		} else {
			int i = 0;
			while (i < size) {
				if (!c.contains(elements[i])) {
					remove(i);
				} else {
					i++;
				}
			}	
		}
	    return (oldSize != size);	
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		checkIndex(index);
		return (E) elements[index];
	}

	@SuppressWarnings("unchecked")
	@Override
	public E set(int index, E element) {
		checkIndex(index);
		E oldValue = (E) elements[index];
		elements[index] = element;
		return oldValue;
	}

	@Override
	public void add(int index, E element) {
		checkIndex(index);
		elements = Arrays.copyOf(elements, size + 1);
		for (int i = size; i > index; i--) {
			elements[i] = elements[i - 1];
		}
		elements[index] = element;
		size++;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		checkIndex(index);
		E element = (E) elements[index];
		for (int i = index; i < size - 1; i++) {
			elements[i] = elements[i + 1];
		}
		elements[--size] = null;
		return element;
	}

	@Override
	public int indexOf(Object o) {
		boolean result = false;
		int i = 0;
		while (i < size && !result) {
			result = (o == null ? elements[i] == null : o.equals(elements[i]));
			i++;
		}		
		return (result ? i - 1 : -1);
	}

	@Override
	public int lastIndexOf(Object o) {
		if (o == null) {
			for (int i = size - 1; i >= 0; i--) {
				if (elements[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = size - 1; i >= 0; i--) {
				if (o.equals(elements[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new MyListIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new MyListIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("subList");
	}

	private class MyListIterator implements ListIterator<E> {
		
		int cursor;
		int lastRet = -1;
		
		MyListIterator() {
			
		}

		MyListIterator(int index) {
			cursor = index;
		}
		
		@Override
		public boolean hasNext() {
			return cursor != size;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E next() {
			int i = cursor;
			if (i >= size) {
				throw new NoSuchElementException();
			}
			Object[] elements = MyArrayList.this.elements;
			cursor = i + 1;
			lastRet = i;
			return (E) elements[i];
		}
		
		@Override
		public void remove() {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			MyArrayList.this.remove(lastRet);	
			cursor = lastRet;
			lastRet = -1;
		}
		
		@Override
		public boolean hasPrevious() {
			return cursor != 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E previous() {
			int i = cursor - 1;
			if (i < 0) {
				throw new NoSuchElementException();
			}
			Object[] elements = MyArrayList.this.elements;
			cursor = i;
			lastRet = i;
			return (E) elements[i];
		}

		@Override
		public int nextIndex() {
			return cursor;
		}

		@Override
		public int previousIndex() {
			return cursor - 1;
		}


		@Override
		public void set(E e) {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			MyArrayList.this.set(lastRet, e);	
		}

		@Override
		public void add(E e) {
			int i = cursor;
			MyArrayList.this.add(i, e);
			cursor = i + 1;
			lastRet = -1;			
		}
		
	}
}
