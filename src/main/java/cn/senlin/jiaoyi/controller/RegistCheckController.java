package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.User;
import cn.senlin.jiaoyi.entity.util.Reply;
import cn.senlin.jiaoyi.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/registCheck")
public class RegistCheckController {
	@Resource
	private UserService userService;

	/**
	 * 检查用户名
	 *
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkUserAccount", method = RequestMethod.POST)
	@ResponseBody
	public Reply checkUserName(User user) throws Exception {
		//检验用户名是否存在
		User us = userService.loadUser(user.getUserAccount());
	    //用户名是否存在的标志
	    boolean flag = true;
	    if(us == null){
	    	flag = false;
	    }		
		//将数据转换成json
		Map<String,Object> map = new HashMap<>();
		map.put("flag", flag);

		return Reply.ok(map);
	}

}
