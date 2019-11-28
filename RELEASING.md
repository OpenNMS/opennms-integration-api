# Releasing

Update pom versions:
```
mvn versions:set -DnewVersion=0.1.1
```

Commit pom update:
```
mvn commit
```

Tag:
```
git tag -a v0.1.1
```

Deploy:
```
mvn -Prelease clean deploy
```

> The `release` profile enables GPG signing of the artifacts.

Update pom versions again:
```
mvn versions:set -DnewVersion=0.1.2-SNAPSHOT
```

Commit pom update:
```
mvn commit
```

Push commits and tags:
```
git push
git push --tags

```
