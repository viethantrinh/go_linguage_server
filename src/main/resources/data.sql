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
    (1, 'Giới thiệu', 'chủ đề giới thiệu', 'http://res.cloudinary.com/golinguage/image/upload/v1741582250/071cd054-5d90-4b6a-86e3-22a392e4a9fb.webp', 1, false, now(), null),
    (1, 'Câu cơ bản', 'các loại câu cơ bản', 'http://res.cloudinary.com/golinguage/image/upload/v1741582254/8053962f-f5f2-4b8a-9084-6bba51b349e4.webp', 2, false, now(), null),
    (1, 'Số đếm & Gia đình', 'số đếm cơ bản và từ vựng về gia đình', 'http://res.cloudinary.com/golinguage/image/upload/v1741582247/bb930d26-15d2-443d-aa69-f0ba2f5a8dde.webp', 3, true, now(), null),
    (1, 'Số đếm & Tập đếm', 'số đếm nâng cao', 'http://res.cloudinary.com/golinguage/image/upload/v1741582252/c4e87654-8186-4caf-8add-3cc63d3be9f7.webp', 4, true, now(), null),
    (2, 'Địa điểm & Vị trí', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859407/3ce77ae1-5af1-4988-9f4d-69134caa483b.webp', 1, true, now() + interval '1 hour', null),
    (2, 'Dẫn đường', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859404/33488dc7-0db3-47a4-9757-99b3da461766.webp', 2, true, now() + interval '2 hour', null),
    (2, 'Công việc & Nơi làm việc', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859402/b1123965-60ce-4f1f-ae59-da768868ad6a.webp', 3, true, now() + interval '10 hour', null),
    (2, 'Tâm trạng & Cảm xúc', 'no des', 'https://res.cloudinary.com/golinguage/image/upload/v1741859971/2e06f143-9b2a-4629-821c-e7c0450c2b64.webp', 4, true, now() + interval '6 hour', null)
;

-- LESSON DATA
INSERT INTO tbl_lesson (topic_id, name, display_order, created_at, updated_at)
VALUES -- 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
    (1, 'Bài học 1', 1, now(), null),
    (1, 'Bài học 2', 2, now(), null),
    (1, 'Bài học 3', 3, now(), null),
    (1, 'Bài học 4', 4, now(), null),
    (1, 'Tập nói', 5, now(), null),
    (1, 'Kiểm tra', 6, now(), null),
    (2, 'Bài học 1', 1, now(), null),
    (2, 'Bài học 2', 2, now(), null),
    (2, 'Bài học 3', 3, now(), null),
    (2, 'Bài học 4', 4, now(), null),
    (2, 'Tập nói', 5, now(), null),
    (2, 'Kiểm tra', 6, now(), null);

-- USER_LESSON_ATTEMPT DATA
INSERT INTO tbl_user_lesson_attempt (user_id, lesson_id, xp_points_earned, go_points_earned, created_at, updated_at)
VALUES
    (1, 1, 2, 150, now(), null),
    (1, 2, 3, 200, now() + interval '1 hour', null),
    (1, 8, 1, 100, now() + interval '3 hour', null);
