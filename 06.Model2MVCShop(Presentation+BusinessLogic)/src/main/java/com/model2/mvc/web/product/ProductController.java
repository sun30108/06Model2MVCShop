package com.model2.mvc.web.product;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	@Qualifier("productServiceImpl")
	ProductService productService;
	
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;

	public ProductController() {
		System.out.println(this.getClass());
	}

	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception{
		
		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("/addProduct.do");
		
		productService.addProduct(product);
		
		return "/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, @RequestParam(value="menu", required=false) String menu, Model model) throws Exception{
		
		System.out.println("/getProduct.do");
		
		Product product = productService.getProduct(prodNo);
		
		model.addAttribute("product", product);
		
		
		if(menu != null && menu.equals("manage")) {
			return "/product/updateProductView.jsp";
		}else {
			return "/product/getProduct.jsp";			
		}
		
	}
	
	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search,@RequestParam("menu") String menu, Model model) throws Exception{
		
		System.out.println("/listProduct.do");
		
		if(search.getCurrentPage() == 0 ) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		
		Map<String, Object> map = productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		
		
		return "/product/listProduct.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product) throws Exception{
		
		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo();
	}
	
//	@RequestMapping("/updateProductView.do")
//	public String updateProduct()throws Exception{
//		
//		System.out.println("/updateProductView.do");
//		
//		return "redirect:/listProduct.do?menu=manage";
//	}
	
}
