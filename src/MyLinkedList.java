import java.util.Comparator;

public class MyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private final int capacity;

    public MyLinkedList() {
        this.capacity = Integer.MAX_VALUE;
        head = null;
        tail = null;
        size = 0;
    }

    public MyLinkedList(int capacity) throws InvalidCapacityException {
        if (capacity <= 0) {
            throw new InvalidCapacityException("Capacity must be positive: " + capacity);
        }
        this.capacity = capacity;
        head = null;
        tail = null;
        size = 0;
    }

    private void checkFull() throws ListFullException {
        if (size >= capacity) {
            throw new ListFullException("List is full. Size: " + size + ", Capacity: " + capacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new InvalidIndexException("Invalid index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new InvalidIndexException("Invalid index for add: " + index + ", Size: " + size);
        }
    }

    private void checkNotEmpty() {
        if (size == 0) {
            throw new EmptyListException("Cannot operate on empty list");
        }
    }

    private void checkNull(T value) {
        if (value == null) {
            throw new NullElementException("Null elements are not allowed");
        }
    }

    public void addLast(T value) throws ListFullException {
        checkNull(value);
        checkFull();
        Node<T> newNode = new Node<>(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void addFirst(T value) throws ListFullException {
        checkNull(value);
        checkFull();
        Node<T> newNode = new Node<>(value);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    private Node<T> getNode(int index) {
        checkIndex(index);
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    public void add(int index, T value) throws ListFullException {
        checkNull(value);
        checkFull();
        checkIndexForAdd(index);

        if (index == 0) {
            addFirst(value);
            return;
        }
        if (index == size) {
            addLast(value);
            return;
        }

        Node<T> current = getNode(index);
        Node<T> newNode = new Node<>(value);
        Node<T> prevNode = current.prev;

        prevNode.next = newNode;
        newNode.prev = prevNode;
        newNode.next = current;
        current.prev = newNode;

        size++;
    }

    public T get(int index) {
        checkNotEmpty();
        return getNode(index).data;
    }

    public T remove(int index) {
        checkNotEmpty();
        Node<T> nodeToRemove = getNode(index);
        T value = nodeToRemove.data;
        Node<T> prevNode = nodeToRemove.prev;
        Node<T> nextNode = nodeToRemove.next;

        if (prevNode == null) {
            head = nextNode;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else if (nextNode == null) {
            tail = prevNode;
            tail.next = null;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        size--;
        return value;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public void sort() {
        if (size <= 1) return;
        if (!(head.data instanceof Comparable)) {
            throw new ClassCastException("Elements must implement Comparable");
        }
        Comparator<T> naturalComparator = (o1, o2) -> ((Comparable<T>) o1).compareTo(o2);
        head = mergeSort(head, naturalComparator);
        Node<T> current = head;
        while (current != null && current.next != null) {
            current = current.next;
        }
        tail = current;
    }

    public void sort(Comparator<T> comparator) {
        if (size <= 1) return;
        head = mergeSort(head, comparator);
        Node<T> current = head;
        while (current != null && current.next != null) {
            current = current.next;
        }
        tail = current;
    }

    private Node<T> mergeSort(Node<T> node, Comparator<T> comparator) {
        if (node == null || node.next == null) return node;
        Node<T> middle = getMiddle(node);
        Node<T> nextOfMiddle = middle.next;
        middle.next = null;
        if (nextOfMiddle != null) nextOfMiddle.prev = null;
        Node<T> left = mergeSort(node, comparator);
        Node<T> right = mergeSort(nextOfMiddle, comparator);
        return merge(left, right, comparator);
    }
    private Node<T> getMiddle(Node<T> head) {
        if (head == null) return null;
        Node<T> slow = head;
        Node<T> fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    private Node<T> merge(Node<T> left, Node<T> right, Comparator<T> comparator) {
        Node<T> dummy = new Node<>(null);
        Node<T> current = dummy;
        while (left != null && right != null) {
            if (comparator.compare(left.data, right.data) <= 0) {
                current.next = left;
                left.prev = current;
                left = left.next;
            } else {
                current.next = right;
                right.prev = current;
                right = right.next;
            }
            current = current.next;
        }
        if (left != null) {
            current.next = left;
            left.prev = current;
        }
        if (right != null) {
            current.next = right;
            right.prev = current;
        }
        Node<T> result = dummy.next;
        if (result != null) result.prev = null;
        return result;
    }
}