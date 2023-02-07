/*
 * Copyright (c) 2023 The Trebol eCommerce Project
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
import org.springframework.stereotype.Service;
import org.trebol.api.models.ProductPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.patch.ProductsPatchService;

@Service
@NoArgsConstructor
public class ProductsPatchServiceImpl
  implements ProductsPatchService {

  @Override
  public Product patchExistingEntity(ProductPojo changes, Product existing) {
    Product target = new Product(existing);

    String barcode = changes.getBarcode();
    if (barcode != null && !barcode.isBlank() && !target.getBarcode().equals(barcode)) {
      target.setBarcode(barcode);
    }

    String name = changes.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    Integer price = changes.getPrice();
    target.setPrice(price);

    String description = changes.getDescription();
    if (description != null) {
      target.setDescription(description);
    }

    Integer currentStock = changes.getCurrentStock();
    if (currentStock != null) {
      target.setStockCurrent(currentStock);
    }

    return target;
  }
}
