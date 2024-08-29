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

package org.trebol.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.trebol.api.models.LoginPojo;
import org.trebol.config.SecurityProperties;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtLoginAuthenticationFilter
    extends GenericJwtAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtLoginAuthenticationFilter(
        SecurityProperties jwtProperties,
        SecretKey secretKey,
        AuthenticationManager authenticationManager
    ) {
        super(jwtProperties, secretKey);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            return null;
        } else {
            try {
                LoginPojo userData = new ObjectMapper().readValue(request.getInputStream(), LoginPojo.class);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userData.getName(),
                    userData.getPassword());
                return authenticationManager.authenticate(authentication);
            } catch (IOException e) {
                throw new BadCredentialsException("Request body is not a login request");
            }
        }
    }
}
