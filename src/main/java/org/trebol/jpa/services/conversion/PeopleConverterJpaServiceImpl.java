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
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

@Service
public class PeopleConverterJpaServiceImpl
  implements IPeopleConverterJpaService {

  @Autowired
  public PeopleConverterJpaServiceImpl() {
  }

  @Override
  public PersonPojo convertToPojo(Person source) {
    PersonPojo target = PersonPojo.builder()
      .id(source.getId())
      .idNumber(source.getIdNumber())
      .firstName(source.getFirstName())
      .lastName(source.getLastName())
      .email(source.getEmail())
      .build();
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }

  @Override
  public Person convertToNewEntity(PersonPojo source) {
    Person target = new Person();
    target.setFirstName(source.getFirstName());
    target.setLastName(source.getLastName());
    target.setIdNumber(source.getIdNumber());
    target.setEmail(source.getEmail());
    if (source.getPhone1() != null) {
      target.setPhone1(source.getPhone1());
    }
    if (source.getPhone2() != null) {
      target.setPhone2(source.getPhone2());
    }
    return target;
  }

  @Override
  public Person applyChangesToExistingEntity(PersonPojo source, Person target) throws BadInputException {
    throw new UnsupportedOperationException("This method is deprecated");
  }
}
