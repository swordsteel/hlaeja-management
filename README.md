# Hlæja Management

In realms of connectedness, where devices roam free, A nexus of management, harmony to decree. Each gadget configured, with precision and care, Their settings aligned, with purpose to share. From networks to endpoints, unity is key, Administrators and users, their access to see. The Device Dominion, a hub of control, A registry of configurations, where order makes whole.

## Properties for deployment

| name                            | required | info                       |
|---------------------------------|:--------:|----------------------------|
| spring.profiles.active          | &cross;  | Spring Boot environment    |
| spring.session.timeout          |          | Session timeout            |
| spring.data.redis.database      |          | Redis database             |
| spring.data.redis.port          |          | Redis port                 |
| spring.data.redis.host          | &#x2714; | Redis host                 |
| spring.data.redis.password      | &#10033; | Redis password             |
| server.error.include-exception  |          | Exception class on error   |
| server.error.include-message    |          | Exception message on error |
| server.error.include-stacktrace |          | stack trace on error       |
| server.port                     |          | HTTP port                  |
| server.ssl.enabled              |          | HTTP Enable SSL            |
| server.ssl.key-store            | &cross;  | HTTP Keystore              |
| server.ssl.key-store-type       |          | HTTP Cert Type             |
| server.ssl.key-store-password   | &#10033; | HTTP Cert Pass             |
| jwt.public-key                  | &cross;  | JWT public key file        |
| account-registry.url            | &#x2714; | Account Register URL       |
| device-registry.url             | &#x2714; | Device Register URL        |

*Required:*
- *&check; can be stored as text. &#x2714; predefined in environment*
- *&cross; mounted file.*
- *&#10033; need to be stored as secret.*

## Releasing Service

Run release pipeline from `master` branch.

## Development Information

### Public RSA Key

This service uses the public key from **[Hlæja Account Register](https://github.com/swordsteel/hlaeja-account-registry)** to identify users. To set up user identification for local development, copy the `public_key.pem` file from the `./cert` directory in **Hlæja Account Register** into the `./cert` directory of this project.

*Note: For more information on generating RSA keys, please refer to our [generate RSA key](https://github.com/swordsteel/hlaeja-development/blob/master/doc/rsa_key.md) documentation.*

### Global Setting

The following global settings are used in Hlaeja Device Registry. You can configure these settings using either Gradle properties or alternatively environment variables. 

*For instructions on how to set these up, please refer to our [set global settings](https://github.com/swordsteel/hlaeja-development/blob/master/doc/global_settings.md) documentation.*

#### Gradle Properties

```properties
repository.user=your_user
repository.token=your_token_value
```

#### Environment Variables

```properties
REPOSITORY_USER=your_user
REPOSITORY_TOKEN=your_token_value
```
