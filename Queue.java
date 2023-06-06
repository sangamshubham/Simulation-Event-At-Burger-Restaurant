public class Queue {
    Node head=new Node();
    Node tail;
    int q_size = 0;


    public boolean isEmpty() {
        return q_size==0;
    }

    public int size() {     
        return q_size;
    }

    public void enqueue(Node node) {
        if(isEmpty()){
            head=node;
            tail=head;
        }
        else{
            Node temp=node;
            tail.next=temp;
            tail=tail.next;
        }
    
        q_size++;


    }

    public Node dequeue() {
        Node temp=head;
        head=head.next;
        q_size--;
        return temp;
    }
}