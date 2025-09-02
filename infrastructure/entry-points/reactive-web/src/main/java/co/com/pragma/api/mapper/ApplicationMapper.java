package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ApplicationDTO;
import co.com.pragma.model.application.Application;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

  Application toApplication(ApplicationDTO applicationDTO);

  ApplicationDTO toApplicationDTO(Application application);
}
