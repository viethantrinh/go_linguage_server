package tech.trvihnls.mobileapis.main.services;

import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationResponse;
import tech.trvihnls.mobileapis.main.dtos.response.HomeResponse;
import tech.trvihnls.mobileapis.main.dtos.response.ReviewResponse;

import java.util.List;

public interface MainService {
    HomeResponse retrieveHomeData();
    ReviewResponse retrieveReviewData();
    List<ConversationResponse> retrieveConversationData();
}
