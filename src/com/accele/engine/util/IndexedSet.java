package com.accele.engine.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class IndexedSet<E> extends AbstractSet<E> implements Cloneable, Serializable {

	private static final long serialVersionUID = -551156320218279871L;

	private transient ArrayList<E> content;
	
	public IndexedSet() {
		this.content = new ArrayList<>();
	}
	
	public IndexedSet(int initialCapacity) {
		this.content = new ArrayList<>(initialCapacity);
	}
	
	public IndexedSet(Collection<? extends E> c) {
		this.content = new ArrayList<>(c);
	}
	
	@Override
	public boolean add(E e) {
		if (content.contains(e))
			return false;
		content.add(e);
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = false;
		for (E e : c)
			if (add(e))
				result = true;
		return result;
	}
	
	@Override
	public Iterator<E> iterator() {
		return content.iterator();
	}

	@Override
	public int size() {
		return content.size();
	}
	
	public E get(int index) {
		return content.get(index);
	}
	
	@SuppressWarnings("unchecked")
    public Object clone() {
        try {
            IndexedSet<E> newSet = (IndexedSet<E>) super.clone();
            newSet.content = (ArrayList<E>) content.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}
