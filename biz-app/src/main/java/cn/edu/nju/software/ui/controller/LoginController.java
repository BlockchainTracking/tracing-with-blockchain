package cn.edu.nju.software.ui.controller;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;
import cn.edu.nju.software.ui.bizservice.UserMgt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Daniel
 * @since 2018/5/13 9:10
 */
@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    UserMgt userMgt;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public BizResponse login(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session) {
        BizResponse bizResponse = userMgt.check(username, password);
        if (bizResponse.getRespStatus().isSuccess()) {
            session.setAttribute("login", true);
        }
        return bizResponse;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BizResponse logout(HttpSession session) {
        session.removeAttribute("login");
        return BizResponse.createSuccess(null, "success");
    }
}
