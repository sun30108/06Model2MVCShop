package com.model2.mvc.service.product;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;


public interface ProductService {
	
	//��ǰ���
	public void addProduct(Product product) throws Exception;

	//��ǰ����Ȯ��
	public Product getProduct(int prodNo) throws Exception;

	//��ǰ��������Ʈ
	public Map<String,Object> getProductList(Search search) throws Exception;
	
	//��ǰ��������
	public void updateProduct(Product product) throws Exception;
	
}