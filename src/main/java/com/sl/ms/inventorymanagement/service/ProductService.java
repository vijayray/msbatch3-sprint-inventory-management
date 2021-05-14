package com.sl.ms.inventorymanagement.service;

import com.sl.ms.inventorymanagement.product.Product;
import com.sl.ms.inventorymanagement.product.ProductController;
import com.sl.ms.inventorymanagement.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductRepository prodrepo;

	public List<Product> save(List<Product> product) {
		return prodrepo.saveAll(product);		
	}
	
	public Product save(Product product) {
		return prodrepo.save(product);		
	}


	public void delete(Long id) {
		
		 prodrepo.deleteById(id);
	}

	public Optional<Product> getById(Long id) {
		return prodrepo.findById(id);
	}

	
	@Cacheable("Products")
	public Object[] findSupportedProducts() {
		log.info("calling findSupportedProducts details");
		try  {
            Thread.sleep(1000*5);
        } 
        catch (InterruptedException e)  {
            e.printStackTrace();
        }
		return prodrepo.findSupportedProducts();
	}

}
