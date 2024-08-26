import java.util.NoSuchElementException;

public class BinarySearchTree<Key extends Comparable<? super Key>> extends BinaryTree<Key> {
    /**
     * creates a new instance
     *
     * @param left  the left subtree
     * @param key   the key of the tree node
     * @param right the right subtree
     */
    public BinarySearchTree(BinarySearchTree<Key> left, Key key, BinarySearchTree<Key> right) {
        super(left, key, right);
    }

    public BinarySearchTree(Key key) {
        this(null, key, null);
    }


    /**
     * returns the left subtree
     */
    @Override
    public BinarySearchTree<Key> getLeft() {
        return (BinarySearchTree<Key>) super.getLeft();
    }

    /**
     * returns the right subtree
     */
    @Override
    public BinarySearchTree<Key> getRight() {
        return (BinarySearchTree<Key>) super.getRight();
    }

    /**
     * adds a key to the tree
     *
     * @param key the key of the tree node
     */
    public void add(Key key) {
        if(this.getKey().compareTo(key)>0){
            if (this.getLeft()==null){
                this.left = new BinarySearchTree<>(key);
            }
            else{
                this.getLeft().add(key);
            }
        }
        else {
            if (this.getRight()==null){
                this.right = new BinarySearchTree<>(key);
            }
            else {
                this.getRight().add(key);
            }
        }
    }

    /**
     * searches a key in the tree
     *
     * @param key the key of the tree node
     * @return subtree where key is found or null
     */
    public BinarySearchTree<Key> search(Key key) {
        if (this.getKey()==key){
            return this;
        }
        if (this.getLeft() == null && this.getRight() == null) return null;
        if (this.key.compareTo(key) > 0){
            return this.getLeft().search(key);
        }
        else {
            return this.getRight().search(key);
        }
    }

    /**
     * deletes a key from tree
     *
     * @param key the key of the tree node
     * @return true if found and deleted, false otherwise
     */
    public boolean remove(Key key) {
        // für Endknoten:
        if(this.getLeft()!=null) {
            if (this.getLeft().getKey() == key && this.getLeft().getLeft() == null && this.getLeft().getRight()==null){
                this.left = null;
                return true;
            }
        }
        if (this.right!=null){
            if(this.getRight().getKey() == key && this.getRight().getLeft() == null && this.getRight().getRight()==null){
                this.right = null;
                return true;
            }
        }
        // remove aus der Vorlesung:
        if (this.getKey()!=key){
            if (this.key.compareTo(key) > 0){
                this.getLeft().remove(key);
            }
            else {
                if (this.key.compareTo(key) < 0){
                    this.getRight().remove(key);
                }
            }
        }
        else{
            if (this.getLeft()==null){
                this.key = this.getRight().getKey();
                this.right = this.getRight().getRight();
                this.left = this.getRight().getLeft();
                return true;
            }
            else {
                if (this.getRight()==null){
                    this.key = this.getLeft().getKey();
                    this.right = this.getLeft().getRight();
                    this.left = this.getLeft().getLeft();
                    return true;
                }
                else { //removeSym aus der Vorlesung
                    removeSymmetricPredecessor(this);
                    return true;
                }
            }
        }
        return false;
    }


    private Key removeSymmetricPredecessor(BinarySearchTree<Key> toDelete) {
        BinarySearchTree<Key> p = toDelete;
        if (p.getRight().getLeft()!=null){
            p = p.getRight();
            while (p.getLeft().getLeft()!=null){
                p = p.getLeft();
            }
            toDelete.key = p.getLeft().getKey();
            p.left = p.getLeft().getRight();
        }
        else {
            toDelete.key = toDelete.getRight().getKey();
            toDelete.right = toDelete.getRight().getRight();
        }
        return toDelete.getKey();
    }


    /**
     * returns the min key of a tree
     *
     * @return
     */
    public Key getMinKey() {
        if(this.getLeft()==null){
            return this.getKey();
        }
        else {
            return getLeft().getMinKey();
        }
    }

    /**
     * returns the max key of a tree
     *
     * @return
     */
    public Key getMaxKey() {
        if(this.getRight()==null){
            return this.getKey();
        }
        else{
            return getRight().getMaxKey();
        }
    }

    /**
     * finds the successor of a key
     *
     * @param key the key of the tree node
     * @return subtree of successor
     */
    public BinarySearchTree<Key> getSuccessor(BinarySearchTree<Key> key) { // Der kleinste Schlüssel > key
        BinarySearchTree<Key> right = key.getRight();
        if(right != null){
            return right.search(right.getMinKey());
        }
        else{
            BinarySearchTree<Key> successor = null;
            BinarySearchTree<Key> ancestor = this;
            while (ancestor != key) {
                if (key.getKey().compareTo(ancestor.getKey()) < 0) {
                    successor = ancestor;
                    ancestor = ancestor.getLeft();
                } else {
                    ancestor = ancestor.getRight();
                }
            }
            return successor;
        }
    }

    /**
     * finds the predecessor of a key
     *
     * @param key the key of the tree node
     * @return subtree of predecessor
     */
    public BinarySearchTree<Key> getPredecessor(BinarySearchTree<Key> key) { // Der größte Schlüssel < key
        BinarySearchTree<Key> left = key.getLeft();
        if(left != null){
            return left.search(left.getMaxKey());
        }
        else{
            BinarySearchTree<Key> predecessor = null;
            BinarySearchTree<Key> ancestor = this;
            while (ancestor != key) {
                if (key.getKey().compareTo(ancestor.getKey()) > 0) {
                    predecessor = ancestor;
                    ancestor = ancestor.getRight();
                } else {
                    ancestor = ancestor.getLeft();
                }
            }
            return predecessor;
        }
    }

    /**
     * finds the max key y in the tree such that y<=x
     *
     * @param x
     * @return
     */
    public Key getMaxKey(Key x) {
        //TODO
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>(7);
        System.out.println(binarySearchTree);
        binarySearchTree.add(3);
        System.out.println("Add 3:");
        System.out.println(binarySearchTree);
        binarySearchTree.add(6);
        System.out.println("Add 6:");
        System.out.println(binarySearchTree);
        binarySearchTree.add(2);
        System.out.println("Add 2:");
        System.out.println(binarySearchTree);
        binarySearchTree.add(8);
        System.out.println("Add 8:");
        System.out.println(binarySearchTree);
        binarySearchTree.add(10);
        System.out.println("Add 10:");
        System.out.println(binarySearchTree);
        binarySearchTree.remove(7);
        System.out.println("Remove 7:");
        System.out.println(binarySearchTree);
        System.out.println("Get Predecessor of 6:");
        System.out.println(binarySearchTree.getPredecessor(binarySearchTree.search(6)));
    }
}
