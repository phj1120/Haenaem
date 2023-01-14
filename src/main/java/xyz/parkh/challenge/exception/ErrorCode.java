package xyz.parkh.challenge.exception;

public enum ErrorCode {
    NO_ID("존재 하지 않는 사용자"), NO_AUTH_ID("존재 하지 않는 아이디"), NO_EMAIL("존재 하지 않는 이메일"), NO_EXIST("존재하지 않음"),
    DIFFERENCE_PASSWORD("비밀번호 불일치"), DIFFERENCE_EMAIL("이메일 불일치"), DIFFERENCE_AUTH_ID("사용자 아이디 불일치"),
    NOT_NULL("필수 인자 부족"), NO_UNIQUE_VALUE("이미 존재 하는 값"),
    EXIST_EMAIL("이미 존재 하는 이메일"), EXIST_AUTH_ID("이미 존재 하는 사용자 아이디"),
    FILE_ERROR("파일 에러"), EXPIRED_JWT("토큰 인증 기간 만료"), UNAUTHORIZED("올바르지 않은 접근 권한"),
    INSUFFICIENT_POINT("잔액 부족"), ALREADY_APPLY ("이미 신청함"), NO_APPLY("신청 상태 아님"),
    NO_RECENT("최근 기록이 아님");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
