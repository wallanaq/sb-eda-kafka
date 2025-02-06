package com.example.eda.mapper;

import com.example.eda.entity.EventEntity;
import com.example.eda.schemas.EventSchema;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

  EventEntity toEntity(EventSchema event);

}
