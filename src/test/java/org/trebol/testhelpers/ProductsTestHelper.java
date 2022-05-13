package org.trebol.testhelpers;

import org.trebol.jpa.entities.Product;
import org.trebol.pojo.ProductPojo;

import java.util.List;

/**
 * Builds & caches reusable instances of Product and ProductPojo
 */
public class ProductsTestHelper {

  public static final long PRODUCT_ID = 1L;
  public static final String PRODUCT_NAME = "test product name";
  public static final String PRODUCT_BARCODE = "TESTPROD1";
  public static final String PRODUCT_DESCRIPTION = "test product description";
  public static final int PRODUCT_PRICE = 100;
  public static final int PRODUCT_STOCK = 10;
  public static final int PRODUCT_STOCK_CRITICAL = 1;
  private static ProductPojo pojoForFetch;
  private static ProductPojo pojoBeforeCreation;
  private static ProductPojo pojoAfterCreation;
  private static Product entityBeforeCreation;
  private static Product entityAfterCreation;

  public static void resetProducts() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static ProductPojo productPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new ProductPojo(PRODUCT_BARCODE);
    }
    return pojoForFetch;
  }

  public static ProductPojo productPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new ProductPojo(null, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION, PRODUCT_PRICE,
                                           PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL, null, List.of());
    }
    return pojoBeforeCreation;
  }

  public static ProductPojo productPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new ProductPojo(PRODUCT_ID, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION,
                                          PRODUCT_PRICE, PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL, null, List.of());
    }
    return pojoAfterCreation;
  }

  public static Product productEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new Product(PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION, PRODUCT_PRICE,
                                         PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL);
    }
    return entityBeforeCreation;
  }

  public static Product productEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_BARCODE, PRODUCT_DESCRIPTION,
                                        PRODUCT_PRICE, PRODUCT_STOCK, PRODUCT_STOCK_CRITICAL, null);
    }
    return entityAfterCreation;
  }
}
