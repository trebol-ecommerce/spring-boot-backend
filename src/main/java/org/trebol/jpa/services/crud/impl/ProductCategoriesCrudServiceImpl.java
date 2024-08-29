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

package org.trebol.jpa.services.crud.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.ProductsCategoriesRepository;
import org.trebol.jpa.services.conversion.ProductCategoriesConverterService;
import org.trebol.jpa.services.crud.CrudGenericService;
import org.trebol.jpa.services.crud.ProductCategoriesCrudService;
import org.trebol.jpa.services.patch.ProductCategoriesPatchService;

import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class ProductCategoriesCrudServiceImpl
    extends CrudGenericService<ProductCategoryPojo, ProductCategory>
    implements ProductCategoriesCrudService {
    private final ProductsCategoriesRepository categoriesRepository;
    private final ProductCategoriesPatchService categoriesPatchService;

    @Autowired
    public ProductCategoriesCrudServiceImpl(
        ProductsCategoriesRepository categoriesRepository,
        ProductCategoriesConverterService categoriesConverterService,
        ProductCategoriesPatchService categoriesPatchService
    ) {
        super(categoriesRepository, categoriesConverterService, categoriesPatchService);
        this.categoriesRepository = categoriesRepository;
        this.categoriesPatchService = categoriesPatchService;
    }

    @Override
    public Optional<ProductCategory> getExisting(ProductCategoryPojo input) throws BadInputException {
        String code = input.getCode();
        if (StringUtils.isBlank(code)) {
            throw new BadInputException("Invalid category code");
        } else {
            return this.categoriesRepository.findByCode(code);
        }
    }

    @Override
    protected final ProductCategory flushPartialChanges(Map<String, Object> changes, ProductCategory existingEntity) throws BadInputException {
        ProductCategory preparedEntity = categoriesPatchService.patchExistingEntity(changes, existingEntity);
        if (existingEntity.getParent()!=null) {
            preparedEntity.setParent(existingEntity.getParent());
        }
        if (existingEntity.equals(preparedEntity)) {
            return existingEntity;
        }
        return categoriesRepository.saveAndFlush(preparedEntity);
    }
}
