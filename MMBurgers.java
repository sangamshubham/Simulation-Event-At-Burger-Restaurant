// If given number (of queue, burgers, etc.) is not valid
class EmptyQueueException extends Exception {
}

class Node {
    int id, num_of_order, arrival_time;
    int dep_time, qdep_time, queue_id, bs_time;
    Node next;
    Queue q;
}

public class MMBurgers implements MMBurgersInterface {
    int M;
    int K;
    int t_start;
    int no_Of_Customer;
    int no_Of_burger_on_griddle;
    int Global_time;
    Heap heap = new Heap();
    Heap1 heap_griddle = new Heap1();
    Queue queue = new Queue();
    Node[] customerInfo = new Node[2];

    public int max(int a, int b) {
        if (a < b) {
            return b;
        }
        return a;
    }

    public boolean isEmpty() {
        if (no_Of_burger_on_griddle == 0 && heap_griddle.isEmpty()) {
            for (int i = 1; i <= K; i++) {
                if (!heap.H[i].q.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setK(int k) throws IllegalNumberException {
        if (k < 0 || K != 0) {
            throw new IllegalNumberException("Invalid K value");
        }
        this.K = k;
        heap.setK(k);
        for (int i = 1; i < K + 1; i++) {
            Node N = new Node();
            N.q = new Queue();
            N.id = i;
            heap.H[i] = N;
        }
        // your implementation
        // throw new java.lang.UnsupportedOperationException("Not implemented yet.");
    }

    public void setM(int m) throws IllegalNumberException {
        if (m < 0 || M != 0) {
            throw new IllegalNumberException("Invalid M value");
        }
        this.M = m;
    }

    public void advanceTime(int t) throws IllegalNumberException {
        if (t < 0) {
            throw new IllegalNumberException("Invalid time");
        }
        for (int j = 1; j <= K; j++) {
            Node N = heap.H[j];
            int i = 0;
            while (!N.q.isEmpty()) {
                i = N.id + max(i, N.q.head.arrival_time);
                if (i > t) {
                    break;
                } else {
                    Node customer = N.q.dequeue();
                    customer.qdep_time = i;
                    heap_griddle.insert(customer);
                }
            }
        }
        heap.buildHeap(heap.H);

        int i = Global_time;
        while (!heap_griddle.isEmpty() && i <= t) {
            while (!queue.isEmpty() && i - queue.head.bs_time == 10) {
                Node customer = queue.dequeue();
                no_Of_burger_on_griddle -= customer.num_of_order;
            }
            while (!heap_griddle.isEmpty() && no_Of_burger_on_griddle < M
                    && i >= heap_griddle.findMin().qdep_time) {
                if (heap_griddle.findMin().num_of_order + no_Of_burger_on_griddle <= M) {
                    Node N = heap_griddle.delMin();
                    N.bs_time = i;
                    N.dep_time = i + 11;
                    no_Of_burger_on_griddle += N.num_of_order;
                    queue.enqueue(N);
                } else {
                    Node N = new Node();
                    N.id = heap_griddle.findMin().id;
                    N.bs_time = i;
                    N.num_of_order = M - no_Of_burger_on_griddle;
                    heap_griddle.findMin().num_of_order -= N.num_of_order;
                    no_Of_burger_on_griddle = M;
                    queue.enqueue(N);
                }
            }
            i++;
        }
        while (!queue.isEmpty() && i <= t) {
            while (!queue.isEmpty() && i - queue.head.bs_time == 10) {
                Node customer = queue.dequeue();
                no_Of_burger_on_griddle -= customer.num_of_order;
            }
            i++;
        }
        Global_time = t;
    }

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException {
        try {
            if (t < Global_time) {
                throw new IllegalNumberException("Invalid time");
            } else if (customerInfo[id] != null) {
                throw new IllegalNumberException("Id already in queue");
            } else {
                if (id != 1 && customerInfo[id - 1] == null) {
                    throw new IllegalNumberException("Id are not consecutive");
                }
            }
        } catch (IllegalNumberException e) {
            throw new IllegalNumberException("Invalid customerId");
        }
        advanceTime(t);
        Node customer = new Node();
        customer.id = id;
        customer.num_of_order = numb;
        customer.arrival_time = t;
        customer.dep_time = -1;
        customer.qdep_time = -1;
        Node Q = heap.findMin();
        customer.queue_id = Q.id;
        Q.q.enqueue(customer);
        heap.percdown(1, Q);
        customerInfo[id] = customer;
        no_Of_Customer++;
        if (id == customerInfo.length - 1) {
            Node[] temp = new Node[2 * customerInfo.length];
            for (int i = 1; i < customerInfo.length; i++) {
                temp[i] = customerInfo[i];
            }
            customerInfo = temp;
        }
    }

    public int customerState(int id, int t) throws IllegalNumberException {
        if (t < Global_time) {
            throw new IllegalNumberException("Invalid time");
        }
        advanceTime(t);
        if(id>no_Of_Customer) {
            return 0;
        }
        if(customerInfo[id] !=null){
            if (customerInfo[id].dep_time != -1 && customerInfo[id].dep_time <= t) {
                return K + 2;
            } else if (customerInfo[id].qdep_time != -1) {
                return K + 1;
            } else {
                return customerInfo[id].queue_id;
            }
        }
        return 0;
    }

    public int griddleState(int t) throws IllegalNumberException {
        if (t < Global_time) {
            throw new IllegalNumberException("Invalid time");
        }
        advanceTime(t);
        return no_Of_burger_on_griddle;
    }

    public int griddleWait(int t) throws IllegalNumberException {
        if (t < Global_time) {
            throw new IllegalNumberException("Invalid time");
        }
        advanceTime(t);
        int wait = 0;
        for (int i = 1; i <= heap_griddle.s; i++) {
            wait += heap_griddle.H1[i].num_of_order;
        }
        return wait;
    }

    public int customerWaitTime(int id) throws IllegalNumberException {
        try {
            if (customerInfo[id] == null) {
                throw new IllegalNumberException("Invalid customer id");
            }
        } catch (Exception e) {
            throw new IllegalNumberException("Invalid customer id");
        }
        return customerInfo[id].dep_time - customerInfo[id].arrival_time;
    }

    public float avgWaitTime() {
        float sum = 0.0f;
        for (int i = 1; i <= no_Of_Customer; i++) {
            sum += (customerInfo[i].dep_time - customerInfo[i].arrival_time);
        }
        return (sum / no_Of_Customer);
    }
}
