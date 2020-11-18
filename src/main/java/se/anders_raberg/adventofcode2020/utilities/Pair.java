package se.anders_raberg.adventofcode2020.utilities;

import java.util.Objects;

public class Pair<U, V> {
    private final U _first;
    private final V _second;

    public Pair(U first, V second) {
        _first = first;
        _second = second;
    }

    public U first() {
        return _first;
    }

    public V second() {
        return _second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_first, _second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(_first, other._first) && Objects.equals(_second, other._second);
    }

    @Override
    public String toString() {
        return "(" + _first + ", " + _second + ")";
    }

}
