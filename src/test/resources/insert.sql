INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('10', 'test1@mail.ru', 'first1', 'N', 'last1', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('2', 'test2@mail.ru', 'first2', 'N', 'last2', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('3', 'test3@mail.ru', 'first3', 'N', 'last3', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');
INSERT INTO `person`(`id`, `e_mail`, `first_name`, `is_blocked`, `last_name`, `password`, `reg_date`) VALUES ('4', 'test4@mail.ru', 'first4', 'N', 'last4', '$2a$10$EqHML.VBGRVxqylVOwETdO3.ueCbsCKF.UPO3AN16l1Fn2pYENPjW', '2020-04-06 06:10:51');

-- INSERT INTO `roles` (`id`, `name`) VALUES ('5', 'ROLE_USER');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('10', '2');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('2', '2');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('3', '2');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES ('4', '2');

INSERT INTO `dialog`(`id`) VALUES ('6');
-- INSERT INTO `dialog`(`id`) VALUES ('7');

INSERT INTO `dialog_user` VALUES('6', '10');
INSERT INTO `dialog_user` VALUES('6', '2');
-- INSERT INTO `dialog_user` VALUES('7', '1');
-- INSERT INTO `dialog_user` VALUES('7', '3');

INSERT INTO `message`(`id`, `author_id`, `message_text`, `read_status`, `recipient_id`, `time`, `dialog_id`) VALUES ('9', '10', 'hello world', 'SENT', '2', '2020-04-06 06:07:11', '6');

INSERT INTO `friendship_status` (`id`, `code`, `name`, `time`) VALUES ('30', 'FRIEND', 'Друзья', '2020-04-19 17:02:14');
INSERT INTO `friendship_status` (`id`, `code`, `name`, `time`) VALUES ('31', 'REQUEST', 'Запрос на добавление в друзья', '2020-04-19 18:31:35');

INSERT INTO `friendship` (`id`, `dst_person_id`, `src_person_id`, `status_id`) VALUES ('32', '10', '2', '30');
INSERT INTO `friendship` (`id`, `dst_person_id`, `src_person_id`, `status_id`) VALUES ('33', '10', '3', '30');
INSERT INTO `friendship` (`id`, `dst_person_id`, `src_person_id`, `status_id`) VALUES ('34', '2', '3', '31');

-- INSERT INTO `notification_type` VALUES ('1','POST','Новый пост');
-- INSERT INTO `notification_type` VALUES ('2','POST_COMMENT','Комментарий к посту');
-- INSERT INTO `notification_type` VALUES ('3','COMMENT_COMMENT','Ответ на комментарий');
-- INSERT INTO `notification_type` VALUES ('4','FRIEND_REQUEST','Запрос дружбы');
-- INSERT INTO `notification_type` VALUES ('5','MESSAGE','Личное сообщение');
-- INSERT INTO `notification_type` VALUES ('6','FRIEND_BIRTHDAY ','День рождения друга');

INSERT INTO `post` VALUES ('100', 'N', 'N', 'post text', '2020-04-19 17:02:14', 'Title1', '2');
INSERT INTO `post` VALUES ('101', 'N', 'N', 'post text', '2020-04-20 17:02:14', 'Title2', '2');

INSERT INTO `post_like` (`id`, `person_id`, `post_id`, `time`) VALUES ('70', '2', '100', '2020-04-06 06:07:11');
INSERT INTO `post_like` (`id`, `person_id`, `post_id`, `time`) VALUES ('71', '3', '100', '2020-04-06 06:07:11');

INSERT INTO `tag` VALUES ('1', 'tag1');
INSERT INTO `tag` VALUES ('2', 'gat2');

INSERT INTO `post2tag` VALUES ('100', '1');

INSERT INTO `post_comments` VALUES ('1', 'comment1', 'N', 'N', '1', '100', '2020-04-23 10:00:00', '2');
INSERT INTO `post_comments` VALUES ('2', 'comment2', 'N', 'N', '1', '100', '2020-04-23 11:00:00', '10');


