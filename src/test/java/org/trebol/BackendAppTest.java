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

package org.trebol;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.trebol.api.controllers.AccessController;
import org.trebol.api.controllers.AccountProfileController;
import org.trebol.api.controllers.DataBillingTypesController;
import org.trebol.api.controllers.DataCustomersController;
import org.trebol.api.controllers.DataImagesController;
import org.trebol.api.controllers.DataPeopleController;
import org.trebol.api.controllers.DataProductCategoriesController;
import org.trebol.api.controllers.DataProductListContentsController;
import org.trebol.api.controllers.DataProductListsController;
import org.trebol.api.controllers.DataProductsController;
import org.trebol.api.controllers.DataSalesController;
import org.trebol.api.controllers.DataSalespeopleController;
import org.trebol.api.controllers.DataSellStatusesController;
import org.trebol.api.controllers.DataShippersController;
import org.trebol.api.controllers.DataUserRolesController;
import org.trebol.api.controllers.DataUsersController;
import org.trebol.api.controllers.PublicAboutController;
import org.trebol.api.controllers.PublicCheckoutController;
import org.trebol.api.controllers.PublicReceiptController;
import org.trebol.api.controllers.PublicRegisterController;
import org.trebol.api.controllers.RootController;

import java.util.List;

@SpringBootTest
class BackendAppTest {
    @Autowired AccessController accessController;
    @Autowired AccountProfileController accountProfileController;
    @Autowired DataBillingTypesController billingTypesController;
    @Autowired DataCustomersController customersController;
    @Autowired DataImagesController imagesController;
    @Autowired DataPeopleController peopleController;
    @Autowired DataProductCategoriesController productCategoriesController;
    @Autowired DataProductListContentsController productListContentsController;
    @Autowired DataProductListsController productListsController;
    @Autowired DataProductsController productsController;
    @Autowired DataSalesController salesController;
    @Autowired DataSalespeopleController salespeopleController;
    @Autowired DataSellStatusesController sellStatusesController;
    @Autowired DataShippersController shippersController;
    @Autowired DataUserRolesController userRolesController;
    @Autowired DataUsersController usersController;
    @Autowired PublicAboutController aboutController;
    @Autowired PublicCheckoutController checkoutController;
    @Autowired PublicReceiptController receiptController;
    @Autowired PublicRegisterController registerController;
    @Autowired RootController rootController;

    @Test
    void sanity_check() {
        List.of(
            accessController,
            accountProfileController,
            billingTypesController,
            customersController,
            imagesController,
            peopleController,
            productCategoriesController,
            productListContentsController,
            productListsController,
            productsController,
            salesController,
            salespeopleController,
            sellStatusesController,
            shippersController,
            userRolesController,
            usersController,
            aboutController,
            checkoutController,
            receiptController,
            registerController,
            rootController
        ).forEach(Assertions::assertNotNull);
    }
}
