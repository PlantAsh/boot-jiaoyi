package cn.senlin.jiaoyi.dto;

import cn.senlin.jiaoyi.entity.User;

public class UserDTO extends User {
    private static final long serialVersionUID = 6815536575291494654L;

    /**
     * 旧密码
     */
    String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

}
