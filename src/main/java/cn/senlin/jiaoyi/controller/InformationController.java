package cn.senlin.jiaoyi.controller;

import cn.senlin.jiaoyi.entity.InformationCode;
import cn.senlin.jiaoyi.entity.UserInformation;
import cn.senlin.jiaoyi.service.InformationService;
import cn.senlin.jiaoyi.util.PropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/information")
@SessionAttributes({ "inFloor", "inMajor", "userInformation", "UserAccount", "userLevel"})
public class InformationController {

	@Resource
	private InformationService informationService;
	@Resource
	private PropertiesUtils propertiesUtils;

	@RequestMapping(value = "/userinfo")
	public String Load(ModelMap model) {

		try {
			List<InformationCode> inFloor;
			List<InformationCode> inMajor;
			inFloor = informationService.loadByType("floor");
			inMajor = informationService.loadByType("major");
			model.addAttribute("inFloor", inFloor);
			model.addAttribute("inMajor", inMajor);
			return "user/user_information";
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return "user/user_information";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateInformation(HttpSession session, ModelMap model,
									HttpServletResponse response, UserInformation usin) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		try {
			String userAccount = (String) session.getAttribute("UserAccount");
			/*// 转型为MultipartHttpRequest(重点的所在)
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得第1张图片（根据前台的name名称得到上传的文件）
			MultipartFile imgFile = multipartRequest.getFile("Picture");
			// 保存第一张图片
			String file = null;
			if (!(imgFile.getOriginalFilename() == null || "".equals(imgFile.getOriginalFilename()))) {
				file = this.addPicture(imgFile,userAccount);
			}

			if(file != null) {
				usin.setUserPicture(file);
			}*/
			usin.setUserAccount(userAccount);
			if(usin.getUserInfo() != null) {
				String text = usin.getUserInfo();
				text = text.replace("\r\n", "<br/>");
				usin.setUserInfo(text);
			}
			UserInformation usin2 = informationService.loadInformation(userAccount);
			String result = informationService.updateInformation(usin, usin2.getUserName());
			if (!result.equals("success")) {
				out.print("<script>alert('" + result + "')</script>");
				out.flush();
				return "user/user_information";
			}
			model.addAttribute("userInformation", usin);
			String level = (String) session.getAttribute("userLevel");
			return "user/" + level;
		} catch (Exception e) {
			// Auto-generated catch block
			e.printStackTrace();
			return "user/user_information";
		}
	}

	/*--------------------------------------------------------------------------*/

	private String addPicture(MultipartFile imgFile, String userAccount) {
		String fileName = imgFile.getOriginalFilename();
		if (StringUtils.isEmpty(fileName)) {
			return "error";
		}
		// 获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		// 对扩展名进行小写转换
		ext = ext.toLowerCase();
		// 定义一个数组，用于保存可上传的文件类型
		List<String> fileTypes = new ArrayList<>();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("gif");
		fileTypes.add("png");

		String path = "";
		File file;
		if (fileTypes.contains(ext)) { // 如果扩展名属于允许上传的类型，则创建文件
			String filePath = propertiesUtils.getFilePath() + File.separator + "senlin/information";
			File secondFolder = new File(filePath, userAccount);
            if(secondFolder.exists()) {                        //如果二级文件夹存在，则创建文件  
            	file = new File(secondFolder,fileName);
            }else {                                            //如果二级文件夹不存在，则创建二级文件夹  
                secondFolder.mkdirs();
                file = new File(secondFolder,fileName);    //创建完二级文件夹后，再合建文件  
            }
			try {
				imgFile.transferTo(file); // 保存上传的文件
				path = filePath + File.separator + userAccount + File.separator + fileName;
			} catch (Exception e) {
				log.error("存储图片失败：", e);
			}
		}
		return path;
	}

}
