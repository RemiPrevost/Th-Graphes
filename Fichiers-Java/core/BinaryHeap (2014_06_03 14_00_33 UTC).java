package core;

import java.util.*;

public class BinaryHeap<E extends Comparable<E>> {

	private int currentSize; // Number of elements in heap

	// Java genericity does not work with arrays.
	// We have to use an ArrayList
	private ArrayList<E> array; // The heap array

	// HashMap permettant de donner la position dans le tas d'un objet E donne
	private HashMap<E, Integer> map;

	/**
	 * Construct the binary heap.
	 */
	public BinaryHeap() {
		this.currentSize = 0;
		this.array = new ArrayList<E>();
		this.map = new HashMap<E, Integer>();
	}

	// Constructor used for debug.
	private BinaryHeap(BinaryHeap<E> heap) {
		this.currentSize = heap.currentSize;
		this.array = new ArrayList<E>(heap.array);
		this.map = new HashMap<E, Integer>(heap.map);
	}

	// ajoute un element dans le tas, a la prochaine place de libre
	private void arraySet(int index, E value) {
		if (index == this.array.size()) {
			this.array.add(value);
		} else {
			this.array.set(index, value);
		}
	}

	// retourne vrai si le tas est vide
	public boolean isEmpty() {
		return this.currentSize == 0;
	}

	// retourne le nombre courant d'elements dans le tas
	public int size() {
		return this.currentSize;
	}

	// retourne l'indice du pere d'un indice
	private int index_parent(int index) {
		return (index - 1) / 2;
	}

	// retourne l'indice du fils gauche d'un indice
	private int index_left(int index) {
		return index * 2 + 1;
	}

	// insere un nouvel element dans le tas et le place au bon endroit
	public boolean insert(E x) {
		// cas ou le tas ne contient pas l'element --> il faut l'ajouter
		int index = this.currentSize++;
		// ajout au tas
		this.arraySet(index, x);
		this.percolateUp(index);
		return true ;
	}

	// permet de remonter un element situe a l'indice donne jusqu'a ce qu'il soit bien place
	private void percolateUp(int index) {
		E x = this.array.get(index);

		for (; index > 0 && x.compareTo(this.array.get(index_parent(index))) < 0; index = index_parent(index)) {
			E moving_val = this.array.get(index_parent(index));
			this.arraySet(index, moving_val);
			this.map.put(moving_val, index);
		}

		this.arraySet(index, x);
		this.map.put(x, index);
	}

	// permet de remonter un element situe a l'indice donne jusqu'a ce qu'il soit bien place
	private void percolateDown(int index) {
		int ileft = index_left(index);
		int iright = ileft + 1;

		if (ileft < this.currentSize) {
			E current = this.array.get(index);
			E left = this.array.get(ileft);
			boolean hasRight = iright < this.currentSize;
			E right = (hasRight) ? this.array.get(iright) : null;

			if (!hasRight || left.compareTo(right) < 0) {
				// Left is smaller
				if (left.compareTo(current) < 0) {
					this.arraySet(index, left);
					this.arraySet(ileft, current);
					this.map.put(left, index);
					this.map.put(current, ileft);
					this.percolateDown(ileft);
				}
			} else {
				// Right is smaller
				if (right.compareTo(current) < 0) {
					this.arraySet(index, right);
					this.arraySet(iright, current);
					this.map.put(left, index);
					this.map.put(current, iright);
					this.percolateDown(iright);
				}
			}
		}
	}

	// retourne l'element le plus petit dans le tas
	public E findMin() {
		if (isEmpty())
			throw new RuntimeException("Empty binary heap");
		return this.array.get(0);
	}

	// supprime la racine et restructure le tas
	public E deleteMin() {
		E minItem = findMin();
		E lastItem = this.array.get(--this.currentSize);
		this.arraySet(0, lastItem);
		this.percolateDown(0);
		return minItem;
	}

	// mis a jour du tas : on ajoute l'element s'il n'existe pas dans le tas sinon on le place au bon endroit
	public boolean update(E element) {
		// on recupere la position de l'element dans le tas
		Integer pos = this.map.get(element);
		
		// l'element n'existe pas -> on l'insere
		if (pos == null) {
			insert(element) ;
			return true ;
		}
		// l'element existe --> on le place au bon endroit
		else {
			percolateUp(pos);
			percolateDown(pos) ;
			return false ;
		}
	}

	/**
	 * Prints the heap
	 */
	public void print() {
		System.out.println();
		System.out.println("========  HEAP  (size = " + this.currentSize
				+ ")  ========");
		System.out.println();

		for (E el : this.array) {
			System.out.println(el.toString());
		}

		System.out.println();
		System.out.println("--------  End of heap  --------");
		System.out.println();
	}

	/**
	 * Prints the elements of the heap according to their respective order.
	 */
	public void printSorted() {

		BinaryHeap<E> copy = new BinaryHeap<E>(this);

		System.out.println();
		System.out.println("========  Sorted HEAP  (size = " + this.currentSize
				+ ")  ========");
		System.out.println();

		while (!copy.isEmpty()) {
			System.out.println(copy.deleteMin());
		}

		System.out.println();
		System.out.println("--------  End of heap  --------");
		System.out.println();
	}
}