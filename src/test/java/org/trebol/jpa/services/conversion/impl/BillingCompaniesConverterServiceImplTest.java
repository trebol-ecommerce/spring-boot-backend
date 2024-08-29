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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.api.models.BillingCompanyPojo;
import org.trebol.jpa.entities.BillingCompany;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.trebol.testing.TestConstants.ANY;

class BillingCompaniesConverterServiceImplTest {
    BillingCompaniesConverterServiceImpl instance;

    @BeforeEach
    void beforeEach() {
        instance = new BillingCompaniesConverterServiceImpl();
    }

    @Test
    void converts_to_pojo() {
        BillingCompany input = BillingCompany.builder()
            .id(1L)
            .name(ANY)
            .idNumber(ANY)
            .build();
        BillingCompanyPojo result = instance.convertToPojo(input);
        assertNotNull(result);
        assertEquals(input.getIdNumber(), result.getIdNumber());
        assertEquals(input.getName(), result.getName());
    }

    @Test
    void converts_to_new_entity() {
        BillingCompanyPojo input = BillingCompanyPojo.builder()
            .idNumber(ANY)
            .name(ANY)
            .build();
        BillingCompany result = instance.convertToNewEntity(input);
        assertNotNull(result);
        assertEquals(input.getIdNumber(), result.getIdNumber());
        assertEquals(input.getName(), result.getName());
    }
}
