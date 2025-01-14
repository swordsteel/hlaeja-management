# Hlæja Management

In realms of connectedness, where devices roam free, A nexus of management, harmony to decree. Each gadget configured, with precision and care, Their settings aligned, with purpose to share. From networks to endpoints, unity is key, Administrators and users, their access to see. The Device Dominion, a hub of control, A registry of configurations, where order makes whole.

## Properties for deployment

| name                   | required | info                    |
|------------------------|:--------:|-------------------------|
| spring.profiles.active | &check;  | Spring Boot environment |

*Required: &check; can be stored as text, and &cross; need to be stored as secret.*

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Information

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
