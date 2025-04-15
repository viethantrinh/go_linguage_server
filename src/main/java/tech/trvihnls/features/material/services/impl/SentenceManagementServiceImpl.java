// src/main/java/tech/trvihnls/features/material/services/impl/WordManagementServiceImpl.java
package tech.trvihnls.features.material.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.Sentence;
import tech.trvihnls.commons.domains.Topic;
import tech.trvihnls.commons.domains.Word;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.repositories.SentenceRepository;
import tech.trvihnls.commons.repositories.TopicRepository;
import tech.trvihnls.commons.repositories.WordRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.material.dtos.request.admin.SentenceRequest;
import tech.trvihnls.features.material.dtos.response.admin.SentenceResponse;
import tech.trvihnls.features.material.services.SentenceManagementService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SentenceManagementServiceImpl implements SentenceManagementService {
    private final SentenceRepository sentenceRepository;
    private final TopicRepository topicRepository;
    private final WordRepository wordRepository;
    // Inject EntityManager
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<SentenceResponse> getAllSentences() {
        List<Sentence> sentences = sentenceRepository.findAll();
        return sentences.stream()
                .map(this::mapToSentenceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public SentenceResponse getSentenceById(Long id) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        return mapToSentenceResponse(sentence);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    // TODO: triển khai gọi đến AI để text to speech nhận kết quả - lưu url lên cloudinary
    public SentenceResponse createSentence(SentenceRequest request) {
        Sentence sentence = new Sentence();
        updateSentenceFromRequest(sentence, request);

        Sentence savedSentence = sentenceRepository.save(sentence);
        return mapToSentenceResponse(savedSentence);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    // TODO: triển khai gọi đến AI để text to speech nhận kết quả - lưu url lên cloudinary
    public SentenceResponse updateSentence(Long id, SentenceRequest request) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        updateSentenceFromRequest(sentence, request);
        Sentence updatedSentence = sentenceRepository.save(sentence);
        return mapToSentenceResponse(updatedSentence);
    }

    private void updateSentenceFromRequest(Sentence sentence, SentenceRequest request) {
        sentence.setEnglishText(request.getEnglishText());
        sentence.setVietnameseText(request.getVietnameseText());

        // Handle topic associations
        if (request.getTopicIds() != null) {
            List<Topic> topics = new ArrayList<>();
            for (Long topicId : request.getTopicIds()) {
                Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.TOPIC_NOT_EXISTED));
                topics.add(topic);
            }

            // Update many-to-many relationship from both sides
            sentence.getTopics().clear();
            sentence.getTopics().addAll(topics);

            for (Topic topic : topics) {
                if (!topic.getSentences().contains(sentence)) {
                    topic.getSentences().add(sentence);
                }
            }
        }

        if (request.getWordIds() != null) {
            // Get original words to handle bidirectional cleanup later
            List<Word> originalWords = new ArrayList<>(sentence.getWords());

            // Create new list of words from request
            List<Word> newWords = new ArrayList<>();
            for (Long wordId : request.getWordIds()) {
                Word word = wordRepository.findById(wordId)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                newWords.add(word);
            }

            // Update many-to-many relationship from both sides
            sentence.getWords().clear();
            sentence.getWords().addAll(newWords);

            // Add sentence to new words if not already there
            for (Word word : newWords) {
                if (!word.getSentences().contains(sentence)) {
                    word.getSentences().add(sentence);
                }
            }

            // Remove sentence from words that are no longer associated
            for (Word word : originalWords) {
                if (!newWords.contains(word)) {
                    word.getSentences().remove(sentence);
                }
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSentenceById(Long id) {
        Sentence sentence = sentenceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Clean up sentence-topic associations
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_topic_sentence WHERE sentence_id = :sentenceId")
                .setParameter("sentenceId", id)
                .executeUpdate();

        // Clean up word-sentence associations
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_word_sentence WHERE sentence_id = :sentenceId")
                .setParameter("sentenceId", id)
                .executeUpdate();

        // Delete the sentence
        sentenceRepository.deleteById(id);
    }

    private SentenceResponse mapToSentenceResponse(Sentence sentence) {
        List<Long> topicIds = sentence.getTopics().stream()
                .map(Topic::getId)
                .collect(Collectors.toList());

        List<Long> wordIds = new ArrayList<>();
        // For each word, check if this sentence is in its sentences list
        // This is a bit inefficient but ensures correct bidirectional mapping
        wordRepository.findAll().forEach(word -> {
            if (word.getSentences().contains(sentence)) {
                wordIds.add(word.getId());
            }
        });

        return SentenceResponse.builder()
                .id(sentence.getId())
                .englishText(sentence.getEnglishText())
                .vietnameseText(sentence.getVietnameseText())
                .audioUrl(sentence.getAudioUrl())
                .createdAt(sentence.getCreatedAt())
                .updatedAt(sentence.getUpdatedAt())
                .topicIds(topicIds)
                .wordIds(wordIds)
                .build();
    }


}
