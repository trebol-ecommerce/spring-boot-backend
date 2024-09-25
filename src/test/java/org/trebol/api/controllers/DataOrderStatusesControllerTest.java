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

package org.trebol.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.DataGenericControllerTest;
import org.trebol.api.models.OrderStatusPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.jpa.entities.OrderStatus;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.OrderStatusesCrudService;
import org.trebol.jpa.services.predicates.OrderStatusesPredicateService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class DataOrderStatusesControllerTest
    extends DataGenericControllerTest<OrderStatusPojo, OrderStatus> {
    @InjectMocks
    DataOrderStatusesController instance;
    @Mock PaginationService paginationServiceMock;
    @Mock SortSpecParserService sortServiceMock;
    @Mock
    OrderStatusesCrudService crudServiceMock;
    @Mock
    OrderStatusesPredicateService predicateServiceMock;

    @BeforeEach
    protected void beforeEach() {
        super.instance = instance;
        super.crudServiceMock = crudServiceMock;
    }

    @Test
    void reads_people_data() {
        assertDoesNotThrow(() -> {
            super.reads_data(null);
            super.reads_data(Map.of());
            super.reads_data(Map.of(ANY, ANY));
        });
    }
}
