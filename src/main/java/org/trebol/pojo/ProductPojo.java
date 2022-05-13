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

package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ProductPojo {
  @JsonIgnore
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String barcode;
  @JsonInclude(NON_EMPTY)
  private String description;
  @NotNull
  private Integer price;
  @JsonIgnore
  private Integer currentStock;
  @JsonIgnore
  private Integer criticalStock;
  private ProductCategoryPojo category;
  private Collection<ImagePojo> images;

  public ProductPojo() { }

  public ProductPojo(String barcode) {
    this.barcode = barcode;
  }

  public ProductPojo(Long id,
                     String name,
                     String barcode,
                     String description,
                     Integer price,
                     Integer currentStock,
                     Integer criticalStock,
                     ProductCategoryPojo category,
                     Collection<ImagePojo> images) {
    this.id = id;
    this.name = name;
    this.barcode = barcode;
    this.description = description;
    this.price = price;
    this.currentStock = currentStock;
    this.criticalStock = criticalStock;
    this.category = category;
    this.images = images;
  }
}
