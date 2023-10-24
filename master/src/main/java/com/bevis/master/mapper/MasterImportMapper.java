package com.bevis.master.mapper;

import com.bevis.master.domain.MasterImport;
import com.bevis.master.dto.MasterImportDTO;
import org.mapstruct.Mapper;

@Mapper
public interface MasterImportMapper {

    MasterImportDTO toDto(MasterImport masterImport);

}
