package com.springboot.jewellerysystem.controller;

import com.springboot.jewellerysystem.entity.User; 
import com.springboot.jewellerysystem.service.UserService;
import com.springboot.jewellerysystem.util.Helper;

import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession; 
@Controller 
@RequestMapping(value = "admin/user") 
public class UserController { 
 private UserService userService; 
    public UserController(UserService userService) { 
        this.userService = userService; 
    }
 
    @GetMapping(value = "/index") 
    public String users(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) { 
    	if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
    	List<User> users = userService.getAllUser(); 
        model.addAttribute("listUsers", users); 
        model.addAttribute("keyword", keyword); 
        return "admin/list/users_list"; 
    }
 
  @GetMapping(value = "/create") 
    public String formUsers(Model model) { 
	  if(Helper.checkUserRole()) { return "redirect:/";}
  	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";} 
	  model.addAttribute("user", new User()); 
        return "admin/entry/user_entry"; 
    } 
    @GetMapping(value = "/delete/{id}") 
    public String deleteUser(@PathVariable(value = "id") Integer id, String keyword, HttpSession session) { 
    	if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
    	userService.removeUser(id); 
        session.setAttribute("msg", "deleted");
        return "redirect:/admin/user/index?keyword=" + keyword; 
    }
 
    @GetMapping(value = "/update/{id}") 
    public String updateUser(@PathVariable(value = "id") Integer id, Model model) { 
    	if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
    	User user = userService.loadUserById(id); 
        model.addAttribute("user", user); 
        return "admin/edit/user_edit"; 
    }
 
    @PostMapping(value = "/save") 
    public String save(User user, HttpSession session) { 
    	String msg = "inserted";
		if(user.getId() != null && user.getId() != 0) {
			msg = "updated";
		}
    	if(user.getEntryDate() == null) {
    		user.setEntryDate(new Date());
    	}
    	User u =userService.createOrUpdateUser(user); 
         
        if(u != null) {
			session.setAttribute("msg", "inserted");
		}else {
			session.setAttribute("error","error");
		}
        return "redirect:/admin/user/index"; 
    }
 
} 
