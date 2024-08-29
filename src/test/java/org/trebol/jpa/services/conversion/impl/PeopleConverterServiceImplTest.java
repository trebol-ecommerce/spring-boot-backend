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
import org.trebol.api.models.PersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.testing.PeopleTestHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PeopleConverterServiceImplTest {
    PeopleConverterServiceImpl instance;
    final PeopleTestHelper peopleTestHelper = new PeopleTestHelper();

    @BeforeEach
    void beforeEach() {
        instance = new PeopleConverterServiceImpl();
        peopleTestHelper.resetPeople();
    }

    @Test
    void testConvertToPojo() {
        Person input = peopleTestHelper.personEntityAfterCreation();
        PersonPojo result = instance.convertToPojo(input);
        assertNotNull(result);
        assertEquals(input.getIdNumber(), result.getIdNumber());
        assertEquals(input.getFirstName(), result.getFirstName());
        assertEquals(input.getLastName(), result.getLastName());
        assertEquals(input.getEmail(), result.getEmail());
        assertEquals(input.getPhone1(), result.getPhone1());
        assertEquals(input.getPhone2(), result.getPhone2());
    }

    @Test
    void testConvertToNewEntity() {
        PersonPojo input = peopleTestHelper.personPojoBeforeCreation();
        Person result = instance.convertToNewEntity(input);
        assertNotNull(result);
        assertEquals(input.getIdNumber(), result.getIdNumber());
        assertEquals(input.getFirstName(), result.getFirstName());
        assertEquals(input.getLastName(), result.getLastName());
        assertEquals(input.getEmail(), result.getEmail());
        assertEquals(input.getPhone1(), result.getPhone1());
        assertEquals(input.getPhone2(), result.getPhone2());
    }
}
