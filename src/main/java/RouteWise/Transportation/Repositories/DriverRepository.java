package RouteWise.Transportation.Repositories;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByPhone(String phone);

    List<Driver> findByVehicleTypeAndAvailableAndCurrentLocation(
            VehicleType vehicleType,
            Boolean available,
            String currentLocation
    );

    List<Driver> findByAvailableTrueAndCurrentLocation(
            String currentLocation
    );
    boolean existsByPhone(String phone);
}
