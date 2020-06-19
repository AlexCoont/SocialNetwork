    Сервер tomcat настроен на 8086 порт
1.  Для запуска приложения используется два профиля: dev, prod<br/>
    Данные значения указываются в VM Options свойством -Dspring.profiles.active=<br/>
    При запуске через spring-boot-maven-plugin значение меняется в pom \<profile>dev\</profile><br/><br/>
    
2. sql-для устновки типов нотификаций
INSERT INTO notification_type VALUES (1,'POST','Новый пост'),
(2,'POST_COMMENT','Комментарий к посту'),
(3,'COMMENT_COMMENT','Ответ на комментарий'),
(4,'FRIEND_REQUEST','Запрос дружбы'),
(5,'MESSAGE','Личное сообщение'),
(6,'FRIEND_BIRTHDAY ','День рождения друга');
