package com.devtam.commonbase.service;

import com.devtam.commonbase.entity.Category;
import com.devtam.commonbase.entity.Product;
import com.devtam.commonbase.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryService categoryService;

    @Override
    public Product saveProduct(Product product) {
        if (product.getProductDetailsList() != null && !product.getProductDetailsList().isEmpty())
            product.getProductDetailsList().stream().forEach(productDetails -> {
                productDetails.setProduct(product);
            });
        return productRepository.save(product);
    }

    @Override
    public List<Product> getListProducts(long categoryId, int limit) {
        List<Product> productList = new ArrayList<>();
        if (categoryId < 0) {
            Page<Product> productPage = productRepository.findAll(PageRequest.of(0, limit));
            if (productPage != null && productPage.getSize() > 0) {
                productList = productPage.getContent();
            }
        } else {
            productList = productRepository.getAllByCategoryId(categoryId, PageRequest.of(0, limit));
        }
        return productList;
    }

    @Override
    public Page<Product> getTopSelling(boolean status, int page, int limit) {
        Page<Product> productList = productRepository.findPageSortedBySoldRatioAndCreated(status, PageRequest.of(page, limit));
        if (productList == null || productList.isEmpty()) {
            return Page.empty();
        }
        return productList;
    }

    @Override
    public Page<Product> getTopSellingByCategory(long categoryId, boolean status, int page, int limit) {
        Category category = categoryService.getCategoryById(categoryId);
        Page<Product> producPage;
        if (category == null) {
            return Page.empty();
        }
        if (category.getParentId() == -1) {
            producPage = productRepository.findAllByCategoryParentIdSortedBySoldRatioAndCreated(categoryId, status, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        } else {
            producPage = productRepository.findAllByCategoryIdSortedBySoldRatioAndCreated(categoryId, status, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        }
        return producPage;
    }

    @Override
    public Page<Product> getNewestProductsListByCategory(long categoryId, boolean status, int page, int limit) {
        Category category = categoryService.getCategoryById(categoryId);
        Page<Product> producPage;
        if (category == null) {
            return Page.empty();
        }
        if (category.getParentId() == -1) {
            producPage = productRepository.findAllByProductStatusAndCategoryParentIdOrderByProductCreatedDesc(status, categoryId, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        } else {
            producPage = productRepository.findAllByProductStatusAndCategoryIdOrderByProductCreatedDesc(status, categoryId, PageRequest.of(page, limit));
            if (producPage == null || producPage.isEmpty()) {
                return Page.empty();
            }
        }
        return producPage;
    }

    @Override
    public Page<Product> getNewestProductsList(boolean status, int page, int limit) {
        Page<Product> productList = productRepository.findAllByProductStatusOrderByProductCreatedDesc(status, PageRequest.of(page, limit));
        if (productList == null || productList.isEmpty()) {
            return Page.empty();
        }
        return productList;
    }

    @Override
    public List<Product> getListProducts(long categoryId, int limit, boolean status) {
        return null;
    }

    @Override
    public Page<Product> getPageProducts(long categoryId, int limit, boolean status) {
        Page<Product> productPage = Page.empty();
        if (categoryId < 0) {
            productPage = productRepository.findAllByProductStatus(PageRequest.of(0, limit), status);
        } else {
            productPage = productRepository.findAllByCategoryIdAndAndProductStatus(categoryId, PageRequest.of(0, limit), status);
        }
        return productPage;
    }

    @Override
    public Page<Product> getPageProducts(long categoryId, int page, int limit, boolean status) {
        Page<Product> productPage = Page.empty();
        if (categoryId < 0) {
            productPage = productRepository.findAllByProductStatusOrderByProductIdDesc(PageRequest.of(page, limit), status);
        } else {
            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return null;
            }
            if (category.getParentId() == -1) {
                productPage = productRepository
                        .findAllByCategoryParentIdAndProductStatusOrderByProductCreatedDesc
                                (categoryId, status, PageRequest.of(page, limit));
            } else {
                productPage = productRepository.findAllByCategoryIdAndProductStatusOrderByProductCreatedDesc(categoryId, PageRequest.of(page, limit), status);
            }
        }
        return productPage;
    }

    @Override
    public Product getProduct(long productId) {
        Product product = productRepository.getByProductId(productId);
        return product;
    }

    @Override
    public Map<Long, Product> getMapProducts(List<Long> productIds) {
        Map<Long, Product> productMap = new HashMap<>();
        for (long id : productIds) {
            Product product = getProduct(id);
            if (product != null) {
                productMap.put(id, product);
            }
        }
        if (productMap.isEmpty()) {
            return Collections.emptyMap();
        }
        return productMap;
    }
}
