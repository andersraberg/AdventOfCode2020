package se.anders_raberg.adventofcode2020;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class Day23CircularList {

    private Node _head;
    private Node _tail;
    private Map<Integer, Node> _nodes = new HashedMap<>();

    public static class Node {
        private int _value;
        private Node _next;

        public Node(int value) {
            _value = value;
        }

        public int value() {
            return _value;
        }

    }

    public Day23CircularList(List<Integer> list) {
        list.forEach(this::add);
    }

    public Node add(Integer x) {
        Node newNode = new Node(x);

        if (_head == null) {
            _head = newNode;
        } else {
            _tail._next = newNode;
        }

        _tail = newNode;
        _tail._next = _head;
        _nodes.put(x, newNode);
        return newNode;
    }

    public Node insertAfter(int x, Node node) {
        Node newNode = new Node(x);

        newNode._next = node._next;
        node._next = newNode;
        _nodes.put(x, newNode);
        return newNode;
    }

    public int removeAfter(Node node) {
        int tmp = node._next._value;
        if (node._next == _head) {
            _head = node._next._next;
        }
        node._next = node._next._next;
        return tmp;
    }

    public Node next(Node node) {
        return node._next;
    }

    public Node get(int x) {
        return _nodes.get(x);
    }

    @Override
    public String toString() {
        Node tmp = _head;
        StringBuilder sb = new StringBuilder("[" + tmp._value);
        tmp = tmp._next;
        while (tmp != _head) {
            sb.append(", ").append(tmp._value);
            tmp = tmp._next;
        }
        sb.append("]");
        return sb.toString();
    }

}