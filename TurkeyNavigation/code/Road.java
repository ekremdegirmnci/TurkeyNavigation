/**
 * Represents a road connecting two cities with a certain distance.
 */
public class Road {
    // The first city connected by the road
    City city1;
    // The second city connected by the road
    City city2;
    // The distance of the road between the two cities
    double distance;

    // Getter method for retrieving the first city connected by the road
    public City getCity1() {
        return city1;
    }

    // Getter method for retrieving the second city connected by the road
    public City getCity2() {
        return city2;
    }

    // Getter method for retrieving the distance of the road
    public double getDistance() {
        return distance;
    }

    // Setter method for setting the first city connected by the road
    public void setCity1(City city1) {
        this.city1 = city1;
    }

    // Setter method for setting the second city connected by the road
    public void setCity2(City city2) {
        this.city2 = city2;
    }

    // Setter method for setting the distance of the road
    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Constructor to initialize a Road object with the given cities and distance
    public Road(City city1, City city2, double distance) {
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
    }
}
