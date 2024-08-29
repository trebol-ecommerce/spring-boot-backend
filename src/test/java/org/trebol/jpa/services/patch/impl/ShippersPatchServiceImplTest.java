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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ShipperPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Shipper;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class ShippersPatchServiceImplTest {
    @InjectMocks ShippersPatchServiceImpl instance;
    private static ObjectMapper MAPPER;
    private static Shipper EXISTING_SHIPPER;

    @BeforeAll
    static void beforeAll() {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(NON_NULL);
        EXISTING_SHIPPER = Shipper.builder()
            .id(1L)
            .name(ANY)
            .build();
    }

    @Test
    void performs_empty_patch() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ShipperPojo.builder().build());
        Shipper result = instance.patchExistingEntity(input, EXISTING_SHIPPER);
        assertEquals(EXISTING_SHIPPER, result);
    }

    @Test
    void patches_name() throws BadInputException {
        Map<String, Object> input = this.mapFrom(ShipperPojo.builder()
            .name(NOT_ANY)
            .build());
        Shipper result = instance.patchExistingEntity(input, EXISTING_SHIPPER);
        assertNotEquals(EXISTING_SHIPPER, result);
        assertEquals(NOT_ANY, result.getName());
    }

    @Test
    void does_not_support_old_method_signature() {
        ShipperPojo input = ShipperPojo.builder().build();
        assertThrows(UnsupportedOperationException.class,
            () -> instance.patchExistingEntity(input, EXISTING_SHIPPER));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> mapFrom(ShipperPojo data) {
        return MAPPER.convertValue(data, Map.class);
    }
}
