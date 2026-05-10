public class Square implements Shape {
    private final double side;

    public Square(double side) {
        if (side < 0) {
            throw new IllegalArgumentException("Side length must be non-negative");
        }
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }

    public double getSide() {
        return side;
    }
}
