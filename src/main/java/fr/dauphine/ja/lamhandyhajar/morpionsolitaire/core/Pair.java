package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

public class Pair<T> {
    protected T p1, p2;

    Pair(T p1, T p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    T getP1() {
        return p1;
    }

    T getP2() {
        return p2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Pair))
            return false;
        Pair<?> p = (Pair<?>) o;

        return p.p1 == p1 && p.p2 == p2;
    }
}
