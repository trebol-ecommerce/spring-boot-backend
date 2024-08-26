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

package org.trebol.jpa.services.impl;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.trebol.jpa.services.SortSpecParserService;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Service
public class SortSpecParserServiceImpl
    implements SortSpecParserService {
    static final String SORT_PROPERTY_QUERY_MAP_KEY = "sortBy";
    static final String SORT_DIRECTION_QUERY_MAP_KEY = "order";

    @Override
    public Sort parse(
        @NotNull @NotEmpty Map<String, OrderSpecifier<?>> orderSpecMap,
        @NotNull Map<String, String> queryMap
    ) {
        if (!queryMap.containsKey(SORT_PROPERTY_QUERY_MAP_KEY)) {
            return Sort.unsorted();
        }
        String propertyName = queryMap.get(SORT_PROPERTY_QUERY_MAP_KEY);
        OrderSpecifier<?> orderSpecifier = orderSpecMap.get(propertyName);
        Sort sortBy = QSort.by(orderSpecifier);
        switch (queryMap.get(SORT_DIRECTION_QUERY_MAP_KEY)) {
            case "asc":
                return sortBy.ascending();
            case "desc":
                return sortBy.descending();
            default:
                return sortBy;
        }
    }
}
