package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
public class PurchaseController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addPurchaseView(HttpSession session, @RequestParam("prod_no") int prodNo) throws Exception{
		
		System.out.println("/addPurchaseView.do");
		
		ModelAndView modelAndView = new ModelAndView();
		
		String userId = ((User)session.getAttribute("user")).getUserId();
		User user = userService.getUser(userId);
		
		Product product = productService.getProduct(prodNo);
		
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		modelAndView.addObject("product", product);
		modelAndView.addObject("user", user);
		
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase(Product product, User user, Purchase purchase ) throws Exception{
		
		System.out.println("/addPurchase.do");
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchaseService.addPurchase(purchase);
		
		modelAndView.setViewName("/purchase/addPurchase.jsp");
		
		return modelAndView;
		
	}
	
	@RequestMapping("/listPurchase.do")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpSession session) throws Exception{
		
		System.out.println("/listPurchase.do");
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		
		
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase.do")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("/getPurchase.do");
		
		ModelAndView modelAndView = new ModelAndView();
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("/updatePurchaseView.do");
		
		ModelAndView modelAndView = new ModelAndView();
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		
		
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updatePurchase(Product product, User user, Purchase purchase, @RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("/updatePurchase.do");
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		purchaseService.updatePurcahse(purchase);
		
		purchase = purchaseService.getPurchase(tranNo);
		
		modelAndView.setViewName("/purchase/updatePurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		return modelAndView;
	}
	
	
	
//	@RequestMapping("/listSale.do")
//	public ModelAndView listSale() throws Exception{
//		
//		System.out.println("/listSale.do");
//		
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("/purchase/getPurchase.jsp");
//		
//		return modelAndView;
//	}
	
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode(@ModelAttribute("purchase") Purchase purchase) throws Exception{
		
		System.out.println("/updateTranCode.do");
		
		ModelAndView modelAndView = new ModelAndView();
		
		purchaseService.updateTranCode(purchase);
		
		modelAndView.setViewName("redirect:/listPurchase.do");
		
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCodeByProd.do")
	public ModelAndView updateTranCodeByProd(Product product, Purchase purchase, Search search, HttpSession session) throws Exception{
		
		System.out.println("/updateTranCodeByProd.do");
		
		purchase.setPurchaseProd(product);
		
		purchaseService.updateTranCode(purchase);
		
		if(search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page(search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/updateTranCodeByProd.jsp");
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);		
		
		return modelAndView;
	}
	
}
