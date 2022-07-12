# Releasing

Update pom versions:
```
mvn versions:set -DnewVersion=0.1.1
```

Commit pom update:
```
git commit
```

Tag:
```
git tag -u opennms@opennms.org -s v0.1.1
```

Deploy:
```
mvn -Prelease -Darguments=-Dgpg.keyname="opennms@opennms.org" -Dgpg.keyname="opennms@opennms.org" clean deploy
```

> The `release` profile enables GPG signing of the artifacts.

Update pom versions again:
```
mvn versions:set -DnewVersion=0.1.2-SNAPSHOT
```

Commit pom update:
```
git commit
```

Push commits and tags:
```
git push
git push --tags

```
