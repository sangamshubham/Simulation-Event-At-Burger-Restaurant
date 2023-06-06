public class Heap {
    int K;
    Node[] H;

    public void setK(int k) {
        this.K = k;
        H = new Node[K + 1];
    }

    public void percdown(int i, Node N) {
        if (2 * i > K) {
            H[i] = N;
        } else if (2 * i == K) {
            if (H[2 * i].q.size() < N.q.size()) {
                H[i] = H[2 * i];
                H[2 * i] = N;
            } else if (H[2 * i].q.size() == N.q.size()) {
                if (H[2 * i].id < N.id) {
                    H[i] = H[2 * i];
                    H[2 * i] = N;
                }
            } else {
                H[i] = N;
            }
        } else {
            int j;
            if (H[2 * i].q.size() < H[2 * i + 1].q.size()) {
                j = 2 * i;
            } else if (H[2 * i].q.size() == H[2 * i + 1].q.size()) {
                if (H[2 * i].id < H[2 * i + 1].id) {
                    j = 2 * i;
                } else {
                    j = 2 * i + 1;
                }
            } else
                j = 2 * i + 1;
            if (H[j].q.size() < N.q.size()) {

                H[i] = H[j];
                percdown(j, N);
            }

            else if (H[2 * i].q.size() == N.q.size()) {

                if (H[2 * i].id < N.id) {
                    H[i] = H[2 * i];
                    H[2 * i] = N;
                }
                //
            } else {
                H[i] = N;
            }
        }
    }

    public Node findMin() {
        return H[1];
    }

    public void buildHeap(Node[] Z) {
        for (int i = Z.length / 2; i >= 1; i--) {
            percdown(i, Z[i]);
        }
    }
}