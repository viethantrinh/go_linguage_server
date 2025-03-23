-- USER DATA
INSERT INTO tbl_user (name, email, password, enabled, total_streak_points, total_xp_points,
                      total_go_points, created_at, updated_at)
VALUES
    ('han', 'hntrnn12@gmail.com', '$2a$10$FOozZpsLs4czytM1kxKpR.46tcuu5E5L5EaUrDAnIM0vDcjyCZBRe', true, 0,
        0, 0, now(), null),
    ('hang', 'hntrnn13@gmail.com', '$2a$10$rSCAMW7Thggyo3OtawWXPe3b0RDkXtsSs2IBk2hMQXDy2PqW8UZ1.', true, 0,
        0, 0, now(), null);

-- ROLE DATA
INSERT INTO tbl_role (name, description, created_at, updated_at)
VALUES ('ADMIN', 'Admin can control anything in the system', now(), null);

INSERT INTO tbl_role (name, description, created_at, updated_at)
VALUES ('USER', 'User can only access to the learning material', now(), null);

-- USER_ROLE DATA
INSERT INTO tbl_user_role (user_id, role_id)
VALUES
    (1, 2), -- han is user
    (2, 1); -- hang is admin


-- SUBSCRIPTION DATA
INSERT INTO tbl_subscription (name, price, stripe_price_id, duration_in_month, created_at, updated_at)
VALUES
    ('1 tháng', 199000, 'price_1QzORUIYP7o7tjuBZkyOUoGG', 1, now(), null),
    ('6 tháng', 799000, 'price_1QzOSbIYP7o7tjuBfXxxgubc', 6, now(), null),
    ('12 tháng', 2399000, 'price_1QzOTsIYP7o7tjuBCcd92WnP', 12, now(), null),
    ('Trọn đời', 4999000, 'price_1QzOVxIYP7o7tjuBJBFcFr5u', 12000, now(), null);

-- LEVEL DATA
INSERT INTO tbl_level (name, description, display_order, created_at, updated_at)
VALUES
    ('Người mới', 'Người mới bắt đầu học', 1, now(), null),       -- id = 1
    ('Trung bình', 'Người có kiến thức cơ bản', 2, now(), null);  -- id = 2

-- TOPIC DATA
INSERT INTO tbl_topic (level_id, name, description, image_url, display_order, is_premium, created_at, updated_at)
VALUES -- 1, 2, 3, 4
    (1, 'Giới thiệu', 'chủ đề giới thiệu', 'https://res.cloudinary.com/golinguage/image/upload/v1741582250/071cd054-5d90-4b6a-86e3-22a392e4a9fb.webp',
     1, false, now(), null),

    (1, 'Câu cơ bản', 'các loại câu cơ bản', 'https://res.cloudinary.com/golinguage/image/upload/v1741582254/8053962f-f5f2-4b8a-9084-6bba51b349e4.webp',
     2, false, now(), null),

    (1, 'Số đếm & Gia đình', 'số đếm cơ bản và từ vựng về gia đình', 'https://res.cloudinary.com/golinguage/image/upload/v1741582247/bb930d26-15d2-443d-aa69-f0ba2f5a8dde.webp',
     3, true, now(), null),

    (1, 'Số đếm & Tập đếm', 'số đếm nâng cao', 'https://res.cloudinary.com/golinguage/image/upload/v1741582252/c4e87654-8186-4caf-8add-3cc63d3be9f7.webp',
     4, true, now(), null),

    (2, 'Địa điểm & Vị trí', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859407/3ce77ae1-5af1-4988-9f4d-69134caa483b.webp',
     1, true, now() + interval '1 hour', null),

    (2, 'Dẫn đường', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859404/33488dc7-0db3-47a4-9757-99b3da461766.webp',
     2, true, now() + interval '2 hour', null),

    (2, 'Công việc & Nơi làm việc', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859402/b1123965-60ce-4f1f-ae59-da768868ad6a.webp',
     3, true, now() + interval '10 hour', null),

    (2, 'Tâm trạng & Cảm xúc', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859971/2e06f143-9b2a-4629-821c-e7c0450c2b64.webp',
     4, true, now() + interval '6 hour', null)
    ;


-- LESSON_TYPE DATA
INSERT INTO tbl_lesson_type (name, description, created_at, updated_at)
VALUES -- 1, 2, 3
    ('Bài học chung', 'Bài học chung kết hợp các bài tập để học từ vựng', now(), null),
    ('Bài học nói', 'Bài học nói cho luyện nói các từ vựng và câu thuộc phạm vi của bài học', now(), null),
    ('Bài học kiểm tra', 'Bài học kiểm tra tổng hợp các bài tập từ các bài học chung', now(), null)
    ;

-- LESSON DATA
INSERT INTO tbl_lesson (topic_id, lesson_type_id, name, display_order, created_at, updated_at)
VALUES -- 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
    (1, 1, 'Bài học 1', 1, now(), null),
    (1, 1, 'Bài học 2', 2, now(), null),
    (1, 1, 'Bài học 3', 3, now(), null),
    (1, 1, 'Bài học 4', 4, now(), null),
    (1, 2, 'Tập nói', 5, now(), null),
    (1, 3, 'Kiểm tra', 6, now(), null),
    (2, 1, 'Bài học 1', 1, now(), null),
    (2, 1, 'Bài học 2', 2, now(), null),
    (2, 1, 'Bài học 3', 3, now(), null),
    (2, 1, 'Bài học 4', 4, now(), null),
    (2, 2, 'Tập nói', 5, now(), null),
    (2, 3, 'Kiểm tra', 6, now(), null);

-- USER_LESSON_ATTEMPT DATA
INSERT INTO tbl_user_lesson_attempt (user_id, lesson_id, xp_points_earned, go_points_earned, created_at, updated_at)
VALUES
    (1, 1, 2, 150, now(), null),
    (1, 2, 3, 200, now() + interval '1 hour', null),
    (1, 8, 1, 100, now() + interval '3 hour', null)
    ;

-- ===================================== LEARNING MATERIAL ===================================== --
-- WORD DATA
INSERT INTO tbl_word (english_text, vietnamese_text, image_url, audio_url, created_at, updated_at)
VALUES -- 1, 2, 3, 4
    ('A woman', 'Phụ nữ', 'https://res.cloudinary.com/golinguage/image/upload/v1742090658/19b05679-b3aa-4e7c-ac29-4fab3c1523ce.webp',
     'https://res.cloudinary.com/golinguage/video/upload/v1742091557/de5fd42c-352b-4af0-bbeb-e70276c7d14b.ogg', now(), null),

    ('A man', 'Đàn ông', 'https://res.cloudinary.com/golinguage/image/upload/v1742090656/0a00d1e8-c072-4865-8918-e978caecf330.webp',
     'https://res.cloudinary.com/golinguage/video/upload/v1742091555/4bf483b2-3d90-4106-9384-eb861ff261dc.ogg', now(), null),

    ('A girl', 'Bé gái', 'https://res.cloudinary.com/golinguage/image/upload/v1742090653/089fdddc-e415-43f0-94de-9c807542b405.webp',
     'https://res.cloudinary.com/golinguage/video/upload/v1742091554/98d0d974-f535-47f9-b7b2-fa66800de605.ogg', now(), null),

    ('A boy', 'Bé trai', 'https://res.cloudinary.com/golinguage/image/upload/v1742090651/f75889b3-c013-4028-9bda-cfa6d8085039.webp',
     'https://res.cloudinary.com/golinguage/video/upload/v1742091552/a57d1771-c571-4f01-acee-d34ee36e11ec.ogg', now(), null)
    ;


-- SENTENCE DATA
INSERT INTO tbl_sentence (english_text, vietnamese_text, audio_url, created_at, updated_at)
VALUES
    -- 1
    ('I am a woman', 'Tôi là một người phụ nữ', 'https://res.cloudinary.com/golinguage/video/upload/v1742092142/e4c456f8-9eb1-4a11-a713-94019e20ce96.ogg', now(), null),
    -- 2
    ('You are a man', 'Bạn là một người đàn ông', 'https://res.cloudinary.com/golinguage/video/upload/v1742092145/d26021a3-766a-41d4-b3a2-cfed44508729.ogg', now(), null),
    -- 3
    ('She is a girl', 'Em ấy là một bé gái', 'https://res.cloudinary.com/golinguage/video/upload/v1742092144/a0d53536-e648-46e6-89fa-78955f506c75.ogg', now(), null),
    -- 4
    ('He is a boy', 'Em ấy là một bé trai', 'https://res.cloudinary.com/golinguage/video/upload/v1742092140/66ea5d46-294d-454b-a2e1-26435ec13da7.ogg', now(), null);


-- WORD_SENTENCE DATA
INSERT INTO tbl_word_sentence (word_id, sentence_id)
VALUES
       (1, 1),
       (2, 2),
       (3, 3),
       (4, 4)
        ;

-- TOPIC_WORD DATA
INSERT INTO tbl_topic_word
    (topic_id, word_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

-- TOPIC_SENTENCE DATA
INSERT INTO tbl_topic_sentence
(topic_id, sentence_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

-- EXERCISE_TYPE DATA
INSERT INTO tbl_exercise_type (name, description, created_at, updated_at)
VALUES -- 1, 2, 3, 4, 5, 6
       ('từ vựng', 'Bài tập về tìm hểu từ vựng mới', now(), null),
       ('trắc nghiệm', 'Bài tập về chọn đáp án trắc nghiệm đúng', now(), null),
       ('nối từ', 'Bài tập về nối từ với nghĩa tương ứng', now(), null),
       ('sắp xếp từ thành câu', 'Bài tập về sắp xếp từ thành câu đúng tương ứng', now(), null),
       ('phát âm', 'Bài tập về luyện phát âm các từ hoặc câu', now(), null),
       ('hội thoại', 'Bài tập về đoạn hội thoại như điền vào ô trống, đọc theo', now(), null)
        ;

-- EXERCISE DATA
INSERT INTO tbl_exercise(lesson_id, exercise_type_id, instruction, display_order, created_at, updated_at)
VALUES
    -- 1
    (1, 1, 'Từ mới! ấn để phát lại', 1, now(), null),
    -- 2
    (1, 1, 'Từ mới! ấn để phát lại', 2, now(), null),
    -- 3
    (1, 2, 'Lựa chọn câu trả lời đúng', 3, now(), null),
    -- 4
    (1, 1, 'Từ mới! ấn để phát lại', 4, now(), null),
    -- 5
    (1, 2, 'Lựa chọn câu trả lời đúng', 5, now(), null),
    -- 6
    (1, 3, 'Nối những thẻ này', 6, now(), null),
    -- 7
    (1, 1, 'Từ mới! ấn để phát lại', 7, now(), null),
    -- 8
    (1, 2, 'Lựa chọn câu trả lời đúng', 8, now(), null),
    -- 9
    (1, 3, 'Nối những thẻ này', 9, now(), null),
    -- 10
    (1, 2, 'Lựa chọn câu trả lời đúng', 10, now(), null),
    -- 11
    (1, 4, 'Dịch câu này bằng cách sắp xếp', 11, now(), null),
    -- 12
    (1, 4, 'Dịch câu này bằng cách sắp xếp', 12, now(), null),
    -- 13
    (1, 4, 'Dịch câu này bằng cách sắp xếp', 13, now(), null),
    -- 14
    (1, 2, 'Lựa chọn câu trả lời đúng', 14, now(), null),
    -- 15
    (1, 6, 'Nghe đoạn hội thoại sau và làm bài', 15, now(), null)
    ;

-- VOCABULARY_EXERCISE DATA
INSERT INTO tbl_vocabulary_exercise(exercise_id, word_id, created_at, updated_at)
VALUES
    (1, 1, now(), null),
    (2, 2, now(), null),
    (4, 3, now(), null),
    (7, 4, now(), null)
    ;

-- MULTIPLE_CHOICE_EXERCISE DATA
INSERT INTO tbl_multiple_choice_exercise
    (exercise_id, word_id, sentence_id, question_type, source_language, target_language, created_at, updated_at)
VALUES
    -- 1
    (3, 1, null, 'word', 'vietnamese', 'english', now(), null),
    -- 2
    (5, 2, null, 'word', 'vietnamese', 'english', now(), null),
    -- 3
    (8, 3, null, 'word', 'vietnamese', 'english', now(), null),
    -- 4
    (10, null, 2, 'sentence', 'english', 'english', now(), null),
    -- 5
    (14, null, 1, 'sentence', 'english', 'vietnamese', now(), null)
    ;

-- MULTIPLE_CHOICE_OPTION DATA
INSERT INTO tbl_multiple_choice_option
    (multiple_choice_exercise_id, word_id, sentence_id, option_type, is_correct, created_at, updated_at)
VALUES
    -- 1 : id của multiple_choice_exercise_id
    (1, 1, null, 'word', true, now(), null),
    (1, 2, null, 'word', false, now(), null),
    (1, 3, null, 'word', false, now(), null),
    (1, 4, null, 'word', false, now(), null),
    -- 2 : id của multiple_choice_exercise_id
    (2, 4, null, 'word', false, now(), null),
    (2, 1, null, 'word', false, now(), null),
    (2, 2, null, 'word', true, now(), null),
    (2, 3, null, 'word', false, now(), null),
    -- 3 : id của multiple_choice_exercise_id
    (3, 2, null, 'word', false, now(), null),
    (3, 3, null, 'word', true, now(), null),
    (3, 4, null, 'word', false, now(), null),
    (3, 1, null, 'word', false, now(), null),
    -- 4 : id của multiple_choice_exercise_id
    (4, null, 1, 'sentence', false, now(), null),
    (4, null, 2, 'sentence', true, now(), null),
    (4, null, 3, 'sentence', false, now(), null),
    (4, null, 4, 'sentence', false, now(), null),
    -- 5 : id của multiple_choice_exercise_id
    (5, null, 4, 'sentence', false, now(), null),
    (5, null, 3, 'sentence', false, now(), null),
    (5, null, 2, 'sentence', false, now(), null),
    (5, null, 1, 'sentence', true, now(), null)
    ;

-- MATCHING_EXERCISE DATA
INSERT INTO tbl_matching_exercise
    (exercise_id, created_at, updated_at)
VALUES
    -- 1
    (6, now(), null),
    -- 2
    (9, now(), null)
    ;

-- MATCHING_PAIR DATA
INSERT INTO tbl_matching_pair
    (matching_exercise_id, word_id, created_at, updated_at)
VALUES
    -- 1
    (1, 1, now(), null),
    (1, 2, now(), null),
    (1, 3, now(), null),
    -- 2
    (2, 1, now(), null),
    (2, 2, now(), null),
    (2, 4, now(), null)
    ;

-- WORD_ARRANGEMENT_EXERCISE DATA
INSERT INTO tbl_word_arrangement_exercise
    (sentence_id, exercise_id, source_language, target_language, created_at, updated_at)
VALUES
    -- 1
    (1, 11, 'english', 'vietnamese', now(), null),
    -- 2
    (2, 12, 'english', 'vietnamese', now(), null),
    -- 3
    (3, 13, 'vietnamese', 'english', now(), null)
    ;

-- WORD_ARRANGEMENT_OPTION DATA
INSERT INTO tbl_word_arrangement_option
    (word_arrangement_exercise_id, word_text, is_distractor, correct_position, created_at, updated_at)
VALUES
    -- 1 : id của word_arrangement_exercise_id
    (1, 'nữ', false, 5, now(), null),
    (1, 'người', false, 3, now(), null),
    (1, 'ông', true, -1, now(), null),
    (1, 'tiếng', true, -1, now(), null),
    (1, 'một', false, 2, now(), null),
    (1, 'nhật', true, -1, now(), null),
    (1, 'đàn', true, -1, now(), null),
    (1, 'phụ', false, 4, now(), null),
    (1, 'tôi', false, 0, now(), null),
    (1, 'là', false, 1, now(), null),
    -- 2 : id của word_arrangement_exercise_id
    (2, 'mười', true, -1, now(), null),
    (2, 'ông', false, 5, now(), null),
    (2, 'bạn', false, 0, now(), null),
    (2, 'nhật', true, -1, now(), null),
    (2, 'người', false, 3, now(), null),
    (2, 'là', false, 1, now(), null),
    (2, 'nữ', true, -1, now(), null),
    (2, 'hai', true, -1, now(), null),
    (2, 'phụ', true, -1, now(), null),
    (2, 'đàn', false, 4, now(), null),
    (2, 'một', false, 2, now(), null),
    -- 3 : id của word_arrangement_exercise_id
    (3, 'is', false, 1, now(), null),
    (3, 'girl', false, 3, now(), null),
    (3, 'she', false, 0, now(), null),
    (3, 'a', false, 2, now(), null)
    ;

-- DIALOGUE_EXERCISE DATA
INSERT INTO tbl_dialogue_exercise
(exercise_id, context, created_at, updated_at)
VALUES
    -- 1
    (15, 'Hai người chào hỏi và tạm biệt', now(), null);

-- DIALOGUE_EXERCISE_LINE DATA
INSERT INTO tbl_dialogue_exercise_line
(dialogue_exercise_id, speaker, english_text, vietnamese_text, audio_url, display_order, has_blank, blank_word, created_at, updated_at)
VALUES
    -- 1 : id của dialogue_exercise_id
    (1, 'A', 'Good afternoon!', 'Chào anh!', 'https://res.cloudinary.com/golinguage/video/upload/v1742138948/485df7a4-b664-4136-8a3d-b8ecac9150d4.ogg', 1, false, null, now(), null),
    (1, 'B', 'Good afternoon!', 'Chào chị!', 'https://res.cloudinary.com/golinguage/video/upload/v1742138950/7918b7a6-42c4-425d-8dbd-93e0edbc9ffb.ogg', 2, false, null, now(), null),
    (1, 'A', 'How are you?', 'Anh khỏe không?', 'https://res.cloudinary.com/golinguage/video/upload/v1742138954/7b262b96-c1ee-45c5-8c68-20a0e0fc236f.ogg', 3, true, 'How', now(), null),
    (1, 'B', 'I''m fine.', 'Tôi khỏe.', 'https://res.cloudinary.com/golinguage/video/upload/v1742138958/56200d35-37ac-4ecf-9bca-ee91bdc2d0fb.ogg', 4, false, null, now(), null),
    (1, 'B', 'And you!', 'Còn chị?', 'https://res.cloudinary.com/golinguage/video/upload/v1742138944/9484b157-3b2f-4b84-9217-22015849b0af.ogg', 5, false, null, now(), null),
    (1, 'A', 'I am fine.', 'Tôi khỏe.', 'https://res.cloudinary.com/golinguage/video/upload/v1742138957/cfe63a1f-d47b-48a0-8ec5-af9b50c86823.ogg', 6, true, 'I', now(), null),
    (1, 'A', 'Thank you.', 'Cảm ơn', 'https://res.cloudinary.com/golinguage/video/upload/v1742138962/6f9726a6-1e71-4289-9e2f-5b20669b42fd.ogg', 7, false, null, now(), null),
    (1, 'A', 'Goodbye!', 'Tạm biệt!', 'https://res.cloudinary.com/golinguage/video/upload/v1742138946/cece8a42-875f-4691-b04f-3ca738042d57.ogg', 8, false, null, now(), null),
    (1, 'B', 'See you later!', 'Hẹn gặp lại!', 'https://res.cloudinary.com/golinguage/video/upload/v1742138960/755a8727-8ef1-4e9b-8e70-fbae7b92694b.ogg', 9, true, 'you', now(), null),
    (1, 'A', 'Good luck!', 'Chúc may mắn!', 'https://res.cloudinary.com/golinguage/video/upload/v1742138952/67109268-15b4-45f4-8db8-a5af5c726ac6.ogg', 10, false, null, now(), null)
;

