package buspass.example.buspass.controller;

import buspass.example.buspass.entity.BusRoute;
import buspass.example.buspass.service.BusRouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class BusRouteController {

    private final BusRouteService busRouteService;

    public BusRouteController(BusRouteService busRouteService) {
        this.busRouteService = busRouteService;
    }

    @PostMapping
    public ResponseEntity<BusRoute> createRoute(
            @Valid @RequestBody BusRoute route) {

        return new ResponseEntity<>(
                busRouteService.createRoute(route),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<BusRoute>> getAllRoutes() {
        return ResponseEntity.ok(
                busRouteService.getAllRoutes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusRoute> getRouteById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                busRouteService.getRouteById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusRoute> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody BusRoute route) {

        return ResponseEntity.ok(
                busRouteService.updateRoute(id, route)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(
            @PathVariable Long id) {

        busRouteService.deleteRoute(id);

        return ResponseEntity.ok(
                "Bus route deleted successfully"
        );
    }
}