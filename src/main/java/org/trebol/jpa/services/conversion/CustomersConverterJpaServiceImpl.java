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

package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

@Service
public class CustomersConverterJpaServiceImpl
  implements ICustomersConverterJpaService {

  private final IPeopleConverterJpaService peopleService;

  @Autowired
  public CustomersConverterJpaServiceImpl(IPeopleConverterJpaService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public CustomerPojo convertToPojo(Customer source) {
    PersonPojo targetPerson = peopleService.convertToPojo(source.getPerson());
    return CustomerPojo.builder()
      .id(source.getId())
      .person(targetPerson)
      .build();
  }

  @Override
  public Customer convertToNewEntity(CustomerPojo source) throws BadInputException {
    Customer target = new Customer();
    Person targetPerson = peopleService.convertToNewEntity(source.getPerson());
    target.setPerson(targetPerson);
    return target;
  }

  @Override
  public Customer applyChangesToExistingEntity(CustomerPojo source, Customer target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
