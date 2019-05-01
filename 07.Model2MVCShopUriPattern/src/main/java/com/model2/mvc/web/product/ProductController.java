package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/product/*")
public class ProductController {

		@Autowired
		@Qualifier("productServiceImpl")
		private ProductService productService;

		public ProductController() {
			System.out.println(this.getClass());
		}
		
		@Value("#{commonProperties['pageUnit']}")
		//@Value("#{commonProperties['pageUnit'] ?: 3}")
		int pageUnit;
		
		@Value("#{commonProperties['pageSize']}")
		//@Value("#{commonProperties['pageSize'] ?: 2}")
		int pageSize;
		
		@RequestMapping("addProductView")
		public String addProductView() throws Exception {

			System.out.println("addProductView");
			
			return "redirect:/product/addProductView.jsp";
		}
		
		@RequestMapping("addProduct")
		public String addProduct( @ModelAttribute("product") Product product ) throws Exception {

			System.out.println("addProduct");
			
			product.setManuDate(product.getManuDate().replaceAll("-", ""));
			
			productService.addProduct(product);
			
			return "forward:/product/addProduct.jsp";
		}
		
		@RequestMapping("getProduct")
		public String getProduct( @RequestParam("prodNo") int prodNo , Model model,
														HttpServletResponse response) throws Exception {
			
			System.out.println("getProduct");
			productService.increaseViewCount(prodNo);
			Product product = productService.getProduct(prodNo);
			
			Cookie cookie = new Cookie(String.valueOf(prodNo), String.valueOf(prodNo));
			response.addCookie(cookie);
			
			model.addAttribute("product", product);
			
			return "forward:/product/readProduct.jsp";
		}
		
		@RequestMapping("/updateProductView")
		public String updateProductView( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

			System.out.println("updateProductView");
			
			Product product = productService.getProduct(prodNo);
			
			model.addAttribute("product", product);
			
			return "forward:/product/updateProduct.jsp";
		}
		
		@RequestMapping("updateProduct")
		public String updateProduct( @ModelAttribute("product") Product product , Model model ) throws Exception{

			System.out.println("updateProduct");

			productService.updateProduct(product);
			
			return "redirect:/product/getProduct?prodNo="+product.getProdNo();
		}
		
		@RequestMapping("listProduct")
		public String listProduct( @ModelAttribute("search") Search search ,
													Model model, HttpServletRequest request) throws Exception{
			
			System.out.println("listProduct");
			
			if(request.getParameter("menu").equals("cartList")) {
				search.setCart(true);
			}else {
				search.setCart(false);
			}
			
			if(search.getCurrentPage() ==0){
				search.setCurrentPage(1);
			}//언제 currenctPage가 0일까?
			
			if(request.getParameter("currentPage") != null && !request.getParameter("currentPage").equals("")) {
				System.out.println("들어온 currentPage 값 :: "+request.getParameter("currentPage"));
				search.setCurrentPage(Integer.parseInt(request.getParameter("currentPage")));
			}
			
			if(request.getParameter("pageCondition") != null && !request.getParameter("pageCondition").equals("")) {
				pageSize = Integer.parseInt(request.getParameter("pageCondition"));
			}else {
				pageSize = 3;
			}
			
			search.setPageSize(pageSize);
			
			// Business logic 수행
			Map<String , Object> map=productService.getProductList(search);
			
			Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
			
			System.out.println(resultPage);
			
			// Model 과 View 연결
			model.addAttribute("list", map.get("list"));
			model.addAttribute("resultPage", resultPage);
			model.addAttribute("search", search);
			
			return "forward:/product/listProduct.jsp";
		}
		
		@RequestMapping("addCart")
		public String addCart(@RequestParam("prodNo") int prodNo) throws Exception{
			
			System.out.println("addCart");
			
			productService.addCart(prodNo);
			
			return "forward:/product/afterCart.jsp";
		}
		
}//end of class
