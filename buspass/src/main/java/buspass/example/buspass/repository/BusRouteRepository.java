package buspass.example.buspass.repository;

import buspass.example.buspass.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
}