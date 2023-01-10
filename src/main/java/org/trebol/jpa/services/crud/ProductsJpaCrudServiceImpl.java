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

package org.trebol.jpa.services.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IImagesConverterJpaService;
import org.trebol.jpa.services.conversion.IProductCategoriesConverterJpaService;
import org.trebol.jpa.services.conversion.IProductsConverterJpaService;
import org.trebol.jpa.services.datatransport.IProductsDataTransportJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ProductsJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductPojo, Product>
  implements IProductsCrudService {

  private final IProductsJpaRepository productsRepository;
  private final IProductImagesJpaRepository productImagesRepository;
  private final IImagesCrudService imagesCrudService;
  private final IProductCategoriesCrudService categoriesCrudService;
  private final IProductCategoriesConverterJpaService categoriesConverter;
  private final IImagesConverterJpaService imageConverter;
  private final Logger logger = LoggerFactory.getLogger(ProductsJpaCrudServiceImpl.class);

  @Autowired
  public ProductsJpaCrudServiceImpl(IProductsJpaRepository repository,
                                    IProductsConverterJpaService converter,
                                    IProductsDataTransportJpaService dataTransportService,
                                    IProductImagesJpaRepository productImagesRepository,
                                    IImagesCrudService imagesCrudService,
                                    IProductCategoriesCrudService categoriesService,
                                    IProductCategoriesConverterJpaService categoriesConverter,
                                    IImagesConverterJpaService imageConverter) {
    super(repository,
          converter,
          dataTransportService);
    this.productsRepository = repository;
    this.imagesCrudService = imagesCrudService;
    this.categoriesConverter = categoriesConverter;
    this.productImagesRepository = productImagesRepository;
    this.categoriesCrudService = categoriesService;
    this.imageConverter = imageConverter;
  }

  @Transactional
  @Override
  public ProductPojo create(ProductPojo inputPojo)
      throws BadInputException, EntityExistsException {
    this.validateInputPojoBeforeCreation(inputPojo);
    Product prepared = this.prepareNewEntityFromInputPojo(inputPojo);
    Product persistent = productsRepository.saveAndFlush(prepared);
    ProductPojo outputPojo = converter.convertToPojo(persistent);

    // one-Product-to-many-Images
    Collection<ImagePojo> inputPojoImages = inputPojo.getImages();
    if (inputPojoImages != null) {
      List<ProductImage> resultImages = this.makeTransientProductImages(persistent, inputPojoImages);
      productImagesRepository.saveAll(resultImages);
      this.addImagesToPojo(resultImages, outputPojo);
    }

    // one-Product-to-one-ProductCategory
    ProductCategoryPojo inputCategory = inputPojo.getCategory();
    if (inputCategory != null) {
      Optional<ProductCategory> match = categoriesCrudService.getExisting(inputCategory);
      if (match.isPresent()) {
        ProductCategory existingCategory = match.get();
        persistent.setProductCategory(existingCategory);
        ProductCategoryPojo outputCategory = categoriesConverter.convertToPojo(existingCategory);
        outputPojo.setCategory(outputCategory);
      }
    }
    productsRepository.save(persistent);

    return outputPojo;
  }

  @Override
  public Optional<Product> getExisting(ProductPojo input)
      throws BadInputException {
    String barcode = input.getBarcode();
    if (barcode == null || barcode.isEmpty()) {
      throw new BadInputException("Invalid product barcode");
    } else {
      return productsRepository.findByBarcode(barcode);
    }
  }

  @Override
  protected ProductPojo persistEntityWithUpdatesFromPojo(ProductPojo changes, Product existingEntity)
      throws BadInputException {
    Product localChanges = dataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    Product persistent = productsRepository.saveAndFlush(localChanges);
    ProductPojo outputPojo = converter.convertToPojo(persistent);
    if (outputPojo == null) {
      throw new IllegalStateException("Conversion service returned null when requested to convert one " +
                                          "persisted Product to a ProductPojo");
    }

    // one-Product-to-many-Images
    productImagesRepository.deleteByProductId(persistent.getId());
    Collection<ImagePojo> inputPojoImages = changes.getImages();
    if (inputPojoImages != null) {
      List<ProductImage> resultImages = this.makeTransientProductImages(persistent, inputPojoImages);
      productImagesRepository.saveAll(resultImages);
      this.addImagesToPojo(resultImages, outputPojo);
    }

    // one-Product-to-one-ProductCategory
    persistent.setProductCategory(null);
    ProductCategoryPojo inputCategory = changes.getCategory();
    if (inputCategory != null) {
      Optional<ProductCategory> match = categoriesCrudService.getExisting(inputCategory);
      if (match.isPresent()) {
        ProductCategory existingCategory = match.get();
        persistent.setProductCategory(existingCategory);
        ProductCategoryPojo outputCategory = categoriesConverter.convertToPojo(existingCategory);
        outputPojo.setCategory(outputCategory);
      }
    }
    return outputPojo;
  }

  private void addImagesToPojo(List<ProductImage> resultImages, ProductPojo outputPojo) {
    Collection<ImagePojo> outputImages = new ArrayList<>();
    for (ProductImage productImage : resultImages) {
      ImagePojo imagePojo = imageConverter.convertToPojo(productImage.getImage());
      outputImages.add(imagePojo);
    }
    outputPojo.setImages(outputImages);
  }

  /**
   * Creates transient instances of the ProductImages entity (for the one-to-many relationship).
   * It does NOT persist these instances.
   * @param existingProduct The persisted entity
   * @param inputImages The list of images to link to the aforementioned Product
   * @return The list of ImagePojos with normalized metadata.
   */
  private List<ProductImage> makeTransientProductImages(Product existingProduct,
                                                        Collection<ImagePojo> inputImages) {
    List<ProductImage> allRelationships = new ArrayList<>();
    for (ImagePojo img : inputImages) {
      try {
        Optional<Image> match = imagesCrudService.getExisting(img);
        if (match.isPresent()) {
          Image existingImage = match.get();
          ProductImage relationship = new ProductImage(existingProduct, existingImage);
          allRelationships.add(relationship);
        }
      } catch (BadInputException ex) {
        logger.debug("An image was not linked to product with barcode '{}'", existingProduct.getBarcode());
      }
    }
    return allRelationships;
  }
}
