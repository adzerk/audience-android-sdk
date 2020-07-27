# Release process

This document describes the process for releasing and deploying a new sdk version.

## Deploying and releasing a new version

1. Make sure you're running on the `master` branch.
1. Update the `CHANGELOG.md` for the impending release.
1. Properly configure your Sonatype credentials, by adding to `~/.gradle/gradle.properties`.
    ```properties
    # Sonatype credentials
    ossrhUsername=<sonatype username>
    ossrhPassword=<sonatype password>
    # GPG credentials and location
    signing.keyId=<gpg public key ID>
    signing.password=<gpg public key password>
    signing.secretKeyRingFile=<path-to-gpg-secring>/.gnupg/secring.gpg
    ```
1. Execute `./gradlew :velocidi-sdk:release`.
1. Enter the new version for the SDK. By default it increments the patch version.
1. Execute `git push && git push --tags`.    `
1. Visit [Sonatype Nexus](https://oss.sonatype.org/#stagingRepositories) and verify the release was created. If everything is in order, `Close` it and `Release` it.
1. Deploy documentation website by running `cd docs && ./deploy.sh`.
1. Add the changes in `CHANGELOG.md` to the [release description on GitHub](https://github.com/velocidi/velocidi-android-sdk/releases).
