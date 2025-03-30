package tech.trvihnls.mobileapis.conversation.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.mobileapis.conversation.dtos.request.ConversationSubmitRequest;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationLineResponse;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationSubmitResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationLineResponse> getConversationDetail(long id);
    String processConversationPronoun(MultipartFile audioFile, long conversationLineId);
    ConversationSubmitResponse submitConversation(long conversationId, ConversationSubmitRequest request);
}
