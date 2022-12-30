/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.datatransport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

@Service
public class CustomersDataTransportJpaServiceImpl
  implements ICustomersDataTransportJpaService {

  private final IPeopleDataTransportJpaService peopleService;

  @Autowired
  public CustomersDataTransportJpaServiceImpl(IPeopleDataTransportJpaService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public Customer applyChangesToExistingEntity(CustomerPojo source, Customer existing) throws BadInputException {
    Customer target = new Customer(existing);
    Person existingPerson = existing.getPerson();

    PersonPojo sourcePerson = source.getPerson();
    Person person = peopleService.applyChangesToExistingEntity(sourcePerson, existingPerson);
    target.setPerson(person);

    return target;
  }
}
