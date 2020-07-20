package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.dto.UserDTO;
import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.User;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.enums.SystemConstantEnum;
import cn.senlin.jiaoyi.service.InformationService;
import cn.senlin.jiaoyi.service.UserService;
import cn.senlin.jiaoyi.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author swu
 * @date 2020-03-31
 */
@Controller
@RequestMapping("/user")
public class UserController {
	Logger log = LoggerFactory.getLogger(UserController.class);

	@Resource
	private UserService userService;
	@Resource
	private InformationService informationService;

	/**
	 * 用户登录
	 *
	 * @param request
	 * @param response
	 * @param userDTO
	 * @return
	 */
	@PostMapping(value = "/login")
	public ModelAndView Login(UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("user/login");

		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();

			User user = userService.loadUser(userDTO.getUserAccount());
			if(StringUtils.isEmpty(userDTO.getUserPassword()) || user == null
					|| !Md5Utils.encode(userDTO.getUserPassword()).equals(user.getUserPassword())) {
				out.print("<script>alert('账号或密码错误')</script>");
				out.flush();
				return view;
			} else {
				Map<String, Object> model = new HashMap<>();
				String sendUser = "";
				List<InformationCode> inFloor = informationService.loadByType("floor");
				String level = "user_level" + user.getUserLevel();
				UserInformation usin = informationService.loadInformation(user.getUserAccount());

				HttpSession session = request.getSession();
				session.setAttribute(SystemConstantEnum.SESSION_USER_KEY.getCode(), user);
				session.setAttribute(SystemConstantEnum.USER_ACCOUNT.getCode(), user.getUserAccount());
				session.setAttribute(SystemConstantEnum.USER_INFORMATION.getCode(), usin);
				session.setAttribute(SystemConstantEnum.IN_FLOOR.getCode(), inFloor);
				session.setAttribute(SystemConstantEnum.USER_LEVEL.getCode(), level);

				Cookie cookie = new Cookie("senlinUser", null);
//				cookie.setMaxAge(60 * 60 * 24 * 30); //cookie保存30天
				cookie.setMaxAge(-1); //浏览器关闭时cookie删除
				cookie.setPath("/");
				response.addCookie(cookie);

				model.put("inFloor", inFloor);
				model.put("userAccount", user.getUserAccount());
				model.put("userInformation", usin);
				model.put("userLevel", level);

				view.addObject(model);
				view.setViewName("user/" + level);
				return view;
			}
		} catch (Exception e) {
			log.error("用户登录报错：", e);
		}
		return view;
	}

	/**
	 * 用户注册
	 *
	 * @param response
	 * @param userDTO
	 * @return
	 */
	@PostMapping(value = "/regist")
	public String Regist(HttpServletResponse response, UserDTO userDTO) {
		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();

			userDTO.setUserLevel("1");
			userDTO.setUserPassword(Md5Utils.encode(userDTO.getUserPassword()));
			String result = userService.addUser(userDTO);
			
			if (!result.equals("success")) {
				out.print("<script>alert('" + result + "')</script>");
				out.flush();
				return "user/regist";
			}else {
				return "user/login";
			}
		} catch (Exception e) {
			log.error("用户注册报错：", e);
			return "user/regist";
		}
	}

	/**
	 * 退出登录
	 *
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/cancel")
	public String cancel(HttpServletRequest request) {
		try {
			 Enumeration<String> em = request.getSession().getAttributeNames();
			 while(em.hasMoreElements()){   
				 request.getSession().removeAttribute(em.nextElement());
			 }
			return "user/login";
		} catch (Exception e) {
			log.error("退出登录报错：", e);
			return "user/login";
		}
	}

	/**
	 * 修改密码
	 *
	 * @param session
	 * @param response
	 * @param userDTO
	 * @return
	 */
	@PostMapping(value = "/modifyPassword")
	public String modifyPassword(HttpSession session, HttpServletResponse response, UserDTO userDTO) {
		response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

		try {
			PrintWriter out = response.getWriter();

			String level = (String) session.getAttribute(SystemConstantEnum.USER_LEVEL.getCode());
			String userAccount = (String) session.getAttribute(SystemConstantEnum.USER_ACCOUNT.getCode());
			User us = userService.loadUser(userAccount);
			if(!us.getUserPassword().equals(Md5Utils.encode(userDTO.getOldPassword()))) {
				out.print("<script>alert('原密码错误')</script>");
				out.flush();
				return "user/" + level;
			} else {
				userDTO.setUserAccount(userAccount);
				userDTO.setUserPassword(Md5Utils.encode(userDTO.getUserPassword()));
				String result = userService.updatePassword(userDTO);
				if (!result.equals("success")) {
					out.print("<script>alert('" + result + "')</script>");
					out.flush();
				}

				us.setUserPassword(userDTO.getUserPassword());
				session.setAttribute(SystemConstantEnum.SESSION_USER_KEY.getCode(), us);

				return "user/" + level;
			}
		} catch (Exception e) {
			log.error("修改密码报错：", e);
			return "user/login";
		}
	}

}
