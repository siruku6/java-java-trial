class Drink {
    private String name;
    private int price;

    Drink() {
        this.name = "Unknown";
        this.price = 0;
    }
    Drink(String name, int price) {
        this.name = name;
        this.price = price;
    }

    String getName() {
        return this.name;
    }

    int getPrice() {
        return this.price;
    }

    public String toString() {
        return this.name + "(" + this.price + "Yen)";
    }
}


class Water extends Drink {
    Water() {
        super("Water", 100);
    }
}

interface AlcDrink {
    public double getPercentage();
}

class Beer extends Drink implements AlcDrink {
    private double percentage;

    Beer(int percentage) {
        super("Beer", 300);
        this.percentage = (double)percentage;
    }

    public double getPercentage() {
        return this.percentage;
    }

    // String toString() {
    //     return this.name + "(" + this.price + "Yen)(" + this.getPercentage() + "%)";
    // }
}


class DrinkMain{
    public static void main(String[] args){
        Drink[] ds = new Drink[3];
        ds[0] = new Water();
        ds[1] = new Drink("Coffee", 130);
        ds[2] = new Beer(5);

        for(Drink d : ds){
            System.out.print(d);
            if(d instanceof AlcDrink)
                System.out.println("("+((AlcDrink)d).getPercentage()+"%)");
            else
                System.out.println();
        }
    }
}
