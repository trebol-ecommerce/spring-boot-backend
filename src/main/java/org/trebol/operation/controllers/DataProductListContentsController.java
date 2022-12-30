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

package org.trebol.operation.controllers;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.*;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.jpa.services.conversion.IProductListItemsConverterJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.DataPagePojo;
import org.trebol.pojo.ProductPojo;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/data/product_list_contents")
public class DataProductListContentsController {

  private static final String ITEM_NOT_FOUND = "Requested item(s) not found";

  private final PaginationService paginationService;
  private final ISortSpecJpaService<ProductListItem> sortService;
  private final IProductListItemsJpaRepository listItemsRepository;
  private final IProductListsJpaRepository listsRepository;
  private final IPredicateJpaService<ProductListItem> listItemsPredicateService;
  private final GenericCrudJpaService<ProductPojo, Product> productCrudService;
  private final IProductListItemsConverterJpaService itemConverterService;

  @Autowired
  public DataProductListContentsController(PaginationService paginationService,
                                           ISortSpecJpaService<ProductListItem> sortService,
                                           IProductListItemsJpaRepository listItemsRepository,
                                           IProductListsJpaRepository listsRepository,
                                           IPredicateJpaService<ProductListItem> listItemsPredicateService,
                                           GenericCrudJpaService<ProductPojo, Product> productCrudService,
                                           IProductListItemsConverterJpaService itemConverterService) {
    this.paginationService = paginationService;
    this.sortService = sortService;
    this.listItemsRepository = listItemsRepository;
    this.listsRepository = listsRepository;
    this.listItemsPredicateService = listItemsPredicateService;
    this.productCrudService = productCrudService;
    this.itemConverterService = itemConverterService;
  }

  @GetMapping({"", "/"})
  public DataPagePojo<ProductPojo> readContents(@RequestParam Map<String, String> requestParams)
      throws BadInputException, EntityNotFoundException {
    Optional<ProductList> match = this.fetchProductListByCode(requestParams);
    if (match.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    int pageIndex = paginationService.determineRequestedPageIndex(requestParams);
    int pageSize = paginationService.determineRequestedPageSize(requestParams);

    Pageable pagination;
    if (requestParams.containsKey("sortBy")) {
      Sort order = sortService.parseMap(requestParams);
      pagination = PageRequest.of(pageIndex, pageSize, order);
    } else {
      pagination = PageRequest.of(pageIndex, pageSize);
    }

    Predicate predicate = listItemsPredicateService.parseMap(requestParams);
    Page<ProductListItem> listItems = listItemsRepository.findAll(predicate, pagination);
    List<ProductPojo> products = new ArrayList<>();
    for (ProductListItem item : listItems) {
      ProductPojo productPojo = itemConverterService.convertToPojo(item);
      products.add(productPojo);
    }
    long totalCount = listItemsRepository.count(QProductListItem.productListItem.list.id.eq(match.get().getId()));

    return new DataPagePojo<>(products, pageIndex, totalCount, pageSize);
  }

  @PostMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void addToContents(@Valid @RequestBody ProductPojo input,
                            @RequestParam Map<String, String> requestParams)
      throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    Optional<Product> productMatch = productCrudService.getExisting(input);
    if (productMatch.isPresent()) {
      ProductListItem listItem = new ProductListItem(listMatch.get(), productMatch.get());
      if (!listItemsRepository.exists(Example.of(listItem))) {
        listItemsRepository.save(listItem);
      }
    }
  }

  @PutMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void updateContents(@RequestBody Collection<ProductPojo> input,
                             @RequestParam Map<String, String> requestParams)
      throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    listItemsRepository.deleteByListId(listMatch.get().getId());
    for (ProductPojo p : input) {
      Optional<Product> productMatch = productCrudService.getExisting(p);
      if (productMatch.isPresent()) {
        ProductListItem listItem = new ProductListItem(listMatch.get(), productMatch.get());
        if (!listItemsRepository.exists(Example.of(listItem))) {
          listItemsRepository.save(listItem);
        }
      }
    }
  }

  @DeleteMapping({"", "/"})
  @PreAuthorize("hasAuthority('product_lists:contents')")
  public void deleteFromContents(@RequestParam Map<String, String> requestParams)
      throws BadInputException, EntityNotFoundException {
    Optional<ProductList> listMatch = this.fetchProductListByCode(requestParams);
    if (listMatch.isEmpty()) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    }

    Predicate predicate = listItemsPredicateService.parseMap(requestParams);
    listItemsRepository.deleteAll(listItemsRepository.findAll(predicate));
  }

  private Optional<ProductList> fetchProductListByCode(Map<String, String> requestParams) throws BadInputException {
    String listCode = requestParams.get("listCode");
    if (listCode == null || listCode.isBlank()) {
      throw new BadInputException("listCode query param is required");
    }
    return listsRepository.findOne(QProductList.productList.code.eq(listCode));
  }
}
