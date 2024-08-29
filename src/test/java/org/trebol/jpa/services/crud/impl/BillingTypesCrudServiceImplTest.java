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


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.BillingTypePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.repositories.BillingTypesRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class BillingTypesCrudServiceImplTest {
    @InjectMocks BillingTypesCrudServiceImpl instance;
    @Mock BillingTypesRepository billingTypesRepositoryMock;

    @Test
    void finds_by_name() throws BadInputException {
        BillingTypePojo example = BillingTypePojo.builder()
            .name(ANY)
            .build();
        BillingType expectedResult = BillingType.builder()
            .id(1L)
            .name(ANY)
            .build();
        when(billingTypesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(expectedResult));
        Optional<BillingType> match = instance.getExisting(example);
        assertTrue(match.isPresent());
        assertEquals(expectedResult, match.get());
        verify(billingTypesRepositoryMock).findByName(example.getName());
    }
}
