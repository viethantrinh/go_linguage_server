package tech.trvihnls.features.main.services;

import tech.trvihnls.features.conversation.dtos.response.ConversationResponse;
import tech.trvihnls.features.main.dtos.response.HomeResponse;
import tech.trvihnls.features.main.dtos.response.ReviewResponse;

import java.util.List;

public interface MainService {
    HomeResponse retrieveHomeData();
    ReviewResponse retrieveReviewData();
    List<ConversationResponse> retrieveConversationData();
}
