package org.trebol.api.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.trebol.api.pojo.ReceiptPojo;
import org.trebol.api.IReceiptService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/public/receipt")
public class PublicReceiptController {
  private final Logger LOG = LoggerFactory.getLogger(PublicReceiptController.class);

  private final IReceiptService receiptService;

  @Autowired
  public PublicReceiptController(IReceiptService receiptService) {
    this.receiptService = receiptService;
  }

  @GetMapping({"/{id}", "/{id}/"})
  public ReceiptPojo fetchReceiptById(@PathVariable("id") Integer id) {
    if (id == null) {
      throw new RuntimeException("An incorrect receipt id was provided");
    }
    return this.receiptService.fetchReceiptById(id);
  }
}
