/**
 * Represents a city with its name and coordinates.
 */
public class City {
    // Name of the city
    String name;
    // X-coordinate of the city
    double x;
    // Y-coordinate of the city
    double y;

    // Getter method for retrieving the name of the city
    public String getName() {
        return name;
    }

    // Getter method for retrieving the X-coordinate of the city
    public double getX() {
        return x;
    }

    // Getter method for retrieving the Y-coordinate of the city
    public double getY() {
        return y;
    }

    // Setter method for setting the name of the city
    public void setName(String name) {
        this.name = name;
    }

    // Setter method for setting the X-coordinate of the city
    public void setX(double x) {
        this.x = x;
    }

    // Setter method for setting the Y-coordinate of the city
    public void setY(double y) {
        this.y = y;
    }

    // Constructor to initialize a City object with the given name, x-coordinate, and y-coordinate
    public City(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
