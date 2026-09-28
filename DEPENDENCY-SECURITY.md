# Dependency Security Evidence

## Analysis performed

- Ran `mvn dependency:tree -Dscope=test` to inspect the resolved application and test dependencies.
- Ran the Sonatype OSS Index Maven audit against all 89 resolved artifacts:
  `mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit`.
- Ran Maven Versions Plugin dependency-update analysis:
  `mvn versions:display-dependency-updates`.
- Added `.github/dependabot.yml` so GitHub can continuously monitor Maven dependencies and raise update/security pull requests.

## Results

The OSS Index audit completed with `BUILD SUCCESS`, but the service returned HTTP 401 while fetching component reports. Therefore, it did not produce a usable vulnerability result and no claim of “no vulnerabilities” is made from that run.

The versions analysis identified newer releases, including H2 `2.5.252` compared with the resolved `2.3.232`, and newer Spring Boot lines. These are update candidates, not confirmed security vulnerabilities. The project remains on the Spring Boot 3.5.6 managed dependency set because upgrading to a major or milestone release without a compatibility review would be unsafe.

No actionable vulnerability was confirmed by the available local analysis. Dependabot is now configured as the ongoing authoritative GitHub-based dependency review mechanism. No dependency was changed solely from an unverified update notice.
