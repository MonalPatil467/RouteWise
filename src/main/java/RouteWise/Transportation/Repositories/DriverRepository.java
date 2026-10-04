package RouteWise.Transportation.Repositories;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByPhone(String phone);

    Optional<Driver> findByPhone(String phone);

    List<Driver> findByAvailableTrueAndCurrentLocation(
            String currentLocation
    );

    @Query("""
        SELECT d
        FROM Driver d
        WHERE d.vehicleType = :vehicleType
        AND d.available = true
        AND d.capacityKg >= :goodsWeight
        AND d.latitude IS NOT NULL
        AND d.longitude IS NOT NULL
        ORDER BY
        (6371 * 2 * ASIN(
            SQRT(
                POWER(
                    SIN(
                        RADIANS(d.latitude - :latitude) / 2
                    ), 2
                )
                +
                COS(RADIANS(:latitude))
                * COS(RADIANS(d.latitude))
                * POWER(
                    SIN(
                        RADIANS(d.longitude - :longitude) / 2
                    ), 2
                )
            )
        ))
    """)
    List<Driver> findNearbyDrivers(
            @Param("vehicleType") VehicleType vehicleType,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("goodsWeight") Double goodsWeight
    );
}


