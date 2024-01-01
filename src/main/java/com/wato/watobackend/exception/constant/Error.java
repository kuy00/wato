package com.wato.watobackend.exception.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {

    SERVER_ERROR(999, "Sever Error"),

    EMPTY_PARAM(1001, "빈 파라미터"),
    IMAGE_UPLOAD(1002, "이미지 업로드 오류"),
    EMPTY_DATA(1003, "빈 데이터"),

    NOT_EXIST_ANNOUNCEMENT(2001, "존재하지 않는 공지사항입니다."),
    NOT_EXIST_FAQ(2002, "존재하지 않는 도움말입니다."),
    NOT_EXIST_BANNER(2003, "존재하지 않는 배너입니다."),
    NOT_EXIST_CATEGORY(2004, "존재하지 않는 카테고리입니다."),
    NOT_EXIST_COUNTRY(2005, "존재하지 않는 국가입니다."),
    NOT_EXIST_POST(2006, "존재하지 않는 게시글입니다."),
    NOT_EXIST_COMMENT(2007, "존재하지 않는 댓글입니다."),
    NOT_EXIST_IMAGE(2008, "존재하지 않는 이미지입니다."),
    NOT_NOTIFICATION_TYPE(2009, "존재하지 않는 알림유형입니다."),
    DELETED_POST(2500, "삭제된 게시글입니다."),

    NOT_EXIST_USER(3001, "존재하지 않는 유저입니다."),
    NOT_EXIST_SETTING(3002, "존재하지 않는 설정입니다."),
    NICKNAME_ALREADY_USED(3003, "이미 사용 중인 닉네임입니다."),
    NICKNAME_LENGTH_EXCEEDED(3004, "사용가능한 닉네임 글자수를 초과하였습니다."),
    EMAIL_ALREADY_USED(3005, "이미 사용 중인 이메일입니다."),
    TOKEN_VALID_FAILED(3006, "토큰 검증 실패"),
    AUTH_TYPE_MISMATCH(3007, "인증 타입 불일치"),
    INVALID_DATA(3008, "잘못된 데이터"),
    AUTH_FAILED(3009, "인증 실패"),
    SNS_LOGIN_EMAIL(3009, "SNS 로그인 이메일입니다."),
    POST_OWNER(3010, "게시글 소유자 입니다."),
    NOT_POST_OWNER(3010, "게시글 소유자가 아닙니다."),
    USER_SELF_BLOCK(3011, "자기 자신을 차단할 수 없습니다."),
    NOT_COMMENT_OWNER(3012, "댓글 소유자가 아닙니다."),

    UNAUTHORIZED(4001, "인증이 필요합니다."),
    NO_PERMISSION(4001, "권한이 없습니다."),
    ;

    private int code;

    private String message;
}
