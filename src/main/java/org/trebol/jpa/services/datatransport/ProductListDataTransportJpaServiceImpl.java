/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.datatransport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.pojo.ProductListPojo;

@Service
public class ProductListDataTransportJpaServiceImpl
  implements IProductListsDataTransportJpaService {

  @Autowired
  public ProductListDataTransportJpaServiceImpl(
  ) {
  }

  @Override
  public ProductList applyChangesToExistingEntity(ProductListPojo source, ProductList existing) throws BadInputException {
    ProductList target = new ProductList(existing);

    if (source.getName() != null && !source.getName().isEmpty() && !source.getName().equals(target.getName())) {
      target.setName(source.getName());
    }

    if (source.getCode() != null && !source.getCode().isEmpty() && !source.getCode().equals(target.getCode())) {
      target.setCode(source.getCode());
    }

    return target;
  }
}
