package com.shop.project.service;

import com.shop.project.exceptions.ResourceNotFoundException;
import com.shop.project.model.Category;
import com.shop.project.model.Product;
import com.shop.project.pyaload.ProductDTO;
import com.shop.project.pyaload.ProductResponse;
import com.shop.project.repository.CategoryRepository;
import com.shop.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId ));
        Product product =modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -  (product.getDiscount() * 0.01* product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        //一樣 把檔案轉成DTO呈現
        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        //get all the products first
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId ));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        //拿到後依樣轉回dto
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        //get all the products first

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        //拿到後依樣轉回dto
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }
    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        // Get the existing product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        // Update the product info with the one in request body
        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        // Save to database
        Product savedProduct = productRepository.save(productFromDb);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Upload img
        String path = "images/";
        String fileName = uploadImage(path, image);

        // Updating the new file name to the product
        productFromDb.setImage(fileName);

        // Save updated product
        Product updatedProduct = productRepository.save(productFromDb);

        // return DTO after mapping product to DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        // File names of current / original file
        String originalFileName = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        // mat.jpg --> 1234 --> 1234.jpg
        assert originalFileName != null;
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        // Check if path exist and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // returning file name
        return fileName;
    }

}
