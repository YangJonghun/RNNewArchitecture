# React Native의 새로운 아키텍쳐를 적용해보는 Template입니다 (0.68.2기준)

- Typescript
- Kotlin

을 사용하며
https://github.com/react-native-community/react-native-template-typescript/tree/6.10.3
로 시작합니다

## 들어가기에 앞서

- 기존 React Native 네이티브 모듈/네이티브 뷰의 작성법을 알고있다는 가정하에 문서가 작성되었습니다.
- README보다 커밋 히스토리를 보시는 걸 추천드립니다
- 현 시점 기준 최신 타입정의 모듈인 `@types/react-native@0.67.7`에선 codegen에 필요한 타입이 제공되지 않기 때문에 이 저장소에선 patch를 이용해 타입을 선언 해두었습니다.

## 1. 공통

- `Native<MODULE_NAME>.{js,ts}`
- `<ComponentName>NativeComponent.{js,ts}`

위 패턴으로 파일을 만들어주고 네이티브 모듈/컴포넌트를 작성해줍니다. (codegen은 flow나 Typescript만 지원합니다)
각 타입은 다른언어에서 사용될 수 있도록 [타입 정의 파일 링크](https://github.com/facebook/react-native/blob/main/Libraries/Types/CodegenTypes.js)를 참조해 작성해줍니다.
Typescript파일은 제공되지 않기 때문에 타입이 깨질 수 있어 같은 형태의 추가 타입을 작성해 사용하시길 추천드립니다.

> 사실 위처럼 작성하라고 하지만 현재 stable기준에선 딱히 상관 없습니다.  
> `Native<ComponentName>.{js,ts}` 이나 `<MODULE_NAME>NativeComponent.{js,ts}` 으로 작성해도 잘 동작하지만  
> 언제 codegen 설정이 바뀔지 모르니 시키는대로 위처럼 합시다.

## 2. Android

- `./android/app/build.gradle`

```
project.ext.react = [
    enableHermes: true,  // clean and rebuild if changing
]
```

- `./android/gradle.properties`

```
- newArchEnabled=false
+ newArchEnabled=true
```

로 변경해 ReactNative의 hermes를 켜고 새로운 아키텍쳐를 허용해줍니다.(gradle을 별도로 수정한게 아니면 기본적으로 fabric과 turbomodule이 한번에 사용할 수 있게 처리됩니다.)

### Android codegen사용법

1. `./android/app/build.gradle`에 다음과 같이 코드를 넣고

```
apply from: "../../node_modules/react-native/react.gradle"
+
+ if (isNewArchitectureEnabled()) {
+     apply plugin: "com.facebook.react"
+     react {
+         root = rootProject.file('..')
+         libraryName = "newarchsample" // c++에서 사용할 모듈명을 결정합니다 (지정하지 않을 경우 default값인 "AppSpec"으로 지정됩니다)
+         jsRootDir = rootProject.file("../src/new-module/") // codegen시킬 js/ts파일이 담긴 경로 - subpath도 모두 읽습니다
+         codegenJavaPackageName = "com.newrn.codegen" // codegen 결과물의 java package 명 - import할때 저기서 읽으면 됩니다
+         reactNativeDir = rootProject.file("../node_modules/react-native/")
+         codegenDir = rootProject.file("../node_modules/react-native-codegen/")
+     }
+ }
```

> 참조) 공식문서에선 현재 if블록 설정이 없지만 새로운 아키텍쳐를 껐다 켰다 할 때의 편의를 위해 추가했습니다

> 안드로이드의 위 옵션 설정방법은 변경될 가능성이 크게 높으니 참조하시길 바랍니다. (아마 iOS처럼 pakcage.json에서 설정하는것으로 통합될 가능성이 큽니다. - [참조 링크](https://github.com/facebook/react-native/blob/main/packages/react-native-gradle-plugin/src/main/kotlin/com/facebook/react/tasks/GenerateCodegenArtifactsTask.kt))

2. `./android/app/src/main/jni/Android.mk`codegen으로 생성된 코드들을 c++에서 읽을 수 있도록에서 다음 주석들을 해제해줍니다.

```
- # include $(GENERATED_SRC_DIR)/codegen/jni/Android.mk
+ include $(GENERATED_SRC_DIR)/codegen/jni/Android.mk
...
- # LOCAL_C_INCLUDES += $(GENERATED_SRC_DIR)/codegen/jni
- # LOCAL_SRC_FILES += $(wildcard $(GENERATED_SRC_DIR)/codegen/jni/*.cpp)
- # LOCAL_EXPORT_C_INCLUDES += $(GENERATED_SRC_DIR)/codegen/jni
+ LOCAL_C_INCLUDES += $(GENERATED_SRC_DIR)/codegen/jni
+ LOCAL_SRC_FILES += $(wildcard $(GENERATED_SRC_DIR)/codegen/jni/*.cpp)
+ LOCAL_EXPORT_C_INCLUDES += $(GENERATED_SRC_DIR)/codegen/jni
```

3. optional) kotlin으로 모듈과 뷰를 작성할 경우

```
    applicationVariants.all { variant ->
        // ... 생략 ... //
    }
+
+     sourceSets.main {
+         java {
+             if (isNewArchitectureEnabled()) {
+                 srcDirs += [
+                     "build/generated/source/codegen/java"
+                 ]
+             }
+         }
+     }
}
```

를 추가해서 kotlin에서 codegen경로를 읽을 수 있게 합시다.

> [링크](https://github.com/facebook/react-native/blob/main/packages/react-native-gradle-plugin/src/main/kotlin/com/facebook/react/ReactPlugin.kt#L144)를 보면 kotlin에서 일읅 수 있어야하는데 안되네요. 아시는 분 있으시면 알려주시면 고맙겠습니다.

4. (optional) codegen만 실행

`yarn android`로 돌려도 실행 중 동작하지만 불필요한 동작이 추가되어 시간이 소요되고 우리는 아직 generate된 코드의 로직을 구현한게 아니기 때문에 앱은 제대로 구동되지 않습니다. 따라서 아래 명령어가 더 빠르고 간편합니다.

```
cd android && ./gradlew clean generateCodegenArtifactsFromSchema
```

위처럼 clean을 먼저 실행하는 이유는 이전에 성공한 codegen이 있으면 바뀐 js/ts의 코드대로 동작하지 않기 때문입니다. (혹은 gradle.properties에서 android.enableBuildCache=false를 설정해서 캐시를 잡지 못하게 하세요.)

위 명령어가 성공하면
`./android/app/build/generated/source/codegen/java`에 아래와 같은 경로들이 생깁니다.

- `<패키지 경로>` : 구현할 모듈의 추상클래스가 생성되는 path - 네이티브 모듈이 있다면 생성됩니다.
- `<패키지 경로의 첫번째 최상단 directory>.facebook.react.viewmanagers` : 구현할 뷰의 ViewManagerDelegate클래스, ViewManagerInterface - 네이티브 뷰가 있다면 생성됩니다.

5. c++파일들에서 codegen된 파일들을 읽을 수 있게 추가해줍니다.

> 참조: 아래에 적히는 `newarchsample`은 아까 위에서 정한 app/build.gradle의 libraryName의 예시입니다 / 지정하지 않았다면 "AppSpec"으로 지정됩니다

- `./android/app/src/main/jni/Android.mk`

```
...
- libyoga
+ libyoga \
+ react_codegen_newarchsample # react_codegen_<아까 지정한 libraryName>
...
```

- `./android/app/src/main/jni/MainApplicationModuleProvider.cpp`

```
#include <rncore.h>
+ #include <newarchsample.h>  // <아까 지정한 libraryName>_ModuleProvider
...
-  // auto module = samplelibrary_ModuleProvider(moduleName, params);
-  // if (module != nullptr) {
-  //    return module;
-  // }
+  auto module = newarchsample_ModuleProvider(moduleName, params);  // <아까 지정한 libraryName>_ModuleProvider
+  if (module != nullptr) {
+     return module;
+  }
```

- `./android/app/src/main/jni/MainComponentsRegistry.cpp`

먼저, codegen으로 생성된 `./android/app/build/generated/source/codegen/jni/react/renderer/components/<아까 지정한 libraryName>/ComponentDescriptors.h`파일을 확인해 생성된 ComponentDescriptor의 이름을 확인합니다.
아래의 예시) `using NewBackgroundViewComponentDescriptor = ConcreteComponentDescriptor<NewBackgroundViewShadowNode>;`

```
#include <react/renderer/componentregistry/ComponentDescriptorProviderRegistry.h>
+ #include <react/renderer/components/newarchsample/ComponentDescriptors.h>
...
-  // providerRegistry->add(concreteComponentDescriptorProvider<
-  //        AocViewerComponentDescriptor>());
-  providerRegistry->add(concreteComponentDescriptorProvider<
-            NewBackgroundViewComponentDescriptor>());
```

6. 상속(extend)받거나 구현(implement)해서 네이티브 모듈이나 네이티브 뷰를 작성해주시고, 모듈의 경우 TurboModulePackage / 뷰의 경우 이전 처럼 Package를 작성해주시면 모듈 작성은 끝나며,
   `..생략../<java패키지명>/newarchitecture/MainApplicationReactNativeHost.java`에서 적절하게 package를 추가해주시면 됩니다.

   이 부분은 공식 document나 `./android/app/src/main/java/com/newrn/newmodules/..`를 참조하셔서 작성해주시면 될 것 같습니다.

## 3. iOS

WIP
