package cn.senlin.jiaoyi.dto;

import cn.senlin.jiaoyi.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends User {
    private static final long serialVersionUID = 6815536575291494654L;

    /**
     * 旧密码
     */
    String oldPassword;

}
