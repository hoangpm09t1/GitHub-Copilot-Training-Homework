public class ShapeFactory {
    public static Shape createShape(String type, double size) {
        if (type == null) {
            throw new IllegalArgumentException("Shape type cannot be null");
        }
        switch (type.toLowerCase()) {
            case "circle":
                return new Circle(size);
            case "square":
                return new Square(size);
            default:
                throw new IllegalArgumentException("Unknown shape type: " + type);
        }
    }
}
