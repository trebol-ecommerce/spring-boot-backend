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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.api.models.PersonPojo;
import org.trebol.common.Utils;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.patch.CustomersPatchService;
import org.trebol.jpa.services.patch.PeoplePatchService;

import java.util.Map;

import static org.trebol.config.Constants.PERSON_DATA_MAP_KEYS_PREFIX;

@Service
public class CustomersPatchServiceImpl
    implements CustomersPatchService {
    private final PeoplePatchService peoplePatchService;

    @Autowired
    public CustomersPatchServiceImpl(
        PeoplePatchService peoplePatchService
    ) {
        this.peoplePatchService = peoplePatchService;
    }

    @Override
    public Customer patchExistingEntity(Map<String, Object> changes, Customer existing) throws BadInputException {
        Customer target = new Customer(existing);

        Map<String, Object> personChanges = Utils.copyMapWithUnprefixedEntries(changes, PERSON_DATA_MAP_KEYS_PREFIX);
        if (!personChanges.isEmpty()) {
            Person existingPerson = existing.getPerson();
            Person person = peoplePatchService.patchExistingEntity(personChanges, existingPerson);
            target.setPerson(person);
        }

        return target;
    }

    @Override
    public Customer patchExistingEntity(PersonPojo changes, Customer existing) throws BadInputException {
        throw new UnsupportedOperationException("This method has been deprecated");
    }
}
