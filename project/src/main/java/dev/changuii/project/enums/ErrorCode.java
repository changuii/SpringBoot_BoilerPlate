package dev.changuii.project.enums;

public enum ErrorCode {

    // 로그인, 회원가입, 유저관련
    DUPLICATION_EMAIL_EXCEPTION("이메일 중복 에러"),
    EMAIL_NOT_FOUND("존재하지 않는 이메일"),
    USER_NOT_FOUND("존재하지 않는 사용자"),
    DESIGNATION_NOT_OWN("소유하지 않은 칭호"),
    DESIGNATION_DUPLICATION("이미 존재하는 칭호"),


    // S3
    S3_IMAGE_PUT_EXCEPTION("이미지 업로드 중 에러"),
    S3_IMAGE_DELETE_EXCEPTION("이미지 삭제 중 에러"),
    S3_IMAGE_BASIC_DELETE_EXCEPTION("기본 이미지 삭제 에러"),
    NON_DELETE_IMAGE_EXCEPTION("삭제할 수 없는 이미지"),


    // wanted
    WANTED_ALREADY_PROGRESS("이미 wanted가 진행중입니다."),
    WANTED_NOT_FOUND("존재하지 않는 Wanted입니다."),

    // report
    REPORT_NOT_FOUND("존재하지 않는 report입니다."),

    // idempotent
    DUPLICATION_REQUEST("중복된 요청입니다."),


    //friendship
    REQUEST_NOT_FOUND("존재하지 않는 요청입니다."),


    //TOKEN
    INVALID_TOKEN("유효하지 않은 토큰입니다.");


    private String errorData;

    private ErrorCode(String errorData){
        this.errorData = errorData;
    }

    public String getErrorDescription(){
        return this.errorData;
    }

}
