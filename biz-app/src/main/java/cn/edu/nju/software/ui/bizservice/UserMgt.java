package cn.edu.nju.software.ui.bizservice;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.fabricservice.bean.SampleUser;

public interface UserMgt {
    /**
     * 使用FabricCA注册一个新用户，并将用户信息持久化保存
     *
     * @return
     */
    BizResponse<String> userRegister(String username);

    BizResponse check(String username, String password);

}
