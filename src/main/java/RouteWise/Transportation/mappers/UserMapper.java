package RouteWise.Transportation.mappers;

import RouteWise.Transportation.Entities.User;
import RouteWise.Transportation.dtos.UserSignupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

     User toEntity(UserSignupDTO dto);
}
