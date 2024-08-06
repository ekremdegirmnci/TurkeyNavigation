import java.awt.*; // AWT library provides graphical user interface (GUI) components.
import java.io.File; // Java class used for file and directory operations.
import java.io.FileNotFoundException; // Represents exceptions to be thrown when a specified file is not found.
import java.io.IOException; // Represents a general exception that can occur during input/output (I/O) operations.
import java.util.*; // Java standard libraries for general-purpose use.
import java.util.List; // Java standard library for representing lists.


/**
 * @author ekremdegirmenci 
 * @version 1.0
 * @since Date: 04.04.2004
 * Turkey Navigation : This program tries to find the shortest
 * path between 2 cities from which data is received from the user.
 * For the purpose of the app different methods are defined.
 */
public class EkremDegirmenci {
    static List<City> cities = new ArrayList<>(); // Defines a list of the City class. It keeps City class' objects.
    static List<Road> roads = new ArrayList<>(); //Defines a list of the Road class. It keeps Road class' objects.

    public static void main(String[] args) throws FileNotFoundException {
        StdDraw.enableDoubleBuffering();
        try {
            CityFile("city_coordinates.txt"); // Calls the CityFile method to read city coordinates from file.
            ConnectionFile("city_connections.txt"); // Calls the ConnectionFile method to read city connections from file.

        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage()); // Print error message if IOException occurs.
            return;
        }
        MapDraw();  // Calls the MapDraw method to draw the map with cities and roads.
        Scanner scanner = new Scanner(System.in);
        City startingCity = null; //Value is assigned as null.
        City destinationCity = null;
        //This while loop receives inputs until it receives a valid input.
        while (startingCity == null) {
            System.out.print("Enter starting city: ");
            String sourceName = scanner.nextLine().trim();
            startingCity = getCity(sourceName);
            if (startingCity == null) {
                System.out.println("City named '" + sourceName + "' not found. Please enter a valid city name."); // If input is invalid then it gives a message.
            }
        }
        //This while loop receives inputs until it receives a valid input.
        while (destinationCity == null) {
            System.out.print("Enter destination city: "); // Prompt the user to enter the destination city.
            String destinationName = scanner.nextLine().trim(); // Read the destination city name entered by the user and remove leading/trailing whitespace.
            destinationCity = getCity(destinationName);// Get the City object corresponding to the destination city name.
            if (destinationCity == null) {
                System.out.println("City named '" + destinationName + "' not found. Please enter a valid city name."); // If input is invalid then it gives a message
            }
        }
        if (startingCity.equals(destinationCity)) {
            System.out.println("Total Distance: 0.00. Path: " + startingCity.getName()); // Print total distance and path
            List<City> shortestPath = new ArrayList<>(); // Create an empty list for the shortest path
            shortestPath.add(startingCity); // Add the starting city to the list
            drawRoads(shortestPath); // Draw the road of the starting city
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE); // Set pen color to blue
            StdDraw.setFont(new Font("Helvetica", Font.BOLD, 12)); // Set font
            StdDraw.text(startingCity.getX(), startingCity.getY() + 10, startingCity.getName()); // Display the city name
            StdDraw.filledCircle(startingCity.getX(),startingCity.getY(),4);
            StdDraw.show(); // Show the changes on the map
            StdDraw.pause(8000); // Pause for 8 seconds
            System.exit(0); // Exit the program
            return;
        }



        List<City> shortestPath = ShortestPathFinder(startingCity, destinationCity); // Finds the shortest path between starting city and destination city.
        if (shortestPath == null) {  // If no path is found, it prints a message and exit.
            System.out.println("No path found.");
            System.exit(0); // Exit the program.
            return;
        }
        // Calculates total distance of the shortest path.
        double totalDistance = 0.0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            City currentPosition = shortestPath.get(i); // Get the current city.
            City nextPosition = shortestPath.get(i + 1); // Get the next city in the path.
            double distance = distanceCalculator(currentPosition, nextPosition); // Calculate the distance between current city and next city.
            totalDistance += distance; // Add the distance to the total distance.
        }


        // Prints total distance and path.
        System.out.print("Total distance: " + String.format("%.2f", totalDistance) + "." + " Path: ");
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            City currentPosition = shortestPath.get(i);
            System.out.print(currentPosition.getName() + " -> ");
        }


        drawRoads(shortestPath); // Draws the shortest path on the map.
        System.out.println(destinationCity.getName()); // Prints destination city.
        StdDraw.pause(8000); //Pauses for 8 seconds display result.
        System.exit(0);//Exit
    }

    /**
     * Reads city coordinates from the specified file and populates the 'cities' list.
     *
     * @param file the name of the file containing city coordinates
     * @throws FileNotFoundException if the specified file is not found
     */
    private static void CityFile(String file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(file))) {
            while (scanner.hasNextLine()) {
                String fileLine = scanner.nextLine();
                String[] elems = fileLine.split(","); //Defines a list called elems from line of the file.
                String name = elems[0].replaceAll(" ", ""); // Defines the element at index 0 to name variable
                double x = Double.parseDouble(elems[1].replaceAll(" ", ""));// Defines the element at index 1 to x variable
                double y = Double.parseDouble(elems[2].replaceAll(" ", ""));// Defines the element at index 2 to y variable
                cities.add(new City(name, x, y));// Add a new City object with the specified name, x-coordinate, and y-coordinate to the 'cities' list.
            }
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        }
    }

    /**
     * Reads city connections from the specified file and populates the 'roads' list.
     *
     * @param file the name of the file containing city connections
     * @throws FileNotFoundException if the specified file is not found
     */
    private static void ConnectionFile(String file) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File(file))) {
            while (scanner.hasNextLine()) {
                String fileLine = scanner.nextLine();
                String[] elems = fileLine.split(",");//Defines a list called elems from line of the file.
                String cityName1 = elems[0].replaceAll(" ", "");// Defines the element at index 0 to cityName1 variable
                String cityName2 = elems[1].replaceAll(" ", "");// Defines the element at index 1 to cityName2 variable
                City city1 = getCity(cityName1);
                City city2 = getCity(cityName2);
                if (city1 != null && city2 != null) {
                    double distance = distanceCalculator(city1, city2);
                    roads.add(new Road(city1, city2, distance)); // Add a new Road object representing the connection between city1 and city2 with the specified distance to the 'roads' list.
                }
            }
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
        }
    }

    /**
     * Calculates the distance between two cities.
     *
     * @param city1 the first city
     * @param city2 the second city
     * @return the distance between the two cities
     */
    private static double distanceCalculator(City city1, City city2) {
        double city1xCoordinate = city1.getX(); //x coordinate of city1
        double city1yCoordinate = city1.getY(); //y coordinate of city1
        double city2xCoordinate = city2.getX(); //x coordinate of city2
        double city2yCoordinate = city2.getY(); //y coordinate of city2
        return Math.sqrt(Math.pow(city1xCoordinate - city2xCoordinate, 2)
                + Math.pow(city1yCoordinate - city2yCoordinate, 2)); // Calculates distance.
    }

    /**
     * Retrieves a city object by its name from the 'cities' list.
     *
     * @param cityName the name of the city to retrieve
     * @return the city object corresponding to the specified name, or null if not found
     */
    private static City getCity(String cityName) {
        for (City city : cities) {
            if (cityName.equals(city.getName())) { // // Checks if the name of the current city matches the specified city name.
                return city; //Returns the city object if it is found.
            }
        }
        return null; //Returns null if there is no matching value
    }
    /**
     * Finds the shortest path between the source and destination cities using Dijkstra's algorithm.
     *
     * @param source the starting city
     * @param destination the destination city
     * @return a list representing the shortest path from the source to the destination, or null if no path exists
     */
    private static List<City> ShortestPathFinder(City source, City destination) {
            // Array to keep track of whether each city has been visited.
            boolean[] isPassed = new boolean[cities.size()];
            // Array to store the shortest distance from the starting city to each city.
            double[] distances = new double[cities.size()];
            // Initializes distances to infinity.
            for (int i = 0; i < distances.length; i++) {
                distances[i] = Double.MAX_VALUE;
            }
            // Distance from source city to itself is 0.
            distances[cities.indexOf(source)] = 0.0;
            // Array to store the previous city in the shortest path.
            City[] citiesPassed = new City[cities.size()];
            // Dijkstra's algorithm implementation.
            while (true) {
                // Finds the unvisited city with the shortest distance from the source.
                double minDistance = Double.MAX_VALUE;
                int minIndex = -1;
                for (int i = 0; i < cities.size(); i++) {
                    if (!isPassed[i] && distances[i] < minDistance) {
                        minDistance = distances[i];
                        minIndex = i;
                    }
                }
                // If there are no more unvisited cities or if the destination city is unreachable, breaks the loop.
                if (minIndex == -1) {
                    break;
                }
                // Marks the current city as visited.
                City current = cities.get(minIndex);
                isPassed[minIndex] = true;
                // Updates the distances to neighboring cities through the current city.
                // Iterates through each road to update distances to neighboring cities through the current city.
                for (Road road : roads) {
                    // Checks if the current city is the starting point of the road.
                    if (road.getCity1().equals(current)) {
                        int neighborCityIndex = cities.indexOf(road.getCity2()); // Index of the neighboring city.
                        double newDistance = distances[minIndex] + road.getDistance(); // Calculate the new distance.
                        // If the new distance is shorter than the current distance to the neighboring city, update the distance and the previous city.
                        if (newDistance < distances[neighborCityIndex]) {
                            distances[neighborCityIndex] = newDistance;
                            citiesPassed[neighborCityIndex] = current;
                        }
                    }
                    // Checks if the current city is the end point of the road.
                    else if (road.getCity2().equals(current)) {
                        int neighborCityIndex = cities.indexOf(road.getCity1()); // Index of the neighboring city.
                        double newDistance = distances[minIndex] + road.getDistance(); // Calculate the new distance.
                        // If the new distance is shorter than the current distance to the neighboring city, update the distance and the previous city.
                        if (newDistance < distances[neighborCityIndex]) {
                            distances[neighborCityIndex] = newDistance;
                            citiesPassed[neighborCityIndex] = current;
                        }
                    }
                }

            }
            // Reconstructs the shortest path from the destination city to the source city.
            List<City> shortestPath = new ArrayList<>();
            int destinationIndex = cities.indexOf(destination);
            if (citiesPassed[destinationIndex] == null) {
                // If the destination city is unreachable, return null.
                return null;
            }
            // Traverse the cities from the destination city to the starting city and add them to the shortest path list.
            while (destinationIndex != -1) {
                shortestPath.add(cities.get(destinationIndex));
                destinationIndex = cities.indexOf(citiesPassed[destinationIndex]);
            }
            // Reverse the list to get the path from starting city to destination.
            int size = shortestPath.size();
            for (int i = 0; i < size / 2; i++) {
            Object temp = shortestPath.get(i);
            shortestPath.set(i, shortestPath.get(size - 1 - i));
            shortestPath.set(size - 1 - i, (City) temp);
         }
            return shortestPath;
        }

    /**
     * Draws the map with cities and roads.
     */
    private static void MapDraw() {


        StdDraw.setCanvasSize(2377 / 2, 1055 / 2); //Sets canvas size.
        StdDraw.setXscale(0, 2377);//Sets X scale.
        StdDraw.setYscale(0, 1055);//Sets Y scale.
        StdDraw.picture(2377 / 2.0, 1055 / 2.0, "map.png", 2377, 1055);//Draws map

        //Draws cities on map.
        for (City city : cities) {
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            double xCoordinate = city.getX();
            double yCoordinate = city.getY();
            StdDraw.filledCircle(xCoordinate, yCoordinate, 4); //Filled circle for cities.
            String cityName = city.getName();
            if (cityName != null) {
                StdDraw.setFont(new Font("Helvetica Bold", Font.BOLD, 12)); //Sets the font.
                StdDraw.text(xCoordinate, yCoordinate + 10.00, cityName); //Prints names of the cities on map.
            }
        }

        // Draws roads on map.
        for (Road road : roads) {
            StdDraw.setPenColor(StdDraw.GRAY);
            City city1 = road.getCity1();
            City city2 = road.getCity2();
            double xc1 = city1.getX(); // Gets the x coordinate of city1.
            double xc2 = city2.getX(); // Gets the x coordinate of city2.
            double yc1 = city1.getY(); // Gets the y coordinate of city1.
            double yc2 = city2.getY(); // Gets the y coordinate of city2.

            StdDraw.line(xc1, yc1, xc2, yc2); // Draws the line between two cities to represent road.
        }
        StdDraw.show();
    }

    /**
     * Draws roads of the shortest path on the map.
     *
     * @param path the shortest path to draw
     */
    private static void drawRoads(List<City> path) {
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.setPenRadius(0.008); //Sets pen radius
        // Iterate through the cities in the path to draw roads between them.
        for (int i = 0; i < path.size() - 1; i++) {
            City city1 = path.get(i); // Get the current city.
            City city2 = path.get(i + 1); // Get the next city in the path.
            StdDraw.line(city1.getX(), city1.getY(), city2.getX(), city2.getY()); // Draw a line between the current city and the next city.
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.setFont(new Font("Helvetica", Font.BOLD, 12)); // Set font for city names

            // Display the city name on the second city
            StdDraw.text(city2.getX(), city2.getY() + 10, city2.getName());
            City startingCity = path.get(0);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.setFont(new Font("Helvetica", Font.BOLD, 12)); // Set font for city names
            StdDraw.text(startingCity.getX(), startingCity.getY() + 10, startingCity.getName());


        }

        StdDraw.show();

    }


}
