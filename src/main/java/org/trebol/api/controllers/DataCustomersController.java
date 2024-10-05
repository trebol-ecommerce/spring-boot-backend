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

package org.trebol.api.controllers;

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.api.DataCrudGenericController;
import org.trebol.api.models.DataPagePojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.services.PaginationService;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.SortSpecParserService;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.jpa.services.predicates.CustomersPredicateService;
import org.trebol.jpa.sortspecs.CustomersSortSpec;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/data/customers")
@PreAuthorize("isAuthenticated()")
public class DataCustomersController
    extends DataCrudGenericController<PersonPojo, Customer> {

    @Autowired
    public DataCustomersController(
        PaginationService paginationService,
        SortSpecParserService sortSpecParserService,
        CustomersCrudService crudService,
        CustomersPredicateService predicateService
    ) {
        super(paginationService, sortSpecParserService, crudService, predicateService);
    }

    @Override
    @GetMapping({"", "/"})
    @PreAuthorize("hasAuthority('customers:read')")
    public DataPagePojo<PersonPojo> readMany(@RequestParam Map<String, String> allRequestParams) {
        return super.readMany(allRequestParams);
    }

    @Override
    @PostMapping({"", "/"})
    @PreAuthorize("hasAuthority('customers:create')")
    @ResponseStatus(CREATED)
    public void create(@Valid @RequestBody PersonPojo input)
        throws BadInputException, EntityExistsException {
        crudService.create(input);
    }

    @Override
    @PutMapping({"", "/"})
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('customers:update')")
    public void update(@Valid @RequestBody PersonPojo input, @RequestParam Map<String, String> requestParams)
        throws EntityNotFoundException, BadInputException {
        super.update(input, requestParams);
    }

    @Override
    @DeleteMapping({"", "/"})
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAuthority('customers:delete')")
    public void delete(Map<String, String> requestParams)
        throws EntityNotFoundException {
        super.delete(requestParams);
    }

    @Override
    protected Map<String, OrderSpecifier<?>> getOrderSpecMap() {
        return CustomersSortSpec.ORDER_SPEC_MAP;
    }
}
