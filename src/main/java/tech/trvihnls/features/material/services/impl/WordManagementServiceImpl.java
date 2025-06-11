// src/main/java/tech/trvihnls/features/material/services/impl/WordManagementServiceImpl.java
package tech.trvihnls.features.material.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.domains.Sentence;
import tech.trvihnls.commons.domains.Topic;
import tech.trvihnls.commons.domains.Word;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.repositories.SentenceRepository;
import tech.trvihnls.commons.repositories.TopicRepository;
import tech.trvihnls.commons.repositories.WordRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.features.ai.services.TtsService;
import tech.trvihnls.features.material.dtos.request.admin.WordRequest;
import tech.trvihnls.features.material.dtos.response.admin.WordImageResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordResponse;
import tech.trvihnls.features.material.services.WordManagementService;
import tech.trvihnls.features.media.dtos.response.CloudinaryUrlResponse;
import tech.trvihnls.features.media.services.MediaUploadService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordManagementServiceImpl implements WordManagementService {

    private final WordRepository wordRepository;
    private final TopicRepository topicRepository;
    private final SentenceRepository sentenceRepository;
    // Inject EntityManager
    @PersistenceContext
    private final EntityManager entityManager;
    private final TtsService ttsService;
    private final MediaUploadService cloudinaryMediaUploadServiceImpl;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<WordResponse> getAllWords() {
        List<Word> words = wordRepository.findAll();
        return words.stream()
                .map(this::mapToWordResponse)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public WordResponse getWordById(Long id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        return mapToWordResponse(word);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WordResponse createWord(WordRequest request) {
        Word word = new Word();
        updateWordFromRequest(word, request);
        
        Word savedWord = wordRepository.save(word);
        return mapToWordResponse(savedWord);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public WordResponse updateWord(Long id, WordRequest request) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        
        updateWordFromRequest(word, request);
        Word updatedWord = wordRepository.save(word);
        return mapToWordResponse(updatedWord);
    }

    private void updateWordFromRequest(Word word, WordRequest request) {

        if (word.getEnglishText() == null || !word.getEnglishText().equalsIgnoreCase(request.getEnglishText())) {
            String englishText = request.getEnglishText();
            byte[] audioBytesData = ttsService.requestTextToSpeech(englishText);
            CloudinaryUrlResponse cloudinaryUrlResponse = cloudinaryMediaUploadServiceImpl.uploadAudio(audioBytesData);
            word.setAudioUrl(cloudinaryUrlResponse.getSecureUrl());
        }

        word.setEnglishText(request.getEnglishText());
        word.setVietnameseText(request.getVietnameseText());

        // Handle topic associations
        if (request.getTopicIds() != null) {
            List<Topic> topics = new ArrayList<>();
            for (Long topicId : request.getTopicIds()) {
                Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                topics.add(topic);
            }
            word.getTopics().clear();
            word.getTopics().addAll(topics); // Add this line to set topics on word side

            for (Topic topic : topics) {
                if (!topic.getWords().contains(word)) {
                    topic.getWords().add(word);
                }
            }
        }

        // Handle sentence associations
        if (request.getSentenceIds() != null) {
            List<Sentence> sentences = new ArrayList<>();
            for (Long sentenceId : request.getSentenceIds()) {
                Sentence sentence = sentenceRepository.findById(sentenceId)
                        .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                sentences.add(sentence);
            }
            word.getSentences().clear();
            word.getSentences().addAll(sentences);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWordById(Long id) {
        Word word = wordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Check if word is associated with a vocabulary exercise
        if (word.getVocabularyExercise() != null) {
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_vocabulary_exercise WHERE word_id = :wordId")
                    .setParameter("wordId", id)
                    .executeUpdate();
        }

        // Remove word from topics (clean up join table)
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_topic_word WHERE word_id = :wordId")
                .setParameter("wordId", id)
                .executeUpdate();

        // Clean word-sentence associations
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_word_sentence WHERE word_id = :wordId")
                .setParameter("wordId", id)
                .executeUpdate();

        // Finally delete the word
        wordRepository.deleteById(id);
    }

    @Override
    public WordImageResponse createOrUpdateImage(MultipartFile file, Long wordId) {
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new AppException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // TODO: chỉnh lại sau cho đỡ tốn tài nguyên cloud - đã xong
        // create new image and get back link
        CloudinaryUrlResponse cloudinaryUrlResponse = cloudinaryMediaUploadServiceImpl.uploadImage(file, 128, 128);
        String imageUrl = cloudinaryUrlResponse.getSecureUrl();

//        String imageUrl = "SAMPLE_IMAGE_URL";

        // set to topic
        word.setImageUrl(imageUrl);


        // save or update
        Word savedWord = wordRepository.save(word);

        return WordImageResponse.builder().imageUrl(savedWord.getImageUrl()).build();
    }

    private WordResponse mapToWordResponse(Word word) {
        List<Long> topicIds = word.getTopics().stream()
                .map(Topic::getId)
                .collect(Collectors.toList());

        List<Long> sentenceIds = word.getSentences().stream()
                .map(Sentence::getId)
                .collect(Collectors.toList());

        return WordResponse.builder()
                .id(word.getId())
                .englishText(word.getEnglishText())
                .vietnameseText(word.getVietnameseText())
                .imageUrl(word.getImageUrl())
                .audioUrl(word.getAudioUrl())
                .createdAt(word.getCreatedAt())
                .updatedAt(word.getUpdatedAt())
                .topicIds(topicIds)
                .sentenceIds(sentenceIds)
                .build();
    }

}
