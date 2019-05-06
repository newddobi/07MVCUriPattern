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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
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
		public ModelAndView addProductView() throws Exception {

			System.out.println("addProductView");
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("redirect:/product/addProductView.jsp");
			
			return modelAndView;
		}
		
		@RequestMapping("addProduct")
		public ModelAndView addProduct( @ModelAttribute("product") Product product ) throws Exception {

			System.out.println("addProduct");
			
			productService.addProduct(product);
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("forward:/product/addProduct.jsp");
			
			return modelAndView;
		}
		
		@RequestMapping("getProduct")
		public ModelAndView getProduct( @RequestParam("prodNo") int prodNo ,
														@CookieValue(value="history", required=false) Cookie cookie,
														HttpServletResponse response) throws Exception {
			
			System.out.println("getProduct");
			productService.increaseViewCount(prodNo);
			Product product = productService.getProduct(prodNo);
			
			if(cookie != null) {
				cookie.setValue(cookie.getValue()+","+Integer.toString(prodNo));
			}else {
				cookie = new Cookie("history", Integer.toString(prodNo));
			}

			cookie.setPath("/");
			cookie.setMaxAge(3600);
			response.addCookie(cookie);						
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("forward:/product/readProduct.jsp");
			modelAndView.addObject("product", product);
			return modelAndView;
		}
		
		@RequestMapping("/updateProductView")
		public ModelAndView updateProductView( @RequestParam("prodNo") int prodNo ) throws Exception{

			System.out.println("updateProductView");
			
			Product product = productService.getProduct(prodNo);
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("product", product);
			modelAndView.setViewName("forward:/product/updateProduct.jsp");
			
			return modelAndView;
		}
		
		@RequestMapping("updateProduct")
		public ModelAndView updateProduct( @ModelAttribute("product") Product product) throws Exception{

			System.out.println("updateProduct");

			productService.updateProduct(product);
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("redirect:/product/getProduct?prodNo="+product.getProdNo());
			
			return modelAndView;
		}
		
		@RequestMapping("listProduct")
		public ModelAndView listProduct( @ModelAttribute("search") Search search ,
											HttpServletRequest request) throws Exception{
			
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
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("forward:/product/listProduct.jsp");
			modelAndView.addObject("list", map.get("list"));
			modelAndView.addObject("resultPage", resultPage);
			modelAndView.addObject("search", search);
			
			return modelAndView;
		}
		
		@RequestMapping("addCart")
		public ModelAndView addCart(@RequestParam("prodNo") int prodNo) throws Exception{
			
			System.out.println("addCart");
			
			productService.addCart(prodNo);
			
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("forward:/product/afterCart.jsp");
			
			return modelAndView;
		}
		
}//end of class
