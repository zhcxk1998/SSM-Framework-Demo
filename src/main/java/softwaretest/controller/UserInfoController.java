package softwaretest.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import softwaretest.controller.viewobject.UserInfoVO;
import softwaretest.entity.LoginInfo;
import softwaretest.error.BusinessException;
import softwaretest.error.EmBusinessError;
import softwaretest.error.NotFoundException;
import softwaretest.response.CommonReturnType;
import softwaretest.service.UserInfoService;
import softwaretest.service.model.UserInfoModel;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("userInfo")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /* 获取全部用户的信息 */
    @GetMapping("/")
    @ResponseBody
    public CommonReturnType getUserList() {
        List<UserInfoModel> userInfoModelList = userInfoService.getUserList();

        userInfoModelList.stream().map(userInfoModel -> {
            UserInfoVO userInfoVO = this.convertFromModel(userInfoModel);
            return userInfoVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(userInfoModelList);
    }

    /* 根据id获取用户信息 */
    @GetMapping("/{id}")
    @ResponseBody
    public CommonReturnType getUser(@PathVariable("id") Integer id) throws BusinessException {
        /* 调用service服务获取对应id的用户对象返回给前端 */
        UserInfoModel userInfoModel = userInfoService.getUserById(id);

        /* 若获取的对应用户信息不存在 */
        if (userInfoModel == null) {
            throw new NotFoundException(EmBusinessError.USER_NOT_EXIST);
        }

        /* 将核心领域模型用户对象转化为可供UI使用的viewobject */
        UserInfoVO userInfoVO = convertFromModel(userInfoModel);

        /* 返回通用对象 */
        return CommonReturnType.create(userInfoVO);
    }

    /* 用户注册 */
    @PostMapping("/register")
    @ResponseBody
    public CommonReturnType userRegister(@RequestBody Map<String, String> requestBody) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        String idCard = requestBody.get("idCard");

        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserName(username);
        userInfoModel.setEncrptPassword(this.encodeBySha1(password));
        userInfoModel.setIdCard(idCard);

        userInfoService.register(userInfoModel);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("msg", "注册成功");

        return CommonReturnType.create(responseData);
    }

    /* 用户登录 */
    @PostMapping("/login")
    @ResponseBody
    public CommonReturnType userLogin(@RequestBody Map<String, String> requestBody) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        /* 入参效验 */
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        /* 用户登录服务，用来效验用户登录是否合法 */
        UserInfoModel userInfoModel = userInfoService.validateLogin(username, this.encodeBySha1(password));
        /* 将登录凭证加入到用户登录成功的session内 */
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userInfoModel);
        /* 记录用户登录信息 */
        userInfoService.recordLogin(userInfoModel);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("msg", "登陆成功");

        return CommonReturnType.create(responseData);
    }

    /* 用户修改密码 */
    @PostMapping("/modify")
    @ResponseBody
    public CommonReturnType modifyPassword(@RequestBody Map<String, String> requestBody) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        String username = requestBody.get("username");
        String prePassword = requestBody.get("prePassword");
        String newPassword = requestBody.get("newPassword");
        String idCard = requestBody.get("idCard");

        /* 入参效验 */
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(prePassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(idCard)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserInfoModel userInfoModel = userInfoService.validateModify(username, this.encodeBySha1(prePassword), idCard);

        /* 若通过验证，则将加密后的密码覆盖掉原先的密码 */
        userInfoModel.setEncrptPassword(this.encodeBySha1(newPassword));
        userInfoService.modifyPassword(userInfoModel);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("msg", "修改成功");

        return CommonReturnType.create(responseData);
    }

    private String encodeBySha1(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        /* 确定一个计算方法 */
        MessageDigest sha1 = MessageDigest.getInstance("Sha1");
        return Base64.encodeBase64String(sha1.digest(string.getBytes("utf-8")));
    }

    private UserInfoVO convertFromModel(UserInfoModel userInfoModel) {
        if (userInfoModel == null) {
            return null;
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(userInfoModel, userInfoVO);
        return userInfoVO;
    }
}
