/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.repositories;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.Repository;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;

import java.util.Collection;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ProductsRepository
    extends Repository<Product> {

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.productCategory", countQuery = "SELECT p FROM Product p")
    Page<Product> deepReadAll(Pageable pageable);

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.productCategory", countQuery = "SELECT p FROM Product p")
    Page<Product> deepReadAll(Predicate filters, Pageable pageable);

    Optional<Product> findByBarcode(String barcode);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productCategory.id = :categoryId WHERE p.id = :id")
    void setProductCategoryById(@Param("id") Long productId, @Param("categoryId") Long categoryId);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productCategory = null WHERE p.productCategory IN (:categories)")
    void orphanizeByCategories(@Param("categories") Collection<ProductCategory> categories);
}
