import java.util.LinkedList;
import java.util.Stack;

public class queueStack {
    // Queue methods
    private LinkedList<Integer> queue = new LinkedList<>();

    public void enqueue(int value) {
        queue.addLast(value);
    }

    public int dequeue() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue.removeFirst();
    }

    public int peekQueue() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queue.getFirst();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public int queueSize() {
        return queue.size();
    }

    // Stack methods
    private Stack<Integer> stack = new Stack<>();

    public void push(int value) {
        stack.push(value);
    }

    public int pop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return stack.pop();
    }

    public int peekStack() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return stack.peek();
    }

    public boolean isStackEmpty() {
        return stack.isEmpty();
    }

    public int stackSize() {
        return stack.size();
    }
}
