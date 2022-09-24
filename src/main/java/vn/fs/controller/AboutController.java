package vn.fs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import vn.fs.commom.CommomDataService;
import vn.fs.entities.User;

import java.util.List;

@Controller
public class AboutController extends CommomController {

	@Autowired
	CommomDataService commomDataService;

	@GetMapping(value = "/aboutUs")
	public String about(Model model, User user) {

		commomDataService.commonData(model, user);
		return "web/about";
	}
	public boolean checkEmail(String email) {
		List<User> list = userRepository.findAll();
		for (User c : list) {
			if (c.getEmail().equalsIgnoreCase(email)) {
				return false;
			}
		}
		return true;
	}
}
