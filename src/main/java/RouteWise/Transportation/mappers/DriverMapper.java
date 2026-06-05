package RouteWise.Transportation.mappers;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Entities.Ride;
import RouteWise.Transportation.dtos.BookRideDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.DriverSignupDTO;
import RouteWise.Transportation.dtos.RideResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface DriverMapper {

    Driver toEntity(DriverSignupDTO dto);

    @Mapping(source = "id", target = "driverId")
    @Mapping(source = "name", target = "driverName")
    DriverResponseDTO toDTO(Driver driver);
}
