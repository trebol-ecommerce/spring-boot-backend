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

package org.trebol.jpa.services.crud.impl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.UserRolePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.UserRolesRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRolesCrudServiceImplTest {
    @InjectMocks UserRolesCrudServiceImpl instance;
    @Mock UserRolesRepository userRolesRepositoryMock;

    @Test
    void finds_by_name() throws BadInputException {
        String roleName = "test-role";
        UserRolePojo input = UserRolePojo.builder()
            .name(roleName)
            .build();
        UserRole persistedEntity = new UserRole(1L, roleName);
        when(userRolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(persistedEntity));

        Optional<UserRole> match = instance.getExisting(input);

        verify(userRolesRepositoryMock).findByName(roleName);
        assertTrue(match.isPresent());
        assertEquals(persistedEntity, match.get());
    }
}
