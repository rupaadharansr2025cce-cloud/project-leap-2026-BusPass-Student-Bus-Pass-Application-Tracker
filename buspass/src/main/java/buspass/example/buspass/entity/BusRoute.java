package buspass.example.buspass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "bus_routes")
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Route name is required")
    private String routeName;

    @NotBlank(message = "Boarding point is required")
    private String boardingPoint;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Bus number is required")
    private String busNumber;

    public BusRoute() {
    }

    public BusRoute(String routeName, String boardingPoint,
                    String destination, String busNumber) {
        this.routeName = routeName;
        this.boardingPoint = boardingPoint;
        this.destination = destination;
        this.busNumber = busNumber;
    }

    public Long getId() {
        return id;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }
}