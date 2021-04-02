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
	
	void remove(AbilityInstance instance) {
		Node found = find(instance);
		
		if (found == null) {
			return;
		}
		
		found.prev.next = found.next;
		found.next.prev = found.prev;
		found.destroy();
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
