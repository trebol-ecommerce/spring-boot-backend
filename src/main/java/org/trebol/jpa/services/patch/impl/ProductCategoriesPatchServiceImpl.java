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

package org.trebol.jpa.services.patch.impl;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.patch.ProductCategoriesPatchService;

import java.util.Map;

@Service
@NoArgsConstructor
public class ProductCategoriesPatchServiceImpl
    implements ProductCategoriesPatchService {

    @Override
    public ProductCategory patchExistingEntity(Map<String, Object> changes, ProductCategory existing) throws BadInputException {
        ProductCategory target = new ProductCategory(existing);

        if (changes.containsKey("code")) {
            String code = (String) changes.get("code");
            if (!StringUtils.isBlank(code)) {
                target.setCode(code);
            }
        }

        if (changes.containsKey("name")) {
            String name = (String) changes.get("name");
            if (!StringUtils.isBlank(name)) {
                target.setName(name);
            }
        }

        return target;
    }

    @Override
    public ProductCategory patchExistingEntity(ProductCategoryPojo changes, ProductCategory existing) throws BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
    }
}
