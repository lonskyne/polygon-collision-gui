package main.avl;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> {
    private Node<T> root;

    // Get the height of the node
    private int height(Node<T> node) {
        return node == null ? 0 : node.getHeight();
    }

    // Right rotate subtree rooted with y
    private Node<T> rightRotate(Node<T> y) {
        Node<T> x = y.getLeft();
        Node<T> T2 = x.getRight();

        // Perform rotation
        x.setRight(y);
        y.setLeft(T2);

        // Update heights
        y.setHeight(1 + Math.max(height(y.getLeft()), height(y.getRight())));
        x.setHeight(1 + Math.max(height(x.getLeft()), height(x.getRight())));

        return x;
    }

    // Left rotate subtree rooted with x
    private Node<T> leftRotate(Node<T> x) {
        Node<T> y = x.getRight();
        Node<T> T2 = y.getLeft();

        // Perform rotation
        y.setLeft(x);
        x.setRight(T2);

        // Update heights
        x.setHeight(1 + Math.max(height(x.getLeft()), height(x.getRight())));
        y.setHeight(1 + Math.max(height(y.getLeft()), height(y.getRight())));

        return y;
    }

    // Get balance factor of a node
    private int getBalance(Node<T> node) {
        return node == null ? 0 : height(node.getLeft()) - height(node.getRight());
    }

    // Insert a key into the AVL tree
    public void insert(T key) {
        root = insert(root, key);
    }

    private Node<T> insert(Node<T> node, T key) {
        // Perform standard BST insertion
        if (node == null) {
            return new Node<>(key);
        }

        if (key.compareTo(node.getKey()) < 0) {
            node.setLeft(insert(node.getLeft(), key));
        } else if (key.compareTo(node.getKey()) > 0) {
            node.setRight(insert(node.getRight(), key));
        } else {
            // Duplicate keys not allowed
            return node;
        }

        // Update height
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        // Get balance factor and perform rotations if necessary
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && key.compareTo(node.getLeft().getKey()) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key.compareTo(node.getRight().getKey()) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key.compareTo(node.getLeft().getKey()) > 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key.compareTo(node.getRight().getKey()) < 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    // Remove a key from the AVL tree
    public void remove(T key) {
        root = remove(root, key);
    }

    private Node<T> remove(Node<T> node, T key) {
        // Perform standard BST delete
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.getKey()) < 0) {
            node.setLeft(remove(node.getLeft(), key));
        } else if (key.compareTo(node.getKey()) > 0) {
            node.setRight(remove(node.getRight(), key));
        } else {
            // Node with one or no child
            if ((node.getLeft() == null) || (node.getRight() == null)) {
                Node<T> temp = node.getLeft() != null ? node.getLeft() : node.getRight();

                // No child case
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp; // One child case
                }
            } else {
                // Node with two children: Get inorder successor (smallest in right subtree)
                Node<T> temp = getMin(node.getRight());

                // Copy successor's data to this node
                node.setKey(temp.getKey());

                // Remove successor
                node.setRight(remove(node.getRight(), temp.getKey()));
            }
        }

        // If the tree had only one node
        if (node == null) {
            return null;
        }

        // Update height
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        // Get balance factor and perform rotations
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.getLeft()) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.getLeft()) < 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.getRight()) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.getRight()) > 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    public Node<T> getMin(Node<T> node) {
        if(node == null)
            return null;

        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    public Node<T> getMax(Node<T> node) {
        if(node == null)
            return null;

        while (node.getRight() != null) {
            node = node.getRight();
        }
        return node;
    }

    public Node<T> getRoot() {
        return root;
    }
}