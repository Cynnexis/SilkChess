package fr.polytech.projet.silkchess.game.ai.minimax;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.exceptions.InfiniteLoopException;
import fr.berger.enhancedlist.tree.Node;
import fr.berger.enhancedlist.tree.Tree;

import java.io.Serializable;

/**
 * Abstract Minimax algorithm.
 * @param <T> The type of data a node contains
 * @author Valentin Berger
 */
public abstract class AbstractMinimax<T> implements Serializable, Computable<T> {
	
	public static final int PLUS_INFINITY = Integer.MAX_VALUE;
	public static final int MINUS_INFINITY = Integer.MIN_VALUE;
	
	public Couple<Integer, Node<T>> minimax(@Nullable Tree<T> root) throws InfiniteLoopException {
		InfiniteLoopException ex = new InfiniteLoopException();
		return minimax(root, root.computeHeight(), true, ex);
	}
	public Couple<Integer, Node<T>> minimax(@Nullable Node<T> root) throws InfiniteLoopException {
		InfiniteLoopException ex = new InfiniteLoopException();
		return minimax(root, root.computeHeight(), true, ex);
	}
	public Couple<Integer, Node<T>> minimax(@NotNull Node<T> node, int depth, boolean isMax) throws InfiniteLoopException {
		return minimax(node, depth, isMax, null);
	}
	public Couple<Integer, Node<T>> minimax(@NotNull Node<T> node, int depth, boolean isMax, @Nullable InfiniteLoopException ex) throws InfiniteLoopException {
		if (node == null)
			throw new NullPointerException();
		
		if (depth == 0 || node.isLeaf())
			// Return the heurisitc value of 'node'
			return new Couple<>(compute(node.getData()), node);
		
		int bestValue = 0;
		Couple<Integer, Node<T>> v = new Couple<>(0, null);
		
		if (isMax) {
			bestValue = MINUS_INFINITY;
			for (Node<T> child : node) {
				// increment() add +1 to a counter for each recursif call. When it reach its limit, the exception is
				// thrown
				ex.increment();
				v = minimax(child, depth - 1, false, ex);
				bestValue = Integer.max(bestValue, v.getX());
			}
		}
		else {
			bestValue = PLUS_INFINITY;
			for (Node<T> child : node) {
				ex.increment();
				v = minimax(child, depth - 1, true, ex);
				bestValue = Integer.min(bestValue, v.getX());
			}
		}
		
		return new Couple<>(bestValue, v.getY());
	}
}
