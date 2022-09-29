

<h1 align="center"><img src="docs/images/logo-woocommerce.svg" width="300"><br>for Android</h1>

<p align="center">A Jetpack-powered mobile app for WooCommerce.</p>



## 🎉 Setup Instructions


1. Generate the developer oauth2 tokens. These values get copied into the main `gradle.properties` file in the next step. See the [OAuth2 Authentication](docs/project-overview.md#oauth2-authentication) section for details.
1. Generate the `gradle.properties` file for this app:

    ```bash
    $ cp ./gradle.properties-example ./gradle.properties
    ```

1. Open and modify the newly created `gradle.properties` files. See the [Configuration Files](docs/project-overview.md#configuration-files) section for a breakdown of the properties.
1. In Android Studio, open the project from the local repository. This will auto-generate `local.properties` with the SDK location.
1. Optional: Go to Tools → Device Manager and create an emulated device.
1. Run. (Creates a default virtual device if you skipped the previous step)

## Build & Test

To build, install, and test the project from the command line:

```bash
$ ./gradlew assembleVanillaDebug                          # assemble the debug .apk
$ ./gradlew installVanillaDebug                           # install the debug apk if you have an
                                                          # emulator or a device connected
$ ./gradlew :WooCommerce:testVanillaDebugUnitTest         # assemble, install and run unit tests
$ ./gradlew :WooCommerce:connectedVanillaDebugAndroidTest # assemble, install and run Android tests
```
