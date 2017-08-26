public class Communication implements Comparable<Communication>{

    private final Expert e1;
    private final Expert e2;
    private final double cost;

    public Communication(Expert e1, Expert e2, double cost) {
        this.e1 = e1;
        this.e2 = e2;
        this.cost = cost;
    }

    @Override
    public int compareTo(Communication communication) {
        double delta = this.cost - communication.cost;
        if(delta == 0){
            return 0;
        }
        if(delta < 0){
            return -1;
        }
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Communication)){
            return false;
        }
        Communication communication = (Communication)obj;
        if(this.e1 == communication.e1 && this.e2 == communication.e2 || this.e1 == communication.e2 && this.e2 == communication.e1){
            return true;
        }
        return false;
    }

    public double getCost() {
        return cost;
    }

    public Expert getE1() {
        return e1;
    }

    public Expert getE2() {
        return e2;
    }
}
