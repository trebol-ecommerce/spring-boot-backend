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

package org.trebol.operation.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.jpa.repositories.ISellDetailsJpaRepository;
import org.trebol.jpa.repositories.ISellStatusesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IProductsConverterJpaService;
import org.trebol.jpa.services.conversion.ISalesConverterJpaService;
import org.trebol.jpa.services.crud.ISalesCrudService;
import org.trebol.operation.ISalesProcessService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.SellDetailPojo;
import org.trebol.pojo.SellPojo;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.trebol.config.Constants.*;

@Transactional
@Service
public class SalesProcessServiceImpl
  implements ISalesProcessService {

  private static final String THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION = "The transaction is not in a valid state for this operation";
  private static final String NO_STATUS_MATCHES_THE = "No status matches the";
  private static final String NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT = "name - Is the database empty or corrupt?";
  private final ISalesCrudService crudService;
  private final ISalesJpaRepository salesRepository;
  private final ISellDetailsJpaRepository sellDetailsRepository;
  private final ISellStatusesJpaRepository sellStatusesRepository;
  private final ISalesConverterJpaService converterService;
  private final IProductsConverterJpaService productConverterService;

  public SalesProcessServiceImpl(ISalesCrudService crudService,
                                 ISalesJpaRepository salesRepository,
                                 ISellDetailsJpaRepository sellDetailsRepository,
                                 ISellStatusesJpaRepository sellStatusesRepository,
                                 ISalesConverterJpaService converterService,
                                 IProductsConverterJpaService productConverterService) {
    this.crudService = crudService;
    this.salesRepository = salesRepository;
    this.sellDetailsRepository = sellDetailsRepository;
    this.sellStatusesRepository = sellStatusesRepository;
    this.converterService = converterService;
    this.productConverterService = productConverterService;
  }

  // TODO figure out how to shorten below methods
  // TODO to compare statuses use numbers, not strings

  @Override
  public SellPojo markAsStarted(SellPojo sell) throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PENDING)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> startedStatus = sellStatusesRepository.findByName(SELL_STATUS_PAYMENT_STARTED);
    if (startedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_PAYMENT_STARTED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), startedStatus.get());
    salesRepository.setTransactionToken(existingSell.getId(), sell.getToken());

    SellPojo target = this.convertOrThrowException(existingSell);
    target.setStatus(SELL_STATUS_PAYMENT_STARTED);
    return target;
  }

  @Override
  public SellPojo markAsAborted(SellPojo sell) throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAYMENT_STARTED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> abortedStatus = sellStatusesRepository.findByName(SELL_STATUS_PAYMENT_CANCELLED);
    if (abortedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_PAYMENT_CANCELLED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), abortedStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);
    target.setStatus(SELL_STATUS_PAYMENT_CANCELLED);
    return target;
  }

  @Override
  public SellPojo markAsFailed(SellPojo sell) throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAYMENT_STARTED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> failedStatus = sellStatusesRepository.findByName(SELL_STATUS_PAYMENT_FAILED);
    if (failedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_PAYMENT_FAILED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), failedStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);
    target.setStatus(SELL_STATUS_PAYMENT_FAILED);
    return target;
  }

  @Override
  public SellPojo markAsPaid(SellPojo sell) throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAYMENT_STARTED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> paidStatus = sellStatusesRepository.findByName(SELL_STATUS_PAID_UNCONFIRMED);
    if (paidStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_PAID_UNCONFIRMED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), paidStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);

    List<SellDetailPojo> pojoDetails = new ArrayList<>();
    for (SellDetail detail : sellDetailsRepository.findBySellId(existingSell.getId())) {
      ProductPojo productPojo = productConverterService.convertToPojo(detail.getProduct());
      SellDetailPojo sellDetailPojo = SellDetailPojo.builder()
        .id(detail.getId())
        .units(detail.getUnits())
        .unitValue(detail.getUnitValue())
        .product(productPojo)
        .build();
      pojoDetails.add(sellDetailPojo);
    }
    target.setStatus(SELL_STATUS_PAID_UNCONFIRMED);
    target.setDetails(pojoDetails);
    
    return target;
  }

  @Override
  public SellPojo markAsConfirmed(SellPojo sell)
      throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAID_UNCONFIRMED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> confirmedStatus = sellStatusesRepository.findByName(SELL_STATUS_PAID_CONFIRMED);
    if (confirmedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_PAID_CONFIRMED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), confirmedStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);

    List<SellDetailPojo> pojoDetails = new ArrayList<>();
    for (SellDetail detail : sellDetailsRepository.findBySellId(existingSell.getId())) {
      ProductPojo productPojo = productConverterService.convertToPojo(detail.getProduct());
      SellDetailPojo sellDetailPojo = SellDetailPojo.builder()
        .id(detail.getId())
        .units(detail.getUnits())
        .unitValue(detail.getUnitValue())
        .product(productPojo)
        .build();
      pojoDetails.add(sellDetailPojo);
    }
    target.setDetails(pojoDetails);
    target.setStatus(SELL_STATUS_PAID_CONFIRMED);


    return target;
  }

  @Override
  public SellPojo markAsRejected(SellPojo sell)
      throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAID_UNCONFIRMED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> rejectedStatus = sellStatusesRepository.findByName(SELL_STATUS_REJECTED);
    if (rejectedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_REJECTED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), rejectedStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);

    List<SellDetailPojo> pojoDetails = new ArrayList<>();
    for (SellDetail detail : sellDetailsRepository.findBySellId(existingSell.getId())) {
      ProductPojo productPojo = productConverterService.convertToPojo(detail.getProduct());
      SellDetailPojo sellDetailPojo = SellDetailPojo.builder()
        .id(detail.getId())
        .units(detail.getUnits())
        .unitValue(detail.getUnitValue())
        .product(productPojo)
        .build();
      pojoDetails.add(sellDetailPojo);
    }
    target.setDetails(pojoDetails);
    target.setStatus(SELL_STATUS_REJECTED);

    return target;
  }

  @Override
  public SellPojo markAsCompleted(SellPojo sell)
      throws BadInputException, EntityNotFoundException {
    Sell existingSell = this.fetchExistingOrThrowException(sell);

    if (!existingSell.getStatus().getName().equals(SELL_STATUS_PAID_CONFIRMED)) {
      throw new BadInputException(THE_TRANSACTION_IS_NOT_IN_A_VALID_STATE_FOR_THIS_OPERATION);
    }

    Optional<SellStatus> completedStatus = sellStatusesRepository.findByName(SELL_STATUS_COMPLETED);
    if (completedStatus.isEmpty()) {
      throw new IllegalStateException(NO_STATUS_MATCHES_THE + " '" + SELL_STATUS_COMPLETED + "' " + NAME_IS_THE_DATABASE_EMPTY_OR_CORRUPT);
    }
    salesRepository.setStatus(existingSell.getId(), completedStatus.get());

    SellPojo target = this.convertOrThrowException(existingSell);

    List<SellDetailPojo> pojoDetails = new ArrayList<>();
    for (SellDetail detail : sellDetailsRepository.findBySellId(existingSell.getId())) {
      ProductPojo productPojo = productConverterService.convertToPojo(detail.getProduct());
      SellDetailPojo sellDetailPojo = SellDetailPojo.builder()
        .id(detail.getId())
        .units(detail.getUnits())
        .unitValue(detail.getUnitValue())
        .product(productPojo)
        .build();
      pojoDetails.add(sellDetailPojo);
    }
    target.setDetails(pojoDetails);
    target.setStatus(SELL_STATUS_COMPLETED);

    return target;
  }

  private Sell fetchExistingOrThrowException(SellPojo sell) throws BadInputException {
    Optional<Sell> match = crudService.getExisting(sell);
    if (match.isEmpty()) {
      throw new EntityNotFoundException("No transaction matches given input");
    }
    return match.get();
  }

  private SellPojo convertOrThrowException(Sell existingSell) {
    Sell freshInstance = salesRepository.getById(existingSell.getId());
    SellPojo target = converterService.convertToPojo(freshInstance);
    if (target == null) {
      throw new IllegalStateException("Converter could not turn Sell into its Pojo equivalent");
    }
    return target;
  }
}
