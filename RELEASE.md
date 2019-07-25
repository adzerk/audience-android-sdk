# Release process

This document describes the process for releasing and deploying a new sdk version.

## Deploying and releasing a new version

1. Make sure you're running on the `master` branch.
1. Update the `CHANGELOG.md` for the impending release.
1. Properly configure your Sonatype credentials.
1. Execute `./gradlew :velocidi-sdk:release`.
1. Enter the new version for the SDK. By default it increments the patch version.
1. Execute `git push && git push --tags`.    `
1. Visit [Sonatype Nexus](https://oss.sonatype.org/) and verify if everything is as expected.
1. Deploy documentation website by running `sh docs/deploy/`.
