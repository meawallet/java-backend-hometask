                                                                           # Senior Java Developer test

##### Estimated execution time: 3h

### Software solution info

Solution enables sending card payment transactions from Android devices. Full software solution consists of 2 main parts :

- Mobile SDK (out of scope)
  -- Embeddable SDK which allows to Mobile Application make payment transactions
- Backend services -- Mobile SDK lifecycle management and transactions processing

Backend consists of several microservices that communicate via HTTP. Backend tech stack :

* Spring Boot Webflux
* Project Reactor
* R2DBC
* Liquibase

### Problem description

**Short : Implement authentication based on signed JWT.**

The test task is a lightweight version of a real task.

During the first development iteration, the back-end team implemented "happy path"
flows only without any security. This is acceptable for an isolated test environment, but not for the real world.

The Mobile SDK is embeddable into Mobile Application.

Mobile Application is something what belongs to our client "Merchant".

**We must accept SDK installations from known "Merchants" only.**

To identify known "Merchants" SDK will send signed JWT in every request.

#### Secured endpoints

All endpoints must be secured, except actuator : /health, /metrics, /prometheus

#### JWT structure

* Headers :
  * alg = must be RS256
  * kid = signing key fingerprint
* Claims :
* iss = merchant name
* iid = SDK instance identifier
* exp = expiration time
* iat = issuing time

Valid Signed JWT example :

```
eyJraWQiOiJBMDVBQkU2OEY1MzlDMTQ2MUM3MjA4QjBCQzI2QTFERDZENTZEQkI4QTNDQzU4NTkxOEUzOTY3RUQ2NUJGM0Y2IiwiYWxnIjoiUlMyNTYifQ
.eyJpc3MiOiJtZWFsYXVuY2giLCJpaWQiOiI3NzFFNzkyOTUzNUNBMERCMEE2M0Y0NTE5NEI4NDVDRCIsImlhdCI6MTYzMzU5ODIxNywiZXhwIjoxNjM2Mjc2NjE3fQ.lK2ZfIOIgJIkDXA-pmgfzsvVG2NurJlpiJ9C56PiO1A4xOW84ZwdwiivVFKw1vAPqYQUsWhfIxgVPM9QZp10wgnqM0ah3-ItDqcXR9yxRqRpYD669viJMnqF8rCpJZ6RIRqWvt1e1asTx8r0QLCAImzdZi9L7K3vBdKoBeIcHx_hiKg01ms1y0NFSGfv-bSYfVMrm9b54kDtJi7T-dCps2Eet8B5acSh6KLGmUR0I3DP6_IZ0RH9eQZ0ZSX7gcLdK_chTko2fXoJ7G6itI8RgFb46XbH20X4gKsR3IEG6sAVzAeEc_HQ3RXHFTH9namRDRXgXLPkh8mExUE5o_GRG4y_-HxlzOWWGfQ5K5j44KG8GyMG9g4bviYtL4IuIZKZtPbkJA_bQsb-Jl53U6nbg4wF3Qgx6f9S29iGTOIv_kNSmB9zABLrBObklbsQ7IrjqLowndmxQqxkv9wY-Gw0SWLMWlmSf_sbq9Pos9UaGKMHfuJX8cKe4IxoDkErE-3LDjEd4yzYvizsx1vOtoqPQebh8nzEzgocpmXrTLQdhmG34jKLMaPeGUw_JHIvLXey70FBS7InocHZikl2bt_vJQ1HQB8yknzORFlBusVKMa_OfNCdRtOd6rrEOZUN0x6NqkDlV_8JHHeYh_nvtGHAVAaOxhCsxVwrOldWP5xgfX0
```

#### JWT validation rules

* Signing key belongs to the Merchant
* JWT does not contain additional headers
* JWT does not contain additional claims
* JWT has been issued in the past
* JWT has not expired
* Signature is valid

#### JWT signing key storage

* Merchant may have many keys.
* Merchant and Key onboarding is out of scope. You have to prepare data in tests only.
* The JWT public keys must be stored in a database bound to the merchant name. It can be simple table :

```
- id
- merchant_name
- public_key
- kid
```

To solve the problem, you can do absolutely everything that you consider necessary :

* Code refactoring
* Adding new tables and columns in database
* Using other technologies (frameworks, caching, ...)
* ... whatever you want ...
