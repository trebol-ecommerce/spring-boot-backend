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

package org.trebol.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.trebol.config.SecurityProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.crud.ICustomersCrudService;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

import javax.crypto.SecretKey;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtGuestAuthenticationFilter
  extends GenericJwtAuthenticationFilter {

  private final Logger myLogger = LoggerFactory.getLogger(JwtGuestAuthenticationFilter.class);
  private final AuthenticationManager authenticationManager;
  private final ICustomersCrudService customersService;

  public JwtGuestAuthenticationFilter(SecurityProperties jwtProperties,
                                      SecretKey secretKey,
                                      AuthenticationManager authenticationManager,
                                      ICustomersCrudService customersService) {
    super(jwtProperties, secretKey);
    this.authenticationManager = authenticationManager;
    this.customersService = customersService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
    if (!HttpMethod.POST.matches(request.getMethod())) {
      return null;
    } else {
      try {
        PersonPojo guestCustomerData = new ObjectMapper().readValue(request.getInputStream(), PersonPojo.class);
        this.saveCustomerData(guestCustomerData);
        Authentication authentication = new UsernamePasswordAuthenticationToken("guest", "guest");
        return authenticationManager.authenticate(authentication);
      } catch (IOException e) {
        throw new BadCredentialsException("Invalid request body for guest session");
      } catch (BadInputException e) {
        throw new BadCredentialsException("Insufficient or invalid profile data for guest");
      }
    }
  }

  private void saveCustomerData(PersonPojo guestData) throws BadInputException {
    try {
      CustomerPojo targetCustomer = CustomerPojo.builder().person(guestData).build();
      customersService.create(targetCustomer);
    } catch (EntityExistsException e) {
      myLogger.info("Guest with idNumber={} is already registered in the database", guestData.getIdNumber());
    }
  }
}
