package com.projectkorra.core.system.ability;

public class AbilityInstances {

	private static class Node {
		private AbilityInstance value;
		private Node next, prev;
		
		public Node(AbilityInstance value, Node prev) {
			this.value = value;
			this.next = null;
			this.prev = prev;
			this.prev.next = this;
		}
		
		private void destroy() {
			value = null;
			
			if (prev != null) {
				prev.next = next;
			}
			
			if (next != null) {
				next.prev = prev;
			}
			
			prev = null;
			next = null;
		}
	}
	
	private Node head, tail;
	private int size, capacity;
	
	public AbilityInstances(int capacity) {
		this.head = null;
		this.tail = null;
		this.size = 0;
		this.capacity = capacity;
	}
	
	public AbilityInstance top() {
		return head == null ? null : head.value;
	}
	
	public int size() {
		return size;
	}
	
	public AbilityInstance get(int index) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		
		Node curr = head;
		
		for (int i = 0; i < index; ++i) {
			curr = curr.next;
		}
		
		return curr.value;
	}
	
	boolean add(AbilityInstance instance) {
		if (++size > capacity) {
			--size;
			return false;
		}
		
		Node node = new Node(instance, tail);
		if (head == null) {
			head = node;
		}
		tail = node;
		return true;
	}
	
	boolean remove(AbilityInstance instance) {
		Node found = find(instance);
		
		if (found == null) {
			return false;
		}
		
		found.destroy();
		return true;
	}
	
	private Node find(AbilityInstance instance) {
		Node curr = head;
		while (curr != null) {
			if (curr.value == instance) {
				return curr;
			}
			
			curr = curr.next;
		}
		
		return curr;
	}
}
