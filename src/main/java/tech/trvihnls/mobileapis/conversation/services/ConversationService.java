package tech.trvihnls.mobileapis.conversation.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationLineResponse;

import java.util.List;

public interface ConversationService {
    List<ConversationLineResponse> getConversationDetail(long id);
    String processConversationPronoun(MultipartFile audioFile, long conversationLineId);
}
