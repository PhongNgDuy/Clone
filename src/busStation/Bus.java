package busStation;

public class Bus {
    String id;
    String name;
    final int price=7;
    double speed;

    public Bus(String id,String name) {
        this.id = id;
        this.name=name;
        if (name.equals("Bus HN")) speed=50;
        else speed=60;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }
}
