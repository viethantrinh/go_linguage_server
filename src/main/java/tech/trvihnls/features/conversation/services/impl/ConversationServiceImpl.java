package tech.trvihnls.features.conversation.services.impl;

import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ConversationEntryGenderEnum;
import tech.trvihnls.commons.utils.enums.ConversationEntryTypeEnum;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;
import tech.trvihnls.features.ai.services.TtsService;
import tech.trvihnls.features.ai.services.impl.GroqServiceImpl;
import tech.trvihnls.features.conversation.dtos.request.ConversationCreateDto;
import tech.trvihnls.features.conversation.dtos.request.ConversationLineCreateDto;
import tech.trvihnls.features.conversation.dtos.request.ConversationSubmitRequest;
import tech.trvihnls.features.conversation.dtos.request.ConversationUserOptionCreateDto;
import tech.trvihnls.features.conversation.dtos.response.*;
import tech.trvihnls.features.conversation.services.ConversationService;
import tech.trvihnls.features.media.services.MediaUploadService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationLineRepository conversationLineRepository;
    private final ConversationUserOptionRepository conversationUserOptionRepository;
    private final GroqServiceImpl groqServiceImpl;
    private final UserRepository userRepository;
    private final UserConversationAttemptRepository userConversationAttemptRepository;
    private final AchievementRepository achievementRepository;
    // Existing class definition and injected repositories...
    private final ConversationRepository conversationRepository;
    private final TtsService ttsService;
    private final MediaUploadService mediaUploadService;

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

        String transcribedUserText = groqServiceImpl.transcribeAudio(audioFile);

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

    @Override
    public List<ConversationListResponse> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findByOrderByDisplayOrderAsc();

        return conversations.stream()
                .map(conversation -> ConversationListResponse.builder()
                        .id(conversation.getId())
                        .name(conversation.getName())
                        .displayOrder(conversation.getDisplayOrder())
                        .imageUrl(conversation.getImageUrl())
                        .createdAt(conversation.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .lineCount(conversation.getConversationLines().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ConversationDetailResponse getConversationById(long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        List<ConversationDetailLineResponse> lines = conversation.getConversationLines().stream()
                .map(line -> ConversationDetailLineResponse.builder()
                        .id(line.getId())
                        .type(line.getType())
                        .displayOrder(line.getDisplayOrder())
                        .systemEnglishText(line.getSystemEnglishText())
                        .systemVietnameseText(line.getSystemVietnameseText())
                        .systemAudioUrl(line.getSystemAudioUrl())
                        .options(line.getConversationUserOptions().stream()
                                .map(option -> ConversationUserOptionResponse.builder()
                                        .englishText(option.getEnglishText())
                                        .vietnameseText(option.getVietnameseText())
                                        .audioUrl(option.getAudioUrl())
                                        .gender(option.getGender().getVietnameseGender())
                                        .build())
                                .toList())
                        .build())
                .toList();

        return ConversationDetailResponse.builder()
                .id(conversation.getId())
                .name(conversation.getName())
                .imageUrl(conversation.getImageUrl())
                .displayOrder(conversation.getDisplayOrder())
                .createdAt(conversation.getUpdatedAt() == null ? conversation.getCreatedAt() : conversation.getUpdatedAt())
                .lines(lines)
                .build();
    }

    @Override
    public ConversationDetailResponse createConversation(ConversationCreateDto createDto) {
        // Create and save the conversation
        Conversation conversation = Conversation.builder()
                .name(createDto.getName())
                .displayOrder(createDto.getDisplayOrder())
                .build();

        conversation = conversationRepository.save(conversation);

        List<ConversationLine> lines = new ArrayList<>();
        int displayOrder = 0;

        // Process each conversation line
        for (ConversationLineCreateDto lineDto : createDto.getLines()) {
            ConversationLine line = ConversationLine.builder()
                    .displayOrder(displayOrder++)
                    .type(lineDto.getType())
                    .conversation(conversation)
                    .build();

            // Handle system type lines with TTS
            if (lineDto.getType().equals(ConversationEntryTypeEnum.system)) {
                line.setSystemEnglishText(lineDto.getEnglishText());
                line.setSystemVietnameseText(lineDto.getVietnameseText());

                // Generate audio for system messages
                byte[] audioBytes = ttsService.requestTextToSpeech(lineDto.getEnglishText());
                var uploadResult = mediaUploadService.uploadAudio(audioBytes);
                line.setSystemAudioUrl(uploadResult.getSecureUrl());
            }

            // Save the line first
            line = conversationLineRepository.save(line);

            // Process user options if any
            if (!lineDto.getOptions().isEmpty()) {
                for (ConversationUserOptionCreateDto optionDto : lineDto.getOptions()) {
                    // Generate audio for user options
                    byte[] optionAudioBytes = ttsService.requestTextToSpeech(optionDto.getEnglishText());
                    var optionUploadResult = mediaUploadService.uploadAudio(optionAudioBytes);

                    ConversationUserOption option = ConversationUserOption.builder()
                            .englishText(optionDto.getEnglishText())
                            .vietnameseText(optionDto.getVietnameseText())
                            .conversationLine(line)
                            .gender(ConversationEntryGenderEnum.male) // Could be parameterized in DTO
                            .audioUrl(optionUploadResult.getSecureUrl())
                            .build();

                    conversationUserOptionRepository.save(option);
                }
            }

            lines.add(line);
        }

        // Simply get the saved conversation with all its details
        return getConversationById(conversation.getId());
    }

    @Override
    public void deleteConversation(long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        conversationRepository.delete(conversation);
    }
}
