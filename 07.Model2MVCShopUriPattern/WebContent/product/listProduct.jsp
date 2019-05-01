<%@ page contentType="text/html; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>상품 목록조회</title>
	
<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">

function fncGetProductList(currentPage){
	document.getElementById("currentPage").value = currentPage;
	document.detailForm.submit();
}
function fncAddCart(prodNo){
	if(confirm("장바구니에 담으시겠습니까?") == true){
		document.detailForm.action='/product/addCart?menu=cartList';
		document.getElementById("prodNo").value = prodNo;
		document.detailForm.submit();
	}else{
		return;
	}
	
}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width:98%; margin-left:10px;">
			
<form name="detailForm" action="/product/listProduct?menu=${param.menu eq 'manage' ? "manage":"search"}" method="post">
<input type="hidden" id="prodNo" name="prodNo" value="">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37">
			<img src="/images/ct_ttl_img01.gif" width="15" height="37"/>
		</td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left:10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">
					<c:choose>
						<c:when test="${param.menu eq 'manage'}">
							상품 관리
						</c:when>
						<c:when test="${param.menu eq 'search'}">
							상품목록 조회
						</c:when>
						<c:when test="${param.menu eq 'cartList'}">
							장바구니
						</c:when>	
					</c:choose>
					</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37">
			<img src="/images/ct_ttl_img03.gif" width="12" height="37"/>
		</td>
	</tr>
</table>


<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td>
			<select name="pageCondition" class="ct_input_g" style="width:80px">
				<option value="" ${ ! empty search.pageSize && search.pageSize.equals("") ? "selected" : "" }>게시물개수</option>
				<option value="4" ${ ! empty search.pageSize && search.pageSize==4 ? "selected" : "" }>4</option>
				<option value="5" ${ ! empty search.pageSize && search.pageSize==5 ? "selected" : "" }>5</option>
				<option value="6" ${ ! empty search.pageSize && search.pageSize==6 ? "selected" : "" }>6</option>
				<option value="7" ${ ! empty search.pageSize && search.pageSize==7 ? "selected" : "" }>7</option>
				<option value="8" ${ ! empty search.pageSize && search.pageSize==8 ? "selected" : "" }>8</option>
				<option value="9" ${ ! empty search.pageSize && search.pageSize==9 ? "selected" : "" }>9</option>
				<option value="10" ${ ! empty search.pageSize && search.pageSize==10 ? "selected" : "" }>10</option>
			</select>
		</td>
		<td align="right">
			<select name="orderCondition" class="ct_input_g" style="width:80px">
				<option value="" ${ ! empty search.orderCondition && search.orderCondition.equals("") ? "selected" : "" }>선택없음</option>
				<option value="1" ${ ! empty search.orderCondition && search.orderCondition==1 ? "selected" : "" }>최근상품</option>
				<option value="2" ${ ! empty search.orderCondition && search.orderCondition==2 ? "selected" : "" }>가격낮은순</option>
				<option value="3" ${ ! empty search.orderCondition && search.orderCondition==3 ? "selected" : "" }>가격높은순</option>
				<option value="4" ${ ! empty search.orderCondition && search.orderCondition==4 ? "selected" : "" }>조회수순</option>
			</select>
			<select name="searchCondition" class="ct_input_g" style="width:80px">
				<option value="0" ${ ! empty search.searchCondition && search.searchCondition==0 ? "selected" : "" }>상품번호</option>
				<option value="1" ${ ! empty search.searchCondition && search.searchCondition==1 ? "selected" : "" }>상품명</option>
				<option value="2" ${ ! empty search.searchCondition && search.searchCondition==2 ? "selected" : "" }>상품가격</option>
				<c:if test="${user.userId eq 'admin' && param.menu eq 'manage'}">
					<option value="3" ${ ! empty search.searchCondition && search.searchCondition==3 ? "selected" : "" }>배송중</option>
					<option value="4" ${ ! empty search.searchCondition && search.searchCondition==4 ? "selected" : "" }>배송완료</option>
					<option value="5" ${ ! empty search.searchCondition && search.searchCondition==5 ? "selected" : "" }>구매완료</option>
				</c:if>
			</select>
			<input type="text" name="searchKeyword" value="${! empty search.searchCondition ? search.searchKeyword : "" }" class="ct_input_g" style="width:200px; height:19px" />
		</td>	
		<td align="right" width="70">
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="17" height="23">
						<img src="/images/ct_btnbg01.gif" width="17" height="23">
					</td>
					<td background="/images/ct_btnbg02.gif" class="ct_btn01" style="padding-top:3px;">
						<a href="javascript:fncGetProductList('1');">검색</a>
					</td>
					<td width="14" height="23">
						<img src="/images/ct_btnbg03.gif" width="14" height="23">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="right">
			가격대로 찾기
			<input type="text" name="searchMin" value="${! empty search.searchMin ? search.searchMin : "" }" class="ct_input_g" style="width:70px; height:19px" />
			-
			<input type="text" name="searchMax" value="${! empty search.searchMax ? search.searchMax : "" }" class="ct_input_g" style="width:70px; height:19px" />
		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td colspan="11" >전체 ${resultPage.totalCount } 건수, 현재  ${resultPage.currentPage } 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">가격</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">상품번호</td>	
		<td class="ct_line02"></td>
		<td class="ct_list_b">현재상태</td>	
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<c:set var="i" value="0"/>
	<c:forEach var="product" items="${list }">
		<c:set var="i" value="${i+1 }"/>
		<tr class="ct_list_pop">
			<td align="center">${ i }</td>
			<td></td>			
			<td align="left">
			  <c:if test="${product.proTranCode == '4' || product.proTranCode == null}">
				<c:choose>
					<c:when test="${param.menu eq 'manage'}">
						<a href="/product/updateProductView?prodNo=${product.prodNo}&menu=manage">	
					</c:when>
					<c:otherwise>
						<a href="/product/getProduct?prodNo=${product.prodNo }&menu=search">			
					</c:otherwise>
				</c:choose>
			  </c:if>
				${product.prodName}</a>
			</td>
			<td></td>
			<td align="left">${product.price}</td>
			<td></td>
			<td align="left">${product.prodNo}</td>
			<td></td>
			<td align="left">
			<c:choose>
				<c:when test="${user.userId == 'admin' }">
					<c:choose>
						<c:when test="${product.proTranCode eq '4' || product.proTranCode == null}">
							판매중
						</c:when>
						<c:when test="${product.proTranCode eq '3' || product.proTranCode eq '2'}">
							구매완료	
							<c:choose>
								<c:when test="${product.proTranCode eq '3' && user.userId eq 'admin' && param.menu eq 'manage'}">
										&nbsp;<a href="/purchase/updateTranCode?prodNo=${product.prodNo}&tranCode=${product.proTranCode}">배송하기</a>
								</c:when>
								<c:when test="${product.proTranCode eq '2' && user.userId eq 'admin'}">
										&nbsp;배송중
								</c:when>
							</c:choose>
						</c:when>
						<c:when test="${product.proTranCode eq '1' }">
							배송완료
						</c:when>
					</c:choose>	
				</c:when>
				<c:when test="${user.userId != 'admin' }">
					<c:choose>
						<c:when test="${product.proTranCode eq '4' || product.proTranCode == null}">
							판매중
							&nbsp;<a href="javascript:fncAddCart(${product.prodNo});">장바구니 담기</a>
						</c:when>
						<c:otherwise>
							재고없음
						</c:otherwise>
					</c:choose>
				</c:when>
			
			</c:choose>
				
			</td>	
		</tr>
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>
	</c:forEach>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			<jsp:include page="../common/pageNavigator.jsp">
				<jsp:param name="what" value="Product"/>
			</jsp:include>	
			
    	</td>
	</tr>
</table>
<!--  페이지 Navigator 끝 -->
</form>
</div>

</body>
</html>

