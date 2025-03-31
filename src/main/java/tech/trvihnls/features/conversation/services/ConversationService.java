package tech.trvihnls.features.conversation.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.features.conversation.dtos.request.ConversationSubmitRequest;
import tech.trvihnls.features.conversation.dtos.response.ConversationLineResponse;
import tech.trvihnls.features.conversation.dtos.response.ConversationSubmitResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationLineResponse> getConversationDetail(long id);
    String processConversationPronoun(MultipartFile audioFile, long conversationLineId);
    ConversationSubmitResponse submitConversation(long conversationId, ConversationSubmitRequest request);
}
