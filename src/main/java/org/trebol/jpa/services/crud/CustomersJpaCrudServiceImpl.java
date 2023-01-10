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

package org.trebol.jpa.services.crud;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.repositories.ICustomersJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.ICustomersConverterJpaService;
import org.trebol.jpa.services.datatransport.ICustomersDataTransportJpaService;
import org.trebol.pojo.CustomerPojo;

import java.util.Optional;

@Transactional
@Service
public class CustomersJpaCrudServiceImpl
  extends GenericCrudJpaService<CustomerPojo, Customer>
  implements ICustomersCrudService {

  private final ICustomersJpaRepository customersRepository;

  @Autowired
  public CustomersJpaCrudServiceImpl(ICustomersJpaRepository repository,
                                     ICustomersConverterJpaService converter,
                                     ICustomersDataTransportJpaService dataTransportService) {
    super(repository,
          converter,
          dataTransportService);
    this.customersRepository = repository;
  }

  @Override
  public Optional<Customer> getExisting(CustomerPojo input) throws BadInputException {
    String idNumber = input.getPerson().getIdNumber();
    if (StringUtils.isBlank(idNumber)) {
      throw new BadInputException("Customer does not have an ID card");
    } else {
      return customersRepository.findByPersonIdNumber(idNumber);
    }
  }
}
