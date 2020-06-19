insert into roles (name) values("ROLE_ADMIN");
insert into roles (name) values("ROLE_USER");

insert into notification_type (code, name) values ("POST", "Новый пост");
insert into notification_type (code, name) values ("POST_COMMENT", "Комментарий к посту");
insert into notification_type (code, name) values ("COMMENT_COMMENT", "Ответ на комментарий");
insert into notification_type (code, name) values ("FRIEND_REQUEST", "Запрос дружбы");
insert into notification_type (code, name) values ("MESSAGE", "Личное сообщение");
insert into notification_type (code, name) values ("FRIEND_BIRTHDAY", "День рождения друга");