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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.conversion.CustomersConverterService;
import org.trebol.jpa.services.conversion.PeopleConverterService;

@Service
public class CustomersConverterServiceImpl
    implements CustomersConverterService {
    private final PeopleConverterService peopleConverterService;

    @Autowired
    public CustomersConverterServiceImpl(
        PeopleConverterService peopleConverterService
    ) {
        this.peopleConverterService = peopleConverterService;
    }

    @Override
    public CustomerPojo convertToPojo(Customer source) {
        PersonPojo targetPerson = peopleConverterService.convertToPojo(source.getPerson());
        return CustomerPojo.builder()
            .person(targetPerson)
            .build();
    }

    @Override
    public Customer convertToNewEntity(CustomerPojo source) throws BadInputException {
        Person targetPerson = peopleConverterService.convertToNewEntity(source.getPerson());
        return Customer.builder()
            .person(targetPerson)
            .build();
    }

    @Override
    public Customer applyChangesToExistingEntity(CustomerPojo source, Customer target) {
        throw new UnsupportedOperationException("This method is deprecated");
    }
}
