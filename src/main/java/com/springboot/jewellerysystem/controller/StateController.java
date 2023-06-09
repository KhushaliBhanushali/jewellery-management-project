package com.springboot.jewellerysystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.jewellerysystem.entity.Country;
import com.springboot.jewellerysystem.entity.State;
import com.springboot.jewellerysystem.service.CountryService;
import com.springboot.jewellerysystem.service.StateService;
import com.springboot.jewellerysystem.util.Helper;

@Controller
@RequestMapping(value = "admin/state")
public class StateController {
	private StateService stateService;
	private CountryService countryService;

	public StateController(StateService stateService, CountryService countryService) {
		this.stateService = stateService;
		this.countryService = countryService;
	}

	@GetMapping(value = "/index")
	public String states(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
		if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
		List<State> states = stateService.getAllState();
		model.addAttribute("listStates", states);
		model.addAttribute("keyword", keyword);
		return "admin/list/states_list";
	}

	@GetMapping(value = "/create")
	public String formStates(Model model) {
		if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
		model.addAttribute("state", new State());
		List<Country> countries = countryService.getAllCountry();
		model.addAttribute("listCountries", countries);

		return "admin/entry/state_entry";
	}

	@GetMapping(value = "/delete/{id}")
	public String deleteState(@PathVariable(value = "id") Integer id, String keyword, HttpSession  session) {
		if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
		stateService.removeState(id);
		session.setAttribute("msg", "deleted");
		return "redirect:/admin/state/index?keyword=" + keyword;
	}

	@GetMapping(value = "/update/{id}")
	public String updateState(@PathVariable(value = "id") Integer id, Model model) {
		if(Helper.checkUserRole()) { return "redirect:/";}
    	if(!Helper.checkAdminRole()) {return "redirect:/admin/logout";}
		State state = stateService.loadStateById(id);
		model.addAttribute("state", state);
		List<Country> countries = countryService.getAllCountry();
		model.addAttribute("listCountries", countries);

		return "admin/edit/state_edit";
	}

	@PostMapping(value = "/save")
	public String save(State state, HttpSession session) {
		String msg = "inserted";
		if(state.getId() != null && state.getId() != 0) {
			msg = "updated";
		}
		State s =stateService.createOrUpdateState(state);
		if(s != null) {
			session.setAttribute("msg", "inserted");
		}else {
			session.setAttribute("error","error");
		}
		return "redirect:/admin/state/index";
	}

}
