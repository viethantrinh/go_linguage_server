package tech.trvihnls.features.topic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import tech.trvihnls.commons.domains.Topic;
import tech.trvihnls.features.topic.dtos.response.TopicAdminResponse;
import tech.trvihnls.features.topic.dtos.response.TopicCreateAdminResponse;
import tech.trvihnls.features.topic.dtos.response.TopicImageResponse;
import tech.trvihnls.features.topic.dtos.response.TopicUpdateAdminResponse;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TopicMapper {
    @Mapping(target = "isPremium", source = "premium")
    TopicAdminResponse toTopicResponse(Topic topic);

    @Mapping(target = "isPremium", source = "premium")
    TopicCreateAdminResponse toTopicCreateAdminResponse(Topic topic);

    @Mapping(target = "isPremium", source = "premium")
    TopicImageResponse toTopicImageResponse(Topic topic);

    @Mapping(target = "isPremium", source = "premium")
    TopicUpdateAdminResponse toTopicUpdateAdminResponse(Topic topic);
}