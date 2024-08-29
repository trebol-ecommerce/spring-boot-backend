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

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.trebol.api.models.AddressPojo;
import org.trebol.jpa.entities.Address;
import org.trebol.jpa.services.conversion.AddressesConverterService;

@Service
@NoArgsConstructor
public class AddressesConverterServiceImpl
    implements AddressesConverterService {

    @Override
    public AddressPojo convertToPojo(Address source) {
        AddressPojo target = AddressPojo.builder()
            .city(source.getCity())
            .municipality(source.getMunicipality())
            .firstLine(source.getFirstLine())
            .build();
        if (!StringUtils.isBlank(source.getSecondLine())) {
            target.setSecondLine(source.getSecondLine());
        }
        if (!StringUtils.isBlank(source.getPostalCode())) {
            target.setPostalCode(source.getPostalCode());
        }
        if (!StringUtils.isBlank(source.getNotes())) {
            target.setNotes(source.getNotes());
        }
        return target;
    }

    @Override
    public Address convertToNewEntity(AddressPojo source) {
        return Address.builder()
            .firstLine(source.getFirstLine())
            .secondLine(source.getSecondLine())
            .city(source.getCity())
            .municipality(source.getMunicipality())
            .postalCode(source.getPostalCode())
            .notes((source.getNotes()))
            .build();
    }

    @Override
    public Address applyChangesToExistingEntity(AddressPojo source, Address target) {
        throw new UnsupportedOperationException("This method is deprecated");
    }
}
