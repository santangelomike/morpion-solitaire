package fr.dauphine.ja.lamhandyhajar.morpionsolitaire.core;

public class Pair<T, E> {
	protected T p1;
	protected E p2;

	public T getP1() {
		return p1;
	}

	public E getP2() {
		return p2;
	}

	Pair(T p1, E p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (!(o instanceof Pair))
			return false;
		Pair<?, ?> p = (Pair<?, ?>) o;

		return p.p1 == p1 && p.p2 == p2;
	}
}
