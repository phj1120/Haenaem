package xyz.parkh.challenge.domain.challenge.model;

public enum ChallengeCategory {
    ENVIRONMENT("환경"), LEARNING("학습"), EXERCISE("운동");

    private final String name;

    ChallengeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
