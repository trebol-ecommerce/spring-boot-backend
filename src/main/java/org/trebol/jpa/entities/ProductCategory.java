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

package org.trebol.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trebol.jpa.DBEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "product_categories",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"parent_product_category_id", "product_category_name"})
    })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductCategory
    implements DBEntity {
    private static final long serialVersionUID = 11L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id", nullable = false)
    private Long id;
    @Size(min = 1, max = 50)
    @Column(name = "product_category_code", nullable = false, unique = true)
    private String code;
    @Size(min = 1, max = 100)
    @Column(name = "product_category_name", nullable = false)
    private String name;
    @JoinColumn(name = "parent_product_category_id", referencedColumnName = "product_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductCategory parent;

    /**
     * Please note: this copy-constructor does NOT include a ProductCategory's relationship to its parent.
     *
     * @param source The original ProductCategory
     */
    public ProductCategory(ProductCategory source) {
        this.id = source.id;
        this.code = source.code;
        this.name = source.name;
        this.parent = null;
    }

    /**
     * This override prevents looping through the entire parent-tree.
     *
     * @return An equivalent result to lombok autogenerated toString() method,
     * except that this will only reveal info about its immediate parent.
     */
    @Override
    public String toString() {
        return "ProductCategory(" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ((parent!=null) ?
                (", parent=ProductCategory(" +
                    "id=" + parent.id +
                    ", code=" + parent.code +
                    ", name=" + parent.name +
                    ')'
                ):
                ", parent=null") +
            ')';
    }
}
