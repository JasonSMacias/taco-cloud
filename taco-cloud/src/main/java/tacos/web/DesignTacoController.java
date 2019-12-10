package tacos.web;
import java.security.Principal;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Taco;
import tacos.User;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;
import tacos.data.IngredientRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	
	private TacoRepository designRepo;
	
	private UserRepository userRepo;
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}
	
	@Autowired
	public DesignTacoController(
			IngredientRepository ingredientRepo, TacoRepository designRepo, 
			UserRepository userRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
		this.userRepo = userRepo;
	}
	
	@GetMapping
	public String showDesignForm(Model model, Principal principal) {
		log.info("   ---  Designing Taco");
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(),
					filterByType(ingredients, type));
		}
		
		String username = principal.getName();
		User user = userRepo.findByUsername(username);
		model.addAttribute("user", user);
		
//		 model.addAttribute("design", new Taco());
		return "design";
	}
	
	@PostMapping
	public String processDesign(@Valid Taco taco, Errors errors, @ModelAttribute Order order) {
		log.info("    ---   Saving taco");
//		System.out.println("hit");
		if(errors.hasErrors()) {
			System.out.println(taco);
			System.out.println(errors);
			return "design";
		}
		Taco saved = designRepo.save(taco);
		order.addDesign(saved);
		return "redirect:/orders/current";
	}
	
	private List<Ingredient> filterByType(
			List<Ingredient> ingredients, Type type)  {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
