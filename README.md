# `EnvAlloc`

[Mindustry](https://github.com/Anuken/Mindustry) Java mod library for creating new `Env` flags.

## Usage

Add `env-alloc` to mod dependencies, and follow the steps below according to your mod's programming language.

### Java

1. Add `com.github.GglLfr:EnvAlloc:v0.1` to Gradle `compileOnly` dependency configuration.
2. Add `maven("https://raw.githubusercontent.com/GglLfr/EnvAllocMaven/main")` to Gradle repositories.
3. ```java
   import env.*;

   // In `loadContent()`:
   int myEnvFlag = EnvAlloc.create("my-mod-name-env-flag");
   ```

### JS

```js
const env = require("env-alloc/library");
const myEnvFlag = env.create("my-mod-name-env-flag");
```

### JSON

Unsupported.

## License

The project is licensed under [GNU GPL v3](/LICENSE).
