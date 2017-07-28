package action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import chok.devwork.BaseController;
import chok.util.EncryptionUtil;
import entity.User;
import service.UserService;

@Scope("prototype")
@Controller
@RequestMapping("/")
public class PasswordAction extends BaseController<User> 
{
	
	@Autowired
	private UserService service;

	/**
	 * 已登录用户修改密码的入口
	 */
	@RequestMapping("/password")
	public String password() throws IOException
	{
		response.setHeader("P3P", "CP=CAO PSA OUR");
		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("P3P", "CP='CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR'");
		//response.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
		String serviceURL = req.getString("service");
		if(log.isDebugEnabled())
		{
			log.debug(serviceURL);
		}
		request.setAttribute("account", req.getString("account"));
		request.setAttribute("service", java.net.URLEncoder.encode(serviceURL, "UTF-8"));
		return "/password.jsp";
	}
	@RequestMapping("/password2")
	public void password2() 
	{
		try 
		{
			User po = service.getByTcCode(req.getString("account"));
			if (null==po)
			{
				result.setSuccess(false);
				result.setMsg(req.getString("account")+" 账号不存在");
			}
			else
			{
				if(!EncryptionUtil.getMD5(req.getString("old_password")).toLowerCase().equals(po.getString("tc_password")))
				{
					result.setSuccess(false);
					result.setMsg("原密码不正确");
				}
				else
				{
					po.set("tc_password", EncryptionUtil.getMD5(req.getString("new_password")).toLowerCase());
					service.updPwd(po);
					Map<Object, Object> data = new HashMap<Object, Object>();
					data.put("RefererUrl", req.getString("service"));
					result.setData(data);
					result.setSuccess(true);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		printJson(result);
	}
	
}