package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.common.pojo.bizservice.UIItemGetRequest;
import cn.edu.nju.software.ui.bizservice.IdentityAuthority;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Daniel
 * @since 2018/5/2 17:01
 */
@Controller
@RequestMapping("/usr")
public class UserController {
    @Autowired
    IdentityAuthority identityAuthority;

    @RequestMapping(value = "/code", method = RequestMethod.POST)
    @ApiOperation(value = "")
    public BizResponse<String> getTracingItemInfo(
            @ApiParam(value = "用户名", required = true)
            @RequestBody String username,
            @ApiParam(value = "密码", required = true)
            @RequestBody String password) {
        return identityAuthority.generateAuthorityCode(username, password);
    }
}
