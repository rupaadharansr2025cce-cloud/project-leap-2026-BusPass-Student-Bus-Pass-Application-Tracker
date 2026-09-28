package buspass.example.buspass.service;

import buspass.example.buspass.entity.BusRoute;
import buspass.example.buspass.repository.BusRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusRouteService {

    private final BusRouteRepository busRouteRepository;

    public BusRouteService(BusRouteRepository busRouteRepository) {
        this.busRouteRepository = busRouteRepository;
    }

    // Create route
    public BusRoute createRoute(BusRoute route) {
        return busRouteRepository.save(route);
    }

    // Get all routes
    public List<BusRoute> getAllRoutes() {
        return busRouteRepository.findAll();
    }

    // Get route by ID
    public BusRoute getRouteById(Long id) {

        return busRouteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bus route not found with id: " + id));
    }

    // Update route
    public BusRoute updateRoute(Long id, BusRoute updatedRoute) {

        BusRoute existingRoute = getRouteById(id);

        existingRoute.setRouteName(updatedRoute.getRouteName());
        existingRoute.setBoardingPoint(updatedRoute.getBoardingPoint());
        existingRoute.setDestination(updatedRoute.getDestination());
        existingRoute.setBusNumber(updatedRoute.getBusNumber());

        return busRouteRepository.save(existingRoute);
    }

    // Delete route
    public void deleteRoute(Long id) {

        if (!busRouteRepository.existsById(id)) {
            throw new RuntimeException("Bus route not found with id: " + id);
        }

        busRouteRepository.deleteById(id);
    }
}