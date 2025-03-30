package tech.trvihnls.mobileapis.conversation.services.impl;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.ConversationLineRepository;
import tech.trvihnls.commons.repositories.ConversationUserOptionRepository;
import tech.trvihnls.commons.repositories.UserConversationAttemptRepository;
import tech.trvihnls.commons.repositories.UserRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ConversationEntryTypeEnum;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.mobileapis.achievement.dtos.response.AchievementResponse;
import tech.trvihnls.mobileapis.ai.services.GroqTranscribeService;
import tech.trvihnls.mobileapis.conversation.dtos.request.ConversationSubmitRequest;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationLineResponse;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationSubmitResponse;
import tech.trvihnls.mobileapis.conversation.dtos.response.ConversationUserOptionResponse;
import tech.trvihnls.mobileapis.conversation.services.ConversationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationLineRepository conversationLineRepository;
    private final ConversationUserOptionRepository conversationUserOptionRepository;
    private final GroqTranscribeService groqTranscribeService;
    private final UserRepository userRepository;
    private final UserConversationAttemptRepository userConversationAttemptRepository;
    private final AchievementRepository achievementRepository;

    @Override
    public List<ConversationLineResponse> getConversationDetail(long id) {
        return conversationLineRepository.findByConversationIdOrderByDisplayOrderAsc(id).stream()
                .map(cl -> ConversationLineResponse.builder()
                        .id(cl.getId())
                        .isChangeSpeaker(cl.getType().equals(ConversationEntryTypeEnum.system))
                        .displayOrder(cl.getDisplayOrder())
                        .systemEnglishText(cl.getSystemEnglishText())
                        .systemVietnameseText(cl.getSystemVietnameseText())
                        .systemAudioUrl(cl.getSystemAudioUrl())
                        .options(cl.getConversationUserOptions().stream()
                                .map(o -> ConversationUserOptionResponse.builder()
                                        .gender(o.getGender().getVietnameseGender())
                                        .englishText(o.getEnglishText())
                                        .vietnameseText(o.getVietnameseText())
                                        .audioUrl(o.getAudioUrl())
                                        .build())
                                .toList())
                        .build())
                .toList();
    }

    @Override
    public String processConversationPronoun(MultipartFile audioFile, long conversationLineId) {
        List<ConversationUserOption> conversationUserOptions = conversationUserOptionRepository
                .findByConversationLineId(conversationLineId);

        if (conversationUserOptions.isEmpty())
            throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);

        String transcribedUserText = groqTranscribeService.transcribeAudio(audioFile);

        // Clean up the transcribed text
        transcribedUserText = normalizeText(transcribedUserText);

        // Initialize similarity calculators
        NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();
        JaroWinkler jaroWinkler = new JaroWinkler();

        // Find the most similar option
        ConversationUserOption bestMatch = null;
        double bestSimilarity = 0.0;
        double similarityThreshold = 0.5; // Adjust based on testing

        for (ConversationUserOption option : conversationUserOptions) {
            String englishText = normalizeText(option.getEnglishText());

            // Combine multiple similarity metrics for better accuracy
            // We can weight JaroWinkler higher as it often works better for transcribed speech
            double levSimilarity = levenshtein.similarity(transcribedUserText, englishText);
            double jaroSimilarity = jaroWinkler.similarity(transcribedUserText, englishText);
            double combinedSimilarity = (levSimilarity + jaroSimilarity * 2) / 3.0;

            if (combinedSimilarity > bestSimilarity) {
                bestSimilarity = combinedSimilarity;
                bestMatch = option;
            }
        }

        // Return the best match if it exceeds the threshold, otherwise null
        if (bestMatch != null && bestSimilarity >= similarityThreshold) {
            return bestMatch.getEnglishText(); // Or return whatever property is needed
        } else {
            return null;
        }
    }

    /**
     * Normalizes text for better comparison
     */
    private String normalizeText(String text) {
        if (text == null) return "";

        // Convert to lowercase, remove excess whitespace, and strip punctuation
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Override
    public ConversationSubmitResponse submitConversation(long conversationId, ConversationSubmitRequest request) {
        int points = request.getGoPoints();

        User user = userRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId()))
                .orElseThrow(() -> new AppException(ErrorCodeEnum.USER_NOT_EXISTED));

        UserConversationAttempt userConversationAttempt = userConversationAttemptRepository
                .findByUserIdAndConversationId(user.getId(), conversationId).orElse(null);

        if (userConversationAttempt == null) {
            userConversationAttemptRepository.save(
                    UserConversationAttempt.builder()
                            .id(UserConversationAttemptId.builder().userId(user.getId()).conversationId(conversationId).build())
                            .goPointsEarned(points)
                            .build()
            );
        } else {
            userConversationAttempt.setGoPointsEarned(userConversationAttempt.getGoPointsEarned() + points);
            userConversationAttemptRepository.save(userConversationAttempt);
        }

        user.setTotalGoPoints(user.getTotalGoPoints() + points);
        user.setTotalStreakPoints(user.getTotalStreakPoints() + 1);
        User savedUser = userRepository.save(user);

        // if achieve any achievement, handle here
        List<Achievement> achievements = achievementRepository.findAll();
        List<AchievementResponse> achievementResponses = new ArrayList<>();
        for (Achievement a : achievements) {
            if (savedUser.getTotalGoPoints() >= a.getGoRewardCondition() && !savedUser.getAchievements().contains(a)) {
                AchievementResponse achievementResponse = AchievementResponse.builder()
                        .name(a.getName())
                        .description(a.getDescription())
                        .imageUrl(a.getImageUrl())
                        .build();
                achievementResponses.add(achievementResponse);
                savedUser.getAchievements().add(a);
                userRepository.save(user);
            }
        }

        return ConversationSubmitResponse.builder()
                .achievements(achievementResponses)
                .build();
    }
}
