package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.operation.IReceiptService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicReceiptControllerTest {
  @InjectMocks PublicReceiptController instance;
  @Mock IReceiptService receiptService;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
