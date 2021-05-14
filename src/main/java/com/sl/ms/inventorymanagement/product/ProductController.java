package com.sl.ms.inventorymanagement.product;

import com.sl.ms.inventorymanagement.exception.ProductNotFoundException;
import com.sl.ms.inventorymanagement.inventory.Inventory;
import com.sl.ms.inventorymanagement.inventory.InventoryRepository;
import com.sl.ms.inventorymanagement.service.ProductFileUploadService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductService productService;

    @Autowired
    ProductFileUploadService prodfileservice;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class.getName());

    @GetMapping("/products")
    public List<Product> getProducts() {
        log.info("Getting getProducts() details");
        List<Product> product = (List<Product>) productRepository.findAll();
        return product;
    }

    @RequestMapping("/products/{Id}")
    public Optional<Product> getProductsById(@PathVariable("Id") Long Id) {
        log.info("Getting getProductsById details");
        Optional<Product> product = productRepository.findById(Id);
        if (!product.isPresent())
            throw new ProductNotFoundException("Id" + Id);
        else {
            return product;
        }
    }

    @PostMapping(path = "/products", consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<Product>> saveProducts(@RequestBody List<Product> product) {
        log.info("Getting saveProducts details");
        productService.save(product);
        return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
    }

    @GetMapping("/supportedProducts")
    public Object[] getSupportedProducts() {
        log.info("Getting getSupportedProducts details");
        Object[] product = productService.findSupportedProducts();
        return product;
    }


    @GetMapping("/checkProducts/{Id}")
    public boolean getAvailableProducts(@PathVariable("Id") Long Id) {
        log.info("calling RestAPI of Orders");
        Optional<Product> product = productRepository.findById(Id);
        int quantity = product.get().getQuantity();
        log.info("Calling Inventory API ");
        if (quantity > 0)
            return true;
        else {
            return false;
        }
    }

    @PostMapping(path = "/products/create/{Id}", consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<Object> createProductsForInv(@PathVariable("Id") Long Id, @RequestBody Product product) {
        log.info("Getting createProductsForInv details");
        Optional<Inventory> inv = inventoryRepository.findById(Id);
        if (!inv.isPresent()) {
            throw new ProductNotFoundException("Id" + Id);
        } else {
            Inventory inventory = inv.get();
            ((Product) product).setInventory(inventory);
            productRepository.save(product);
        }
        return new ResponseEntity<Object>(product, HttpStatus.OK);
    }


    @PostMapping("product/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        log.info("Getting uploadFile details");
        String message = "";
        try {
            prodfileservice.store(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }


    @PutMapping(path = "/products/{Id}", consumes = {"application/json"})
    @ResponseBody
    public ResponseEntity<Object> updateProduct(@PathVariable("Id") Long Id, @RequestBody Product product) {
        log.info("Getting updateProduct details");
        Optional<Inventory> inv = inventoryRepository.findById(Id);
        Optional<Product> pro = productRepository.findById(Id);
        if (!pro.isPresent()) {
            throw new ProductNotFoundException("Id" + Id);
        } else {
            Inventory inventory = inv.get();
            ((Product) product).setInventory(inventory);
            productRepository.save(product);
        }
        return new ResponseEntity<Object>(product, HttpStatus.OK);
    }


    @DeleteMapping("/products/{id}")
    public Optional<Product> deleteProductsById(@PathVariable("id") Long id) {
        log.info("Getting deleteProductsById details");
        Optional<Product> pro = productRepository.findById(id);
        if (!pro.isPresent()) {
            throw new ProductNotFoundException("Id" + id);
        } else {
            Optional<Product> delete = productService.getById(id);
            productService.delete(id);
            return delete;
        }
    }

    @PutMapping("/testProductUpdate")
    public Product updatetest(@RequestBody Product product) {
        productService.save(product);
        return product;
    }

}
