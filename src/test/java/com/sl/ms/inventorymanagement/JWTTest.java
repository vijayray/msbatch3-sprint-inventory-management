package com.sl.ms.inventorymanagement;

import com.sl.ms.inventorymanagement.config.AuthenticationRequest;
import com.sl.ms.inventorymanagement.config.JwtUtil;
import com.sl.ms.inventorymanagement.inventory.InventoryRepository;
import com.sl.ms.inventorymanagement.product.ProductRepository;
import com.sl.ms.inventorymanagement.service.MyUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HelloWorldController.class)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class JWTTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	ProductRepository productRepository;
	
	@MockBean
	InventoryRepository inventoryRepository;
	
	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtUtil jwtUtil;

	@MockBean
	private MyUserDetailsService myUserDetailsService;
	
	 @Test
	 public void createTokenTest() throws Exception {
		 
		 AuthenticationRequest req = new AuthenticationRequest();
		 req.setUsername("foo");
		 req.setPassword("faa");
		 Authentication authentication = mock(Authentication.class);
	        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())))
	        .thenReturn(authentication);
	        this.mockMvc.perform(post("/authenticate"))
	                .andExpect(status().isBadRequest());
	                
	    }

}
