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

package org.trebol.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.trebol.jpa.DBEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(
    name = "products",
    indexes = {
        @Index(columnList = "product_name")
    })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product
    implements DBEntity {
    private static final long serialVersionUID = 10L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;
    @Size(max = 200)
    @Column(name = "product_name", nullable = false, unique = true)
    private String name;
    @Size(max = 50)
    @Column(name = "product_code", nullable = false, unique = true)
    private String barcode;
    @Size(max = 4000)
    @Column(name = "product_description")
    private String description;
    @Column(name = "product_price", nullable = false)
    private int price;
    @Column(name = "product_stock_current", nullable = false)
    private int stockCurrent;
    @Column(name = "product_stock_critical", nullable = false)
    private int stockCritical;
    @JoinColumn(name = "product_category_id", referencedColumnName = "product_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory productCategory;

    /**
     * Please note: this copy-constructor does not include a Product's relationship to a ProductCategory.
     *
     * @param source The original Product
     */
    public Product(Product source) {
        this.id = source.id;
        this.name = source.name;
        this.barcode = source.barcode;
        this.description = source.description;
        this.price = source.price;
        this.stockCurrent = source.stockCurrent;
        this.stockCritical = source.stockCritical;
        this.productCategory = null;
    }
}
