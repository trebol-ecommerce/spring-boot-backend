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

package org.trebol.operation;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.trebol.jpa.services.ICrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.pojo.DataPagePojo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * RestController that implements IDataController with a GenericJpaService
 * @param <P> The Pojo class
 * @param <E> The Entity class
 */
public abstract class GenericDataController<P, E>
  implements IDataController<P> {

  protected PaginationService paginationService;
  protected ISortSpecJpaService<E> sortService;
  protected final ICrudJpaService<P, E> crudService;
  protected final IPredicateJpaService<E> predicateService;

  public GenericDataController(PaginationService paginationService,
                               ISortSpecJpaService<E> sortService,
                               ICrudJpaService<P, E> crudService,
                               IPredicateJpaService<E> predicateService) {
    this.paginationService = paginationService;
    this.sortService = sortService;
    this.crudService = crudService;
    this.predicateService = predicateService;
  }

  /**
   * Retrieve a page of items with a fixed size and offset index.
   * An optional Map (like query string parameters) can be provided for filtering criteria
   * @param requestParams May contain filtering conditions and/or page size & page index parameters.
   * @return A paged collection of Pojos.
   */
  @Override
  public DataPagePojo<P> readMany(@NotNull Map<String, String> requestParams) {

    int pageIndex = paginationService.determineRequestedPageIndex(requestParams);
    int pageSize = paginationService.determineRequestedPageSize(requestParams);

    Sort order = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      order = sortService.parseMap(requestParams);
    }

    Predicate filters = null;
    if (requestParams != null && !requestParams.isEmpty()) {
      filters = predicateService.parseMap(requestParams);
    }

    return crudService.readMany(pageIndex, pageSize, order, filters);
  }
}
