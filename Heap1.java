public class Heap1 {
    int s = 0;
    Node[] H1 = new Node[2];

    public void percdown(int i, Node N) {
        if (2 * i > s) {
            H1[i] = N;
        } else if (2 * i == s) {
            if (H1[2 * i].qdep_time < N.qdep_time) {
                H1[i] = H1[2 * i];
                H1[2 * i] = N;
            } else if (H1[2 * i].qdep_time == N.qdep_time) {
                if (H1[2 * i].queue_id > N.queue_id) {
                    H1[i] = H1[2 * i];
                    H1[2 * i] = N;
                }
            } else {
                H1[i] = N;
            }
        } else {
            int j;
            if (H1[2 * i].qdep_time < H1[2 * i + 1].qdep_time) {
                j = 2 * i;
            } else if (H1[2 * i].qdep_time == H1[2 * i + 1].qdep_time) {
                if (H1[2 * i].queue_id > H1[2 * i + 1].queue_id) {
                    j = 2 * i;
                } else {
                    j = 2 * i + 1;
                }
            } else
                j = 2 * i + 1;
            if (H1[j].qdep_time < N.qdep_time) {
                H1[i] = H1[j];
                percdown(j, N);
            } else if (H1[2 * i].qdep_time == N.qdep_time) {
                if (H1[2 * i].queue_id > N.queue_id) {
                    H1[i] = H1[2 * i];
                    H1[2 * i] = N;
                }
            } else {
                H1[i] = N;
            }
        }
    }

    public void percup(int i) {
        if (i == 1)
            return;
        if (H1[i].qdep_time < H1[i / 2].qdep_time) {
            Node temp = H1[i / 2];
            H1[i / 2] = H1[i];
            H1[i] = temp;
        }
        if (H1[i].qdep_time == H1[i / 2].qdep_time) {
            if (H1[i / 2].queue_id < H1[i].queue_id) {
                Node temp1 = H1[i / 2];
                H1[i / 2] = H1[i];
                H1[i] = temp1;
            }
        }
        percup(i / 2);
    }

    public Node findMin() {
        return H1[1];
    }

    public Node delMin() {
        Node N = H1[1];
        H1[1] = H1[s];
        H1[s] = null;
        s--;
        // System.out.println(s+"-=-=-=");
        if (s > 0)
            percdown(1, H1[1]);
        return N;
    }

    public void insert(Node N) {
        if (s == H1.length - 1) {
            Node[] X = new Node[2 * H1.length];
            for (int i = 1; i <= s; i++) {
                X[i] = H1[i];
            }
            H1 = X;
        }
        H1[s+1]=N;
        s++;
        percup(s);
    }

    public boolean isEmpty() {
        if (s == 0) {
            return true;
        }
        return false;
    }
}