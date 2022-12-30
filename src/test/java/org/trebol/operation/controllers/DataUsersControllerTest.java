package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.ISortSpecJpaService;
import org.trebol.operation.PaginationService;
import org.trebol.pojo.UserPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DataUsersControllerTest {
  @InjectMocks DataUsersController instance;
  @Mock PaginationService paginationService;
  @Mock ISortSpecJpaService<User> sortService;
  @Mock GenericCrudJpaService<UserPojo, User> crudService;
  @Mock IPredicateJpaService<User> predicateService;

  @Test
  void sanity_check() {
    assertNotNull(instance);
  }
}
