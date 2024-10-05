# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased

### Conforming to API v3

  - Follow new specification for error messages
    - Introduce new model class `AppError`
    - Make the `ExceptionsControllerAdvice` use this new class on all its methods
  - Rename all references of the word `Sell` and `Sales` to `Order` and `Orders` respectively
    - REST API endpoints
      - `/data/sales` -> `/data/orders`
      - `/data/sell_statuses` -> `/data/order_statuses`
    - Hardcoded Spring Security authorities
      - `sales:(create|read|update|delete)` -> `orders:(...)`
      - `sell_statuses:(create|read|update|delete)` -> `order_statuses:(...)`
    - SQL schema tables, columns, constraints, indexes and initial values
      - Entity table mappings included as well
    - Custom properties
      - `trebol.api.able-to-edit-sales-after-being-processed`
        - to `trebol.api.able-to-edit-orders-after-being-processed`
    - Class names, by package
      - `org.trebol.api.`
        - `models.`
          - `SellPojo` -> `OrderPojo`
          - `SellDetailPojo` -> `OrderDetailPojo`
          - `SellStatusPojo` -> `OrderStatusPojo`
        - `services.` (for both interfaces and implementations)
          - `SalesProcessService` -> `OrdersProcessService`
      - `org.trebol.jpa.`
        - `entities.` (and their QueryDSL counterparts)
          - `Sell` -> `Order`
          - `SellDetail` -> `OrderDetail`
          - `SellStatus` -> `OrderStatus`
        - `repositories.`
          - `SalesRepository` -> `OrdersRepository`
          - `SellDetailsRepository` -> `OrderDetailsRepository`
          - `SellStatusesRepository` -> `OrderStatusesRepository`
        - `sortspecs.`
          - `SalesSortSpec` -> `OrdersSortSpec`
          - `SellStatusesSortSpec` -> `OrderStatusesSortSpec`
        - `services.` (for both interfaces and implementations)
          - `crud.`
            - `SalesCrudService` -> `OrdersCrudService`
            - `SellStatusesCrudService` -> `OrderStatusesCrudService`
          - `patch.`
            - `SalesPatchService` -> `OrdersPatchService`
            - `SellStatusesPatchService` -> `OrderStatusesPatchService`
          - `conversion.`
            - `SalesConverterService` -> `OrdersConverterService`
            - `SellStatusesConverterService` -> `OrderStatusesConverterService`
          - `predicates.`
            - `SalesPredicateService` -> `OrdersPredicateService`
            - `SellStatusesPredicateService` -> `OrderStatusesPredicateService`
    - And all other constant and variable names that reference them
  - remove `CustomerPojo` and `SalespersonPojo` classes
    - the `OrderPojo` class now relates to two instances of `PersonPojo` instead
  - Change status codes for most `/data` endpoints
    - All POST requests return 201, except for three specific paths
      - `/data/orders/confirmation`
      - `/data/orders/rejection`
      - `/data/orders/completion`
    - All PUT,PATCH,DELETE,OPTIONS requests return 204
  - Remove duplicated functionality endpoint `/public/checkout/result/{token}`
    - Served the same purpose as `/public/receipt/{token}`

## [v0.2.4] - 2024-28-08

### Changed

- **BREAKING CHANGE:** Updated supported Java version to [17 LTS](https://openjdk.org/projects/jdk/17/)
  (although the folks at [whichjdk.com](https://whichjdk.com/#tldr) recommend migrating to 21, this is the best choice
  compatibility-wise, also supported by SonarQube static analysis)
  - Artifact Id was given suffix `-java17` to reflect this change in base Java version
- Updated project dependencies
  - spring boot starter parent pom `2.7.18` -> `3.2.9`
  - lombok `1.18.26` -> `1.18.34` (version inherited from `spring-boot-dependencies`)
  - h2 database `2.1.214` -> `2.1.220`
- Many minor changes required to keep things running under these new dependencies
  - Most `javax`-prefixed libraries now use the `jakarta`-prefix instead (with the exception of `javax.crypto`)
    - `querydsl` also needed to be aware of this as a "classifier"
  - Migrate `SecurityConfig` following the official guides by Spring Security
    ([1](https://docs.spring.io/spring-security/reference/5.8/migration/index.html))
    ([2](https://docs.spring.io/spring-security/reference/6.0/migration/index.html))
    ([3](https://docs.spring.io/spring-security/reference/6.2/migration/index.html))
    - Drop `WebSecurityConfigurerAdapter` interface
      - Instead of implement `configure` method; declare a bean of type `SecurityFilterChain`
    - Include `AuthenticationManager` and `DaoAuthenticationProvider` beans
    - Configure `HttpSecurity` with newer arrow function-based syntax

## [v0.2.2] - 2023-06-09

### Changed

- Update GitHub issue templates, contribution guidelines and reformat `*.md` files
- Removed properties in `pom.xml` that were only used once
- Updated project dependencies
  - lombok `1.18.24` -> `1.18.26`
  - h2 database `2.1.212` -> `2.1.214`

### Fixed

- Missing validation for request bodies of `PUT` methods in `/data/*` controllers

## [v0.2.1] - 2023-06-06

### Changed

- Project-wide clean up (re-indentation & import optimization)
  - New indentation rules set in `.editorconfig`

### Fixed

- Raise an exception when no payment type is specified
- Unintended errors during checkout
  - Description of each sell detail would not be included
  - New sales would not have an initial status or registered payment type
  - Billing address was required for non-enterprise customers>

## [v0.2.0] - 2023-05-26

### Added

- Support for partial updates using `PATCH` request in these endpoints
  - `/data/products`
  - `/data/sales`
  - `/data/users`
  - `/data/images`
  - `/data/product_categories`
  - `/data/shippers`
  - `/data/product_lists`

### Changed

- Update `CrudService` signature methods
  - `update` - now meant as a method to fully update a data registry or item
  - `partialUpdate` - new method; meant to update part(s) of a data registry or item (former behavior of the `update`
    method)
- Update `PatchService` signature methods
  - `patchExistingEntity` method relies on a `Map<String, Object>` as first parameter, instead of a `<P>` object
    - The previous method signature has been deprecated in favor of this one
- All `@Entity` classes implement a new `DBEntity` interface
  - This interface declares a getter & setter pair for an `id` field. Most entities already had one anyway.
  - This is made to easily integrate the `partialUpdate` method with database numeric primary keys, represented by
    the `findById` repositories
- Update base `CrudGenericService` implementation
  - `partialUpdate` method is implemented, being fully-aware of the `DBEntity` interface to pass the `id` to an existing
    target entity
  - `create` and `update` methods now share extremely similar logic
    - Their only difference is that the `update` method syncs the entity `id` before being saved and flushed
  - `prepareEntityWithUpdatesFromPojo` renamed to `flushPartialChanges`
  - Remove method `prepareNewEntityFromInputPojo`
    - Instead of overriding this method, implementations may directly override its containing `create` method
  - Remove method `persist`
    - This two-line method started making things a bit confusing... (!?)
    - (note to self: never excuse a two-line method to duplicate code refactoring)
- Scope and flows of data have changed
  - Entity classes' copy constructors are very often used, so now these never care for entity relationships
    - EXCEPT when it is explicitly stated otherwise! These exceptions were made (from-to):
      - `Customer` -> `Person`
      - `Salesperson` -> `Person`
      - `Sell` -> `SellStatus`
      - `Sell` -> `PaymentType`
      - `Sell` -> `BillingType`
      - `User` -> `UserRole`
  - `ConverterService`s now take care of one-to-one entity relationships at most
  - `CrudService`s now take care of all entity relationships not covered by other cases
  - Pojo classes with `id` fields have been stripped of them; only entities hold `id` fields now

### Fixed

- Calls to `/checkout` using an invalid sell will output a `400` error (bad input from user) instead of `500` (bad state
  of the server)
- Add a check in two `GenericCrudService` methods, `partialUpdate` and `update`, both overloaded with a
  QueryDSL `Predicate` as a second method argument
  - Validate that said `Predicate` only ever matches one record
  - This behavior *could* change in the future, but for now it is a deliberate choice to avoid them affecting more than
    one registry at a time

## [v0.1.1] - 2023-03-23

### Added

- Stock JUnit run configuration for IntelliJ

### Tests

- Raised coverage percentage from `51.4%` to `73.4%`
  - Now excluding API models and JPA entities
  - Added tests for all remaining security components as well as the Webpay Plus integration service

## [v0.1.0] - 2023-02-17

This is the first tagged version that is filed under the new project artifact,
`trebol-backend-monolith`.

### Added

- Issue and Pull Request templates
- [.editorconfig](https://editorconfig.org) file
  - Enforces Unix-style line endings (LF)
- `PhoneNumber` annotation for validating phone numbers - **Thank you `@mepox`**
  - Use custom `PhoneNumberValidator`, subclass of `javax.validation.ValidationConstraint`, which validates using
    regular expression
  - Configure the regular expression through `trebol.validation.phonenumber-regexp` in `application.properties`
- Account protection mechanism by `user_id` - **Thank you `@mepox`**
  - Any attempt to delete one certain user account will be cancelled
  - The protected user account must have the same `id` indicated by `trebol.security.protected-account-id`
    in `application.properties`
- Spring Security Test dependency for integration tests that deal with Spring Security mechanisms

### Changed

- [WIP] **BREAKING CHANGE** Split logic in services that implement `ICrudJpaService` (now `CrudService`) and
  extend `GenericCrudJpaService` (now `CrudGenericService`)
  - Introduce `PatchService<P, E>` to keep specific domain-dependant boilerplate code for updating entities before they
    are submitted to database
    - This new, separate interface inherits the `applyChangesToExistingEntity` method
      from `ITwoWayConverterJpaService` (now `ConverterService`)
    - The original method in `ITwoWayConverterJpaService` (now `ConverterService`) has been deprecated
  - Break down the steps taken on each method in the public API of `GenericCrudJpaService` (now `CrudGenericService`)
    and its sub-implementations, by introducing these overridable `protected` methods
    - `prepareEntityWithUpdatesFromPojo`
    - `validateInputPojoBeforeCreation`
    - `prepareNewEntityFromInputPojo`
  - Introduce sub-interfaces from `ICrudJpaService` (now `CrudService`), `PatchService`
    and `ITwoWayConverterJpaService` (now `ConverterService`)
    - This increases the accuracy of type-safe dependency injection. Specially important for mocks and unit testing.
- UsersConverterJpaServiceImpl - refactor `convertToNewEntity` since it tag as cyclomatic issue
- SalesConverterJpaServiceImpl, SalesProcessServiceImpl - add string constants
- **BREAKING CHANGE**: Rename table names to follow the naming convention - **Thank you `@mepox`**
  - `products_categories`          -> `product_categories`
  - `products_images`              -> `product_images`
  - `sales_statuses`               -> `sell_statuses`
  - `app_users_roles`              -> `app_user_roles`
  - `app_users_roles_permissions`  -> `app_user_role_permissions`
- Update GitHub Actions workflow
  - `actions/checkout`      | `v2 -> `v3`
  - `actions/setup-java`    | `v1 -> `v3`
    - Following [Switching to V2](https://github.com/actions/setup-java/blob/v3.6.0/docs/switching-to-v2.md) guide
    - Chose `temurin` distro, maintained by the Eclipse foundation
  - `actions/cache`         | `v1 -> `v3`
- Update to the latest Spring Boot patch (as of Oct 12th, 2022)
  - `spring-boot-starter-parent` - `2.6.4` to `2.6.12`
- Take advantage of Project Lombok annotations for entity and model classes
- Use entrySet in place of keySet for better iteration in CorsConfigurationSourceBuilder
- Enforce a better naming scheme for classes and methods
  - No interface names with capital initial `I`
  - Class and method names should explain roles and purposes from the get-go
  - It is preferable to avoid class names over 50 characters long
  - Test classes must always be suffixed with `Test`
  - Test methods should be named using `underscores_and_lowercase`
  - Spring component class names should be suffixed with their respective base type
    name (`Service`, `Controller`, `Config`, `Repository`)

### Removed

- REST Assured dependency, as the Spring Framework provides an easy and working `MockMvc`

### Tests

- ProductListJpaCrudServiceImpl - add unit tests
- ReceiptServiceImpl - add unit tests
- SalesProcessServiceImpl - add unit tests
- ProfileServiceImpl - add unit tests
- RegistrationServiceImpl - add unit tests
- Refactor PhoneNumberValidatorTest - resolve code smell tag by sonarqube and @ParameterizedTest is the solution
- Additional entry in TestConstants `int 1`
- CheckoutServiceImpl - add more coverage for method "confirmTransaction, generatePageUrl"
- CompanyServiceImpl - validate the proper mapping behaviour of method "readDetails"
- Add `mockito-inline` dependency to create inline mocks - **Thank you `@NyorJa`**
  - Useful to mock final classes and methods, also for having constant regex
- Add TestConstants to hold some static final values for use with mocks - **Thank you `@NyorJa`**
- Introduce unit tests for 15 converter services - **Thank you `@NyorJa`**
  - `BillingCompaniesConverterJpaServiceImpl`
  - `BillingTypesConverterJpaServiceImpl`
  - `CustomersConverterJpaServiceImpl`
  - `ImagesConverterJpaServiceImpl`
  - `PeopleConverterJpaServiceImpl`
  - `ProductCategoriesConverterJpaServiceImpl`
  - `ProductListsConverterJpaServiceImpl`
  - `ProductsConverterJpaServiceImplTest`
  - `SalesStatusesConverterJpaServiceImplTest`
  - `SellStatusesConverterJpaServiceImplTest`
  - `ShippersConverterJpaServiceImplTest`
  - `UserRolesConverterJpaServiceImplTest`
  - `UsersConverterJpaServiceImplTest`
- Refactor out test boilerplate using `@InjectMocks`
- Introduce Spring Security integration tests for custom Filters
  - `JwtGuestAuthenticationFilter`
  - `JwtLoginAuthenticationFilter`

### Fixed

- Using a better fitted java11 method of String to check for emptiness - **Thank you `@NyorJa`**

# OLD VERSION NUMBERING

From here and below, the project used to have another artifact name and version.
It was reset because the author didn't feel that the software was very polished,
so in turn, tagging versions as `v3.0` and onwards felt misleading.

## [v3.0.0.rc-1] - 2022-08-18

### Added

- Support sorting product lists by item count (the amount of products held on each)
- Properties to configure the guest user (a public account only enabled for checking out)
  - `trebol.security.guestUserEnabled` - Can be true or false
  - `trebol.security.guestUserName` - Any non-blank string; also acts as its password
- Property to configure max allowed nested depth while fetching categories
- *BREAKING CHANGE*: `SellDetail` entity now has a `description` field, which is meant to describe and summarize the
  detail in a human-readable format
- When fetching data, filters by category may include descendant of lower level categories
  - For example, assumming that category A includes subcategories AB and AC, filtering 'by category A' may include
    children from subcategories AB and AC as well
  - This applies for filtering categories and products
  - Behavior before was to fetch only direct descendants of a given category
- Implement three API resources to interact with sales after a checkout process from the customer
  - `/data/sales/confirmation` - To confirm the sell (when logistics is good to go)
  - `/data/sales/rejection` - To reject the sell (for cases such as, when no stock is available)
  - `/data/sales/completion` - To complete the sell, or otherwise mark it as delivered
- Introduce a service interface for sending notification mails to customers and owners alike during checkout steps
  - Include Mailgun HTTP API implementation
    - To use it, the `mailgun` spring profile should be active
    - An empty configuration file is provided

### Changed

- Use [Project Lombok](https://projectlombok.org)
- The transaction token for the (frontend) checkout result page is passed through query param instead of path param
- Default `GET /data/sales` sort order is by descending `buyOrder`
- (Temporary) Disable regex pattern validation for phone numbers
- Further divide logic for sorting/pagination/filtering into different services
- Bump dependencies
  - `spring-boot-starter-parent` from `2.6.2` to `2.6.4`
  - `h2database` from `inherited` to `2.1.212`
  - `jjwt-*` from `0.11.2` to `0.11.5`
  - `unirest-java` from `3.13.6` to `3.13.8`

- Bump plugins
  - `jacoco-maven-plugin` from `0.8.5` to `0.8.8`

### Fixed

- Incorrect amount value given to Webpay (net value instead of total value)
- Two very important equations when calculating sell totals
- Incorrect protocol for MariaDB JDBC URL
- Webpay Plus was unable to redirect users to the callback URL; added `null` in list of allowed CORS origins
- Incongruent binding of query parameters to data search filters for products
- Guest can call the `/access` API and be returned a `200 OK` response
- Support for fetching an individual sell data (details included) when requested with `buyOrder` query param
- Change integration type for payments with Webpay Plus when production mode is not enabled
  - Now uses `TEST` as recommended by `@TransbankDevelopers`
  - Was using `MOCK` which is not very well documented

## [v2.0.0] - 2022-01-20

### Changed

- Reformat `pom.xml` file
  - Add comments
  - Update properties
    - Add keys for code coverage ratio
    - Add keys to version of all dependencies and plugins (except those inferred from parent)
    - Rename some properties
  - Remove unused `jcenter` repository
  - Bump plugins
    - `maven-compiler-plugin` - from `3.1` to `3.9.0`
    - `maven-war-plugin` - from `3.3.1` to `3.3.2`
  - Remove old `Oracle Java EE endorsed API` artifacts
    - In reality, these have been replaced by Jakarta EE endorsed API
- **Bump Spring Boot Starter POM to v2.6.2 [BREAKING CHANGE]**
  - Deprecated/unsupported properties; update configuration described in `application.properties`
- Update license file; add headers to all source files
- Creating products through a `POST /data/products` call does not cascade creation of other entities

### Removed

- Clean up deprecated API resources
  - `/public/categories`
  - `/public/products`
  - `/public/products/{barcode}`
- Remove any reference to the depreacted `amount` property in the Receipt entity

## [v1.2.0] - 2022-01-07

### Added

- CRUD operations support for `Product List`
  - Also for managing contents of individual lists, as described by resource `/data/product_list_contents` in the API
- Schema SQL script for MariaDB
- Maximum page size limit, parametrizable through `properties` files

### Changed

- Updated security rule for `GET /data/products` to: do not require any authority
- Simplified method signature for `readMany` method in `IDataController`
- Updated BD schema diagram `schema.png`

### Removed

- Clean up deprecated resources
  - `/data/customers/{idNumber}`
  - `/data/images/{code}`
  - `/data/product_categories/{parentCode}`
  - `/data/products/{barcode}`
  - `/data/sales/{buyOrder}`
  - `/data/salespeople/{idNumber}`
  - `/data/users/{name}`
  - `/public/categories/{parentId}`

### Deprecated

- Paths to be superseded
  - `/public/products`
  - `/public/products/{barcode}`

## [v1.1.2] - 2021-12-27

### Added

- Support for sorting data through query parameters

### Fixed

- Unable to update products due to possible null pointer in description
- CORS mappings for `/access` API

### Tests

- Added unit tests for predicate services (responsible for parsing query params to filtering conditions)
- Improved quality of some unit tests

## [v1.1.1.1] - 2021-12-08

### Fixed

- Issues during checkout:
  - Incorrect validation API endpoints for integrating Webpay Plus SDK
  - JPA transactions failing between service calls
  - Data missing in receipt after checkout is complete

## [v1.1.1] - 2021-12-06

### Added

- Using [SonarCloud](https://sonarcloud.io) to automate project building and code analyzing
- Include maintainability and reliability badges, generated by SonarCloud, in `README.md`
- Support for query parameters in PUT requests to `/data` resources
- Unit tests providing an approximate of 20% codebase coverage
- Database schema file `/schema.png`

### Changed

- Removed request authentication for remapped paths in `/data/product_categories/*` to make them publicly available to
  anonymous users
- Updated `Person` schema by splitting `name` property into two properties `firstName` and `lastName`
- Updated `Sell`  schema by adding properties `taxValue`, `transportValue`, `totalValue` and `totalItems`
- Updated `Receipt` schema by adding properties `token`, `taxValue`, `transportValue`, `totalValue` and `totalItems`
- Updated `SellDetail` and `ReceiptDetail` schemas by adding property `unitValue` in both
- Updated `ProductCategory` schema by changing data type of `code` from `integer` to `string`
- Delegate API authorized access requirements to each REST controller method, instead of using HttpSecurity-centric
  configuration
- Improvements on JPA entities
  - `BillingCompany` - added unique constraint on `name`
  - `BillingType` - added unique constraint on `name`
  - `Image` - added unique constraint on `code`, `filename` and `url`
  - `Param` - added unique constraint that uses both `category` and `name`
  - `PaymentType` - added unique constraint on `name`
  - `Permission` - added unique constraint on `code`
  - `Person` - added index to `firstName` and `lastName`
  - `Product` - added index to `name`
  - `Sell` - added index to `date` and `transactionToken`
  - `SellStatus` - added unique constraint on `code` and `name`
- Refactored services in `org.trebol.jpa` package for better separation of concerns

### Deprecated

- Path `/public/categories` and `/public/categories/{parentId}`, mapped under `PublicCategoriesController` - Thanks
  @ParundeepSingh
- Property `amount` of `Sell` and their getters; must use `totalValue` instead
- Parameterized paths
  - `/data/customers/{idNumber}` replaced by `/data/customers?idNumber={}`
  - `/data/images/{code}` replaced by `/data/images?code={}`
  - `/data/products/{code}` replaced by `/data/products?barcode={}`
  - `/data/sales/{buyOrder}` replaced by `/data/sales?buyOrder={}`
  - `/data/salespeople/{idNumber}` replaced by `/data/salespeople?idNumber={}`
  - `/data/users/{name}` replaced by `/data/users?name={}`

### Removed

- Entity class `Session`, not used in the codebase

## [v1.0.4] - 2021-10-21

### Changed

- Clean up entities and remove redundant annotation values - Thanks to @trangntt-016

### Fix

- Fix resolving paging parameters where it would always get the default value.

### Security

- Restrict users from deleting their own account - Thanks to @trangntt-016

## [v1.0.3] - 2021-09-23

### Changed

- `/public/receipt/{id}` updated with `id` parameter now called `token` and referencing sell payment token

### Fixed

- Fix default application parameters related to the checkout/payment service
  - Self-callback URL after clients navigates checkout
  - Success/Failure page URL to send customers to, after checkout
- Add missing sales filters required to fetch data during the checkout flow
- Add missing mock data into `data.sql` that is needed to invoke `/public/about`
- `ReceiptPojo` now uses `Instant` to represent dates, just like `Sell` and `SellPojo`

## [v1.0.0] - 2021-09-20

First public version.
