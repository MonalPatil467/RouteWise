package RouteWise.Transportation.mappers;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Entities.Ride;
import RouteWise.Transportation.dtos.DriverSignupDTO;
import RouteWise.Transportation.dtos.RideResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RideMapper {

    @Mapping(source = "id", target = "rideId")
    RideResponseDTO toDTO(Ride ride);
}
