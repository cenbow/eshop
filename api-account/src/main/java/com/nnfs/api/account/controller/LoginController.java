package com.nnfs.api.account.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnfs.api.account.constant.PromptMsg;
import com.nnfs.api.account.dto.Result;
import com.nnfs.api.account.util.Utility;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public Result Login(@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password, @RequestParam(value = "rememberMe") Boolean rememberMe) {
		Result result = new Result();
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password, rememberMe);
		try {
			SecurityUtils.getSubject().login(usernamePasswordToken);
			String token = Utility.tokenGenerator();
			result.setCode(PromptMsg.SUCCESS.getCode());
			result.setMsg(PromptMsg.SUCCESS.getMsg());
			result.setData(token);
		} catch (UnknownAccountException e) {
			result.setCode(PromptMsg.ACCOUNT_NOT_EXIST.getCode());
			result.setMsg(PromptMsg.ACCOUNT_NOT_EXIST.getMsg());
		} catch (IncorrectCredentialsException e) {
			result.setCode(PromptMsg.ACCOUNT_INCORRECTCREDENTIALS.getCode());
			result.setMsg(PromptMsg.ACCOUNT_INCORRECTCREDENTIALS.getMsg());
		} catch (LockedAccountException e) {
			result.setCode(PromptMsg.ACCOUNT_LOCKED.getCode());
			result.setMsg(PromptMsg.ACCOUNT_LOCKED.getMsg());
		} catch (AuthenticationException e) {
			result.setCode(PromptMsg.LOGIN_ERROR.getCode());
			result.setMsg(PromptMsg.LOGIN_ERROR.getMsg());
		}
		logger.info(result.getMsg());
		return result;
	}
}
