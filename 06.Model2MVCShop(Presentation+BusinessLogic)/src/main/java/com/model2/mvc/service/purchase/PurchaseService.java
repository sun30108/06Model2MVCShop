package com.model2.mvc.service.purchase;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;


public interface PurchaseService {

	//구매등록
	public void addPurchase(Purchase purchase) throws Exception;
	
	//구매정보 조회  //구매이력에서 조회?
	public Purchase getPurchase(int tranNo) throws Exception;
	
	//구매정보 조회
	public Purchase getPurchase2(int ProdNo) throws Exception;
	
	//구매리스트 조회
	public Map<String,Object> getPurchaseList(Search search,String buyerId) throws Exception;
	
	//판매리스트 조회
	public Map<String,Object> getSaleList(Search search) throws Exception;
	
	//구매정보 수정
	public void updatePurcahse(Purchase purchase) throws Exception;
	
	//제품상태코드 수정
	public void updateTranCode(Purchase purchase) throws Exception;
	
}