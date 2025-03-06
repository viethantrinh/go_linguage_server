-- USER DATA
INSERT INTO tbl_user (name, email, password, image_url, enabled, total_streak_points, total_xp_points,
                      total_gem_points, created_at, updated_at)
VALUES ('han', 'hntrnn12@gmail.com', '$2a$10$FOozZpsLs4czytM1kxKpR.46tcuu5E5L5EaUrDAnIM0vDcjyCZBRe', 'null-1', true, 0,
        0, 0, now(), null),
       ('hang', 'hntrnn13@gmail.com', '$2a$10$rSCAMW7Thggyo3OtawWXPe3b0RDkXtsSs2IBk2hMQXDy2PqW8UZ1.', 'null-2', true, 0,
        0, 0, now(), null);

-- ROLE DATA
INSERT INTO tbl_role (name, description, created_at, updated_at)
VALUES ('ADMIN', 'Admin can control anything in the system', now(), null);

INSERT INTO tbl_role (name, description, created_at, updated_at)
VALUES ('USER', 'User can only access to the learning material', now(), null);

-- USER_ROLE DATA
INSERT INTO tbl_user_role (user_id, role_id)
VALUES (1, 2), -- han is user
       (2, 1); -- hang is admin

-- SUBSCRIPTION DATA
INSERT INTO tbl_subscription (name, price, stripe_price_id, duration_in_month, created_at, updated_at)
VALUES ('1 tháng', 199000, 'price_1QzORUIYP7o7tjuBZkyOUoGG', 1, now(), null),
       ('6 tháng', 799000, 'price_1QzOSbIYP7o7tjuBfXxxgubc', 6, now(), null),
       ('12 tháng', 2399000, 'price_1QzOTsIYP7o7tjuBCcd92WnP', 12, now(), null),
       ('Trọn đời', 4999000, 'price_1QzOVxIYP7o7tjuBJBFcFr5u', 12000, now(), null);

