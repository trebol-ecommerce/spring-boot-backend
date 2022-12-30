package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.operation.ICompanyService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicAboutControllerTest {
  @InjectMocks PublicAboutController instance;
  @Mock ICompanyService companyService;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
