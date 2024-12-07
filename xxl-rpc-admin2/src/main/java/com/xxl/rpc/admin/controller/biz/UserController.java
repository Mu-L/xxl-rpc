package com.xxl.rpc.admin.controller.biz;

import com.xxl.rpc.admin.annotation.Permission;
import com.xxl.rpc.admin.constant.enums.RoleEnum;
import com.xxl.rpc.admin.constant.enums.UserStatuEnum;
import com.xxl.rpc.admin.model.dto.LoginUserDTO;
import com.xxl.rpc.admin.model.dto.UserDTO;
import com.xxl.rpc.admin.service.UserService;
import com.xxl.rpc.admin.service.impl.LoginService;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import com.xxl.tool.response.ResponseBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:39:50
 */
@Controller
@RequestMapping("/user")
public class UserController {
    public static final String ADMIN_ROLE = "ADMIN";

    @Resource
    private UserService userService;
    @Resource
    private LoginService loginService;

    @RequestMapping
    @Permission(role = ADMIN_ROLE)
    public String index(Model model) {

        model.addAttribute("UserStatuEnum", UserStatuEnum.values());
        model.addAttribute("RoleEnum", RoleEnum.values());

        return "biz/user";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @Permission(role = ADMIN_ROLE)
    public Response<PageModel<UserDTO>> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                                 @RequestParam(required = false, defaultValue = "10") int length,
                                                 String username,
                                                 @RequestParam(required = false, defaultValue = "-1") int status) {

        PageModel<UserDTO> pageModel = userService.pageList(start, length, username, status);
        return new ResponseBuilder<PageModel<UserDTO>>().success(pageModel).build();
    }

    @RequestMapping("/add")
    @ResponseBody
    @Permission(role = ADMIN_ROLE)
    public Response<String> add(UserDTO xxlJobUser) {
        return userService.insert(xxlJobUser);
    }

    @RequestMapping("/update")
    @ResponseBody
    @Permission(role = ADMIN_ROLE)
    public Response<String> update(HttpServletRequest request, UserDTO xxlJobUser) {
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.update(xxlJobUser, loginUser);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Permission(role = ADMIN_ROLE)
    public Response<String> delete(HttpServletRequest request,
                                   @RequestParam("ids[]") List<Integer> ids) {
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.deleteByIds(ids, loginUser);
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    @Permission
    public Response<String> updatePwd(HttpServletRequest request, String password){
        LoginUserDTO loginUser = loginService.getLoginUser(request);
        return userService.updatePwd(loginUser, password);
    }

}
