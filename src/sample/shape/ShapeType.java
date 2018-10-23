package sample.shape;

public enum ShapeType {
    RECTANGLE("Prostokąt"),
    CIRCLE("Okrąg"),
    LINE("Linia");

    private final String type;

    ShapeType(String type) {
        this.type = type;
    }

    public static ShapeType getShapeType(String type) {
        for (ShapeType shapeType: ShapeType.values()) {
            if (shapeType.type.equals(type)) {
                return shapeType;
            }
        }
        throw new IllegalArgumentException("No constant with text " + type + " found");
    }
}

