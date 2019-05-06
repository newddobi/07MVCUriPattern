package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("addPurchaseView")
	public String addPurchaseView(@RequestParam("prodNo") int prodNo,
									Map<String, Product> map) throws Exception{
		
		System.out.println("addPurchaseView");
		
		Product product = productService.getProduct(prodNo);
		
		map.put("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	@RequestMapping("addPurchase")
	public String addPurchase(@ModelAttribute("purchase") Purchase purchase,
								@RequestParam("prodNo") int prodNo,
								@RequestParam("buyerId") String buyerId) throws Exception{
		
		System.out.println("addPurchase");
		
		User user = new User();
		Product product = new Product();
		
		product = productService.getProduct(prodNo);
		product.setAmount(product.getAmount() - purchase.getTranAmount());
		productService.minusAmount(product);
		
		user.setUserId(buyerId);
		//product.setProdNo(prodNo);
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		purchaseService.addPurchase(purchase);
		
		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("getPurchase")
	public String getPurchase(HttpServletRequest request, Model model) throws Exception{
		
		System.out.println("getPurchase");
		
		Purchase purchase;
		
		if(request.getParameter("prodNo") != null) {
			int prodNo = Integer.parseInt(request.getParameter("prodNo"));
			purchase = purchaseService.getPurchase2(prodNo);
		}else {
			int tranNo = Integer.parseInt(request.getParameter("tranNo"));
			purchase = purchaseService.getPurchase(tranNo);
		}
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	@RequestMapping("updatePurchaseView")
	public String updatePurchaseView(@RequestParam("tranNo") int tranNo, 
																	Model model) throws Exception{
		
		System.out.println("updatePurchaseView");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	@RequestMapping("updatePurchase")
	public String updatePurchase(@ModelAttribute("purchase") Purchase purchase,
															@RequestParam("tranNo") int tranNo) throws Exception {
		
		System.out.println("updatePurchase");
		purchase.setTranNo(tranNo);
		purchaseService.updatePurchase(purchase);
		
		return "redirect:/purchase/getPurchase?tranNo="+purchase.getTranNo();
	}
	
	@RequestMapping("listPurchase")
	public String listPurchase(@ModelAttribute("search") Search search,
													Model model, HttpServletRequest request,
													HttpSession session) throws Exception{
		System.out.println("listPurchase");
		
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
		
		session = request.getSession();
		User user = (User)session.getAttribute("user");
		
		Map<String, Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("deletePurchase")
	public String deletePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		
		System.out.println("deletePurchase");	
		
		purchaseService.deletePurchase(tranNo);
		
		return "forward:/purchase/afterDelete.jsp";
	}
	
	@RequestMapping("updateTranCode")
	public String updateTranCoed(@RequestParam("prodNo") int prodNo,
															@RequestParam("tranCode") String tranCode) throws Exception{
		
		System.out.println("updateTranCode");
		
		Purchase purchase = new Purchase();
		Product product = new Product();
		
		product.setProdNo(prodNo);
		purchase.setPurchaseProd(product);
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/purchase/getPurchase?prodNo="+prodNo;
	}
}//end of class
