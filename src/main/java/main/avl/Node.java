package main.avl;

public class Node<T extends Comparable<T>> {
    private T key;
    private int height;
    private Node<T> left, right;

    public Node(T d) {
        key = d;
        height = 1;
    }

    public T getKey() {
        return key;
    }

    public int getHeight() {
        return height;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }
}
