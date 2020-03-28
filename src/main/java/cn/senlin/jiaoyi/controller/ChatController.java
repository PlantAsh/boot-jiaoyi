package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.Message;
import cn.senlin.jiaoyi.entity.util.Reply;
import cn.senlin.jiaoyi.service.ChatService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chat")
@SessionAttributes({ "message", "sendUser", "messageUser" })
public class ChatController {
	
	@Resource
	private ChatService chatService;
	
	@RequestMapping(value = "/getMessages", method = RequestMethod.POST)
	public String getMessages(HttpSession session, HttpServletResponse response, String sendUser, ModelMap model) {
		try {
			String acceptUser = (String) session.getAttribute("UserAccount");
			List<Message> message2 = JSON.parseArray(JSON.toJSONString(session.getAttribute("message")), Message.class);
			String sendUser2 = (String) session.getAttribute("sendUser");
			List<Message> message;
			message = chatService.getMessage(sendUser, acceptUser);
			boolean flag = false;
			if(!CollectionUtils.isEmpty(message)) {
				flag = true;
			}
			if(message2 == null || sendUser2.equals("")) {
				model.addAttribute("message", message);
				model.addAttribute("sendUser", sendUser);
			} else if(message2.size() == message.size() && sendUser2.equals(sendUser)) {
				flag = false;
			} else {
				model.addAttribute("message", message);
				model.addAttribute("sendUser", sendUser);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			map.put("message", message);
			map.put("sendUser", sendUser);
			map.put("acceptUser", acceptUser);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getUser", method = RequestMethod.POST)
	public String getUser(HttpSession session, HttpServletResponse response, ModelMap model, String name) {
		try {
			String acceptUser = (String) session.getAttribute("UserAccount");
			List<Message> messageUser2 = JSON.parseArray(JSON.toJSONString(session.getAttribute("messageUser")), Message.class);
			List<Message> messageUser;
			messageUser = chatService.getUser(acceptUser);
			boolean flag = false;
			boolean flag2 = false;
			if(messageUser.size() > 0) {
				flag2 = true;
				flag = true;
			}
			if(messageUser2 == null) {
				model.addAttribute("messageUser", messageUser);
			} else if(messageUser.size() == messageUser2.size() && name.equals("true")) {
				flag = false;
			} else {
				model.addAttribute("messageUser", messageUser);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			map.put("flag2", flag2);
			map.put("acceptUser", acceptUser);
			map.put("messageUser", messageUser);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/closeMessages", method = RequestMethod.POST)
	public String closeMessages(HttpServletResponse response, ModelMap model) {
		try {
			String sendUser = "";
			model.addAttribute("sendUser", sendUser);
			Map<String, Object> map = new HashMap<>();
			map.put("flag", true);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public String sendMessage(HttpSession session, HttpServletResponse response, String text) {
		try {
			String acceptUser = (String) session.getAttribute("UserAccount");
			String sendUser = (String) session.getAttribute("sendUser");
			Message message = new Message();
			message.setMessageSend(acceptUser);
			message.setMessageAccept(sendUser);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			message.setMessageDate(sdf.format(date));
			message.setMessageState("未读");
			text = text.replace("\r\n", "<br/>");
			message.setMessage(text);
			String result = chatService.addMessage(message);
			boolean flag = false;
			if(result.equals("success")) {
				flag = true;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flag", flag);
			String json = JSONObject.toJSONString(map);
			response.setCharacterEncoding("UTF-8");
	        response.flushBuffer();
	        response.getWriter().write(json);
	        response.getWriter().flush();
	        response.getWriter().close();
			return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取未读消息数量
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getCount", method = RequestMethod.POST)
	@ResponseBody
	public Reply getCount(HttpSession session) {
		try {
			String acceptUser = (String) session.getAttribute("UserAccount");
			int count = chatService.getCount(acceptUser);
			Map<String, Object> map = new HashMap<>();
			map.put("count", count);

			return Reply.ok(map);
		} catch(Exception e) {
			log.error("获取未读消息数量失败：", e);
			return Reply.fail("获取未读消息数量失败！");
		}
	}

}
