package project.models.enums;

public enum FriendshipStatusCode {
    REQUEST("Запрос на добавление в друзья"),
    FRIEND("Друзья"),
    BLOCKED("Пользователь в чёрном списке"),
    DECLINED("Запрос на добавление в друзья отклонён"),
    SUBSCRIBED("Подписан");

    private final String code2Name;

    FriendshipStatusCode(String code2Name) {
        this.code2Name = code2Name;
    }

    public String getCode2Name() {
        return code2Name;
    }
}
