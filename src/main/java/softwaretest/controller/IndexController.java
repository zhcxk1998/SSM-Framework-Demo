package softwaretest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import softwaretest.entity.LoginInfo;
import softwaretest.error.BusinessException;
import softwaretest.error.EmBusinessError;
import softwaretest.error.NotLoginException;
import softwaretest.response.CommonReturnType;
import softwaretest.service.IndexService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("index")
@RequestMapping("/index")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class IndexController extends BaseController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private IndexService indexService;

    /* 获取登录信息列表 */
    @GetMapping("/login_list")
    @ResponseBody
    public CommonReturnType getLoginList() throws NotLoginException {
        this.validateLogin();
        List<LoginInfo> loginInfoList = indexService.getLoginList();
        return CommonReturnType.create(loginInfoList);
    }

    private void validateLogin() throws NotLoginException {
        Boolean isLogin = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
        /* 如果没有登录就不返回值 */
        if (isLogin == null) {
            throw new NotLoginException(EmBusinessError.USER_NOT_LOGIN);
        }
    }
}
