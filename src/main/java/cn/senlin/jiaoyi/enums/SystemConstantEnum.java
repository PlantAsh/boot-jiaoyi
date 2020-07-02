package cn.senlin.jiaoyi.enums;

public enum SystemConstantEnum {
    /**
     * 用户信息
     */
    SESSION_USER_KEY("sessionUser"),

    /**
     * 用户详细信息
     */
    USER_INFORMATION("userInformation"),

    /**
     * 用户账号
     */
    USER_ACCOUNT("userAccount"),

    /**
     * 用户类型
     */
    USER_LEVEL("userLevel"),

    /**
     * 楼层字典
     */
    IN_FLOOR("inFloor");

    private String code;

    SystemConstantEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
