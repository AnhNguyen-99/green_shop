package vn.fs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import vn.fs.dto.ChangePassword;
import vn.fs.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.SendMailService;

@Controller
public class AccountController {

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	HttpSession session;

	@Autowired
	UserRepository userRepository;


	@GetMapping(value = "/forgotPassword")
	public String forgotPassword() {

		return "web/forgotPassword";
	}

	@PostMapping("/forgotPassword")
	public ModelAndView forgotPassoword(ModelMap model, @RequestParam("email") String email) {
		List<User> listUser = userRepository.findAll();
		for (User user : listUser) {
			if(user.getEmail().equalsIgnoreCase(email)){
				model.addAttribute("newPassword", "");
				model.addAttribute("confirmPassword", "");
				model.addAttribute("changePassword", new ChangePassword());
				return new ModelAndView("web/changePassword", model);
			}
		}
		model.addAttribute("error", "Email này chưa đăng ký!");
		return new ModelAndView("web/changePassword", model);
	}

	@PostMapping("/changePassword")
	public ModelAndView changeForm(ModelMap model,
			@Valid @ModelAttribute("changePassword") ChangePassword changePassword, BindingResult result,
			@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword) {
		if (result.hasErrors()) {
			model.addAttribute("newPassword", newPassword);
			model.addAttribute("newPassword", confirmPassword);
			model.addAttribute("email", email);
			return new ModelAndView("/web/changePassword", model);
		}

		if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {

			model.addAttribute("newPassword", newPassword);
			model.addAttribute("newPassword", confirmPassword);
			model.addAttribute("error", "error");
			model.addAttribute("email", email);
			return new ModelAndView("/web/changePassword", model);
		}
		User user = userRepository.findByEmail(email);
		user.setStatus(true);
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userRepository.save(user);
		model.addAttribute("message", "Đặt lại mật khẩu thành công!");
		model.addAttribute("email", "");
		return new ModelAndView("/web/home", model);
	}

}
