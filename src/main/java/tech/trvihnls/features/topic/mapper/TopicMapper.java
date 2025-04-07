package tech.trvihnls.features.topic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import tech.trvihnls.commons.domains.Topic;
import tech.trvihnls.features.topic.dtos.response.TopicAdminResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TopicMapper {
    TopicAdminResponse toTopicResponse(Topic topic);
}