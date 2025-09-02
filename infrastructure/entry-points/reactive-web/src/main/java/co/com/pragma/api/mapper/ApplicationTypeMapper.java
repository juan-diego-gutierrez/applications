package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.ApplicationDTO;
import co.com.pragma.api.dto.ApplicationTypeDTO;
import co.com.pragma.model.application.Application;
import co.com.pragma.model.applicationtype.ApplicationType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationTypeMapper {

  ApplicationType toApplicationType(ApplicationTypeDTO applicationTypeDTO);

  ApplicationTypeDTO toApplicationTypeDTO(ApplicationType applicationType);
}
