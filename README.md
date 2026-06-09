# Companion library for Kotlin Multiplatform projects

[![Build](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/build.yml/badge.svg)](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/build.yml)
[![Test](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/test.yml/badge.svg)](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/test.yml)
[![Release](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/release.yml/badge.svg)](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/release.yml)

## Dependency

Available in the Ubique Artifactory:

```kotlin
implementation("ch.ubique.kmp:kmpanion:0.0.1")
```

You may find the current version and version history in
the [Releases list](https://github.com/UbiqueInnovation/kmpanion/releases).

## Features

### KMP (common)

These utilities are available on all supported platforms.

#### Extensions

**Any**

```kotlin
// Apply a block only if a condition is true, returns this
view.applyIf(isVisible) { alpha = 1f }

// Like applyIf but returns the result of the block
val result = value.runIf(condition) { transform() }

// Safe and unsafe casting
val str = obj.cast<String>()
val str = obj.castOrNull<String>()

// requireNotNull with an optional lazy message
val value = nullableValue.requireNotNull()
val value = nullableValue.requireNotNull { "Must not be null" }
```

**Boolean**

```kotlin
// Logical implication (a implies b)
val valid = isLoggedIn implies { hasPermission }

// Convert to Int (true → 1, false → 0)
val bit = flag.toInt()
```

**ByteArray / Byte**

```kotlin
val hex: String = byteArray.toHexString()
val hex: String = byte.toHexString()
```

**CharSequence**

```kotlin
"Jane Doe".isValidName()         // true (letters + whitespace/dash only)
"user@example.com".isValidMail() // true
"+41 79 123 45 67".isValidPhone() // true
"hello😀".containsSurrogateChars() // true (emoji = surrogate pair)
val s: String? = null
s.isNotNullOrEmpty()             // false (smart-casts to non-null on true)
```

**Collections**

```kotlin
list.toArrayList()
listOf(1, 2, 3).containsAny(listOf(3, 4)) // true
```

**Map**

```kotlin
val map: Map<String, Int> = mapWithNulls.filterNotNullValues()
```

**StringBuilder**

```kotlin
sb.appendNonNull(nullableString) // no-op if null
```

**String**

```kotlin
val hash: Long = "hello".longHashCode()

// Convert a String to an enum value
val state = "ACTIVE".toEnum<State>()
val state = "UNKNOWN".toEnum<State>(fallback = State.DEFAULT)
val state = "UNKNOWN".toEnumNullable<State>() // null if no match
```

#### Coroutines

`runSuspendCatching` is a `runCatching` equivalent that correctly handles coroutine cancellation — it rethrows
`CancellationException` instead of wrapping it in a `Result.failure`.

```kotlin
val result = runSuspendCatching { fetchData() }
```

#### Flow

**`SingleEventFlow`** — emits values that are consumed exactly once (equivalent to `SingleLiveEvent`):

```kotlin
val eventFlow = SingleEventFlow<String>()

// emit
eventFlow.emit("hello")
eventFlow.emit() // shorthand when T is Unit

// collect
eventFlow.asFlow().collect { value -> /* handled once */ }
```

**`SavedStateMutableStateFlow`** — a `MutableStateFlow` that persists its value in a `SavedStateHandle`, so it survives process
death:

```kotlin
val counter = SavedStateMutableStateFlow(savedStateHandle, "counter", initialValue = 0)
counter.value = 1 // written to both the flow and the SavedStateHandle
```

#### Lifecycle

`AppLifecycleState` (`FOREGROUND` / `BACKGROUND`) and `AppLifecycleSource` provide a cross-platform flow of the application
lifecycle state. Platform-specific implementations (`AndroidAppLifecycleSource`, `IosAppLifecycleSource`) are wired up
automatically.

#### Math

```kotlin
val dist: Float = euclideanDistance(aX, aY, bX, bY)
```

#### SemanticVersion

```kotlin
val v1 = SemanticVersion("1.2.3")
val v2 = SemanticVersion("1.10.0")
println(v1 < v2) // true

v1 == SemanticVersion("1.2.3") // true
```

---

### Android

These utilities are available on the Android target only.

#### Extensions

**Bitmap**

```kotlin
// Overlay a foreground bitmap onto a copy of the background
val combined: Bitmap? = background.overlaid(foreground)
val combined: Bitmap? = background.overlaid(foreground, matrix, paint)

// Overlay in-place (mutates the background)
background.overlay(foreground)

// Rotate
val rotated: Bitmap = bitmap.rotated(90f)

// Encode to Base64 (suspending)
val base64: String = bitmap.asBase64()
```

**Bundle**

Type-safe, API-level-aware helpers for reading from `Bundle` — handles the `TIRAMISU` deprecation split automatically:

```kotlin
// Optional (returns null if missing)
val parcel: MyParcelable? = bundle.optionalParcelable("key")
val list: ArrayList<MyParcelable>? = bundle.optionalParcelableArrayList("key")
val serial: MySerializable? = bundle.optionalSerializable("key")
val map: Map<String, Int>? = bundle.optionalSerializableMap("key")

// Required (throws if missing)
val parcel: MyParcelable = bundle.requireParcelable("key")
val str: String = bundle.requireString("key")

// Builder DSL
val bundle = buildBundle {
	putString("key", "value")
	putInt("count", 42)
}
```

**ByteArray / String — MD5**

```kotlin
val hex: String = byteArray.md5()
val raw: ByteArray = byteArray.md5Bytes()

val hex: String = "hello".md5()
val raw: ByteArray = "hello".md5Bytes()
```

**Context**

```kotlin
context.dpToPx(16)    // Float
context.spToPx(14)    // Float

context.isTablet()    // true if smallestScreenWidthDp >= 600
context.isPortrait()
context.isLandscape()

context.getStringByResName("my_string_name")
context.getDrawableByResName("my_drawable_name")

context.isPackageInstalled("com.example.app")
context.isTalkBackEnabled()
context.isInDarkMode()

context.getActivityContext()         // Activity? (walks the ContextWrapper chain)
context.requireActivityContext()     // Activity (throws if not found)

context.isPermissionGranted(Manifest.permission.CAMERA)
context.isLocationServiceEnabled()
context.getLocationPermissionState() // LocationPermissionState enum
```

**Cursor**

Null-safe column reads by column name:

```kotlin
cursor.getStringOrNull("name")
cursor.getLongOrNull("timestamp")
cursor.getIntOrNull("count")
cursor.getDoubleOrNull("lat")
cursor.getFloatOrNull("score")
cursor.getShortOrNull("flags")
```

**File**

```kotlin
// Read EXIF rotation from an image file (0 / 90 / 180 / 270)
val degrees: Int = file.getImageRotation()
```

**Point / PointF**

```kotlin
val dist: Float = point.euclideanDistanceTo(other)
val dist: Float = pointF.euclideanDistanceTo(otherX, otherY)
```

**Spannable**

Regex-based search-and-replace that preserves existing spans and allows adding new ones:

```kotlin
spannable.replace(Pattern.compile("\\bhello\\b")) { matchResult, matched ->
	SpannableString(matched).apply {
		setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
	}
}
```

**String (Android)**

```kotlin
"café".unaccent()       // "cafe"
"hello world".capitalize() // "Hello world"
"query param".urlEncode()  // "query%20param"
```

#### Flow

**`SharedPreferenceMutableStateFlow`** — a `MutableStateFlow` backed by `SharedPreferences`. Reads the persisted value on
construction and writes back on every update. Supports `Long`, `String`, `Int`, `Boolean`, `Float`, and `Set<String>`.

```kotlin
val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
val isDarkMode = SharedPreferenceMutableStateFlow(prefs, "dark_mode", initialValue = false)

isDarkMode.value = true // persisted to SharedPreferences immediately
```

#### Permissions

**`LocationPermissionState`** — enum describing the current location permission level:

```kotlin
when (context.getLocationPermissionState()) {
	LocationPermissionState.FINE_BACKGROUND -> { /* precise + background */
	}
	LocationPermissionState.FINE_FOREGROUND -> { /* precise, foreground only */
	}
	LocationPermissionState.COARSE_BACKGROUND -> { /* approximate + background */
	}
	LocationPermissionState.COARSE_FOREGROUND -> { /* approximate, foreground only */
	}
	LocationPermissionState.DENIED -> { /* not granted */
	}
}
```

**`RuntimePermissionHandler`** — handles the full Android permission request lifecycle (initial request, rationale, settings
redirect) for both `Activity` and `Fragment`:

```kotlin
// In an Activity or Fragment
val handler = registerPermissionHandler(
	Manifest.permission.CAMERA,
	object : RuntimePermissionHandler.Listener {
		override fun onPermissionResult(isGranted: Boolean) { /* react */
		}
		override fun showJumpToAppSettingsExplanation(intent: Intent) {
			// Show a dialog explaining why the user should go to settings, then startActivity(intent)
		}
	}
)

// When the user taps "Grant"
handler.requestPermission()

// Check state
handler.isPermissionGranted()

// Open the correct settings screen directly
handler.openAppSettings()
```

Multiple permissions are supported via the `Set<String>` overloads.

---

### Compose

These utilities are for Android + Jetpack Compose.

#### Adaptive Layouts

**`SinglePaneLayout`** — constrains its content to a maximum width (default 640 dp) and aligns it within the available space. Useful
for readable layouts on large screens:

```kotlin
SinglePaneLayout(
	maxWidth = 640.dp,
	alignment = Alignment.TopCenter,
) {
	MyContent()
}
```

**`SinglePaneLazyColumn`** — a `LazyColumn` that adds horizontal padding to cap content width. Drop-in replacement for `LazyColumn`
on wide-screen layouts:

```kotlin
SinglePaneLazyColumn(
	maxContentWidth = 640.dp,
	alignment = Alignment.CenterHorizontally,
) {
	items(myList) { item -> MyListItem(item) }
}
```

#### Buttons

`UbiqueButton` provides a DSL-driven button that covers filled, outlined, text, and icon variants with composable content:

```kotlin
UbiqueButton(onClick = { /* ... */ }) {
	filled {
		icon(R.drawable.ic_send, size = 20.dp)
		spacing(8.dp)
		text("Send")
	}
}

UbiqueButton(enabled = isLoading.not(), onClick = { /* ... */ }) {
	outlined {
		loading(size = 20.dp)
		spacing(8.dp)
		text("Loading…")
	}
}

UbiqueButton(onClick = { /* ... */ }) {
	icon(R.drawable.ic_close)
}
```

#### Extensions

**Any**

```kotlin
// Turn a nullable value into an optional @Composable lambda
val content: (@Composable () -> Unit)? = nullableItem.letComposable { item ->
	Text(item.label)
}
```

**Clipboard**

```kotlin
val clipboard = LocalClipboard.current

// Write
LaunchedEffect(Unit) { clipboard.setText("Hello") }

// Read
LaunchedEffect(Unit) { val text = clipboard.getText() }
```

**Color**

```kotlin
// Shorthand for copy(alpha = …)
val faded = color.alpha(0.5f)

// Pick light/dark-mode color inline
val background = Color.White or Color.Black // second value used in dark mode
```

**LazyListScope**

```kotlin
LazyColumn {
	// Items with built-in animateItem() modifier
	animatedItem(key = "header") { MyHeader() }
	animatedItems(items = myList, key = { it.id }) { item -> MyRow(item) }

	// Spacer item
	spacer(size = 16.dp)
}
```

**MeasureScope**

```kotlin
// Return a zero-size layout from a custom Layout measurement
fun MeasureResult = emptyLayout()
```

**TextStyle**

Chainable property extensions for quick text style variations:

```kotlin
MaterialTheme.typography.bodyMedium.bold.italic.underline
MaterialTheme.typography.titleLarge.color(Color.Red)
MaterialTheme.typography.labelSmall.white
```

Available modifiers: `thin`, `extraLight`, `light`, `medium`, `semiBold`, `bold`, `extraBold`, `italic`, `underline`,
`strikeThrough`, `white`, `black`, `color(Color)`.

**WindowInsets**

```kotlin
Modifier.windowInsetsPadding(WindowInsets.none) // zero insets
```

#### Images

**`Base64Image`** — decodes a Base64-encoded image string asynchronously and displays a placeholder while loading:

```kotlin
Base64Image(
	encodedImage = base64String,
	placeholder = { CircularProgressIndicator() },
	contentScale = ContentScale.Crop,
)
```

**`rememberBlurHashDrawable`** — decodes a [BlurHash](https://blurha.sh) into a `BitmapDrawable` state, async in normal mode and
sync in preview mode:

```kotlin
val drawable by rememberBlurHashDrawable(
	blurhash = "LKO2?U%2Tw=w]~RBVZRi};RPxuwH",
	blurhashHeight = 200.dp,
	blurhashAspectRatio = 16.0 / 9.0,
)
```

**`LargeBitmapImage`** — loads a large bitmap (e.g. from disk) asynchronously, scales it to fit the available space, and crossfades
it in:

```kotlin
LargeBitmapImage(
	bitmapLoader = { BitmapFactory.decodeFile(path) },
	contentScale = ContentScale.Fit,
)
```

#### Layout

**`SelectableColumn`** — wraps a `Column` in a `SelectionContainer` so text inside is long-press selectable:

```kotlin
SelectableColumn {
	Text("This text can be selected")
	Text("So can this")
}
```

#### Modifiers

```kotlin
// Continuously rotate an element
Icon(painter, modifier = Modifier.infiniteRotation(durationPerRotationInMs = 1000))

// Detect a triple-tap
Box(modifier = Modifier.tripleClick { showEasterEgg() })

// Detect an N-tap
Box(modifier = Modifier.multiClick(count = 5) { doSomething() })
```

#### Permissions

**`PermissionScaffold`** — declaratively handles a single permission, rendering different content depending on state:

```kotlin
PermissionScaffold(
	permission = Manifest.permission.CAMERA,
	permissionNotGrantedContent = { handler ->
		Button(onClick = handler::requestPermission) { Text("Grant Camera") }
	},
	permissionRationaleContent = { intent, onHandled ->
		AlertDialog(
			onDismissRequest = onHandled,
			confirmButton = { TextButton(onClick = { startActivity(intent); onHandled() }) { Text("Settings") } },
			text = { Text("Camera access is required. Please enable it in Settings.") },
		)
	},
	permissionGrantedContent = {
		CameraPreview()
	},
	onPermissionGranted = { /* one-shot callback */ },
)
```

**`MultiPermissionScaffold`** — same pattern for multiple permissions at once, accepting an `ImmutableList<String>`.

#### Preview Annotations

Multi-preview annotations covering common device/theme combinations:

| Annotation                 | Renders on                           |
|----------------------------|--------------------------------------|
| `@ComponentPreviews`       | Light + dark mode                    |
| `@PhonePortraitPreviews`   | Pixel 9 portrait, light + dark       |
| `@PhoneLandscapePreviews`  | Pixel 9 landscape, light + dark      |
| `@PhonePreviews`           | Portrait + landscape, light + dark   |
| `@TabletPortraitPreviews`  | Pixel Tablet portrait, light + dark  |
| `@TabletLandscapePreviews` | Pixel Tablet landscape, light + dark |
| `@TabletPreviews`          | Portrait + landscape, light + dark   |
| `@ScreenPreviews`          | All phone + tablet variants          |

```kotlin
@ScreenPreviews
@Composable
fun MyScreenPreview() {
	MyTheme { MyScreen() }
}
```

#### Screen

**`ScreenBrightnessOverride`** — forces full brightness while the composable is in the composition (e.g. for QR code display) and
restores it on disposal:

```kotlin
ScreenBrightnessOverride()
```

**`ScreenshotProtection`** — adds `FLAG_SECURE` to the window while the lifecycle is started, preventing screenshots and screen
recordings:

```kotlin
ScreenshotProtection()
```

**`StatusBarColor`** — sets the status bar icon appearance (light/dark) and keeps it in sync across lifecycle resume events:

```kotlin
// Default: follows system dark mode
StatusBarColor()

// Or explicitly
StatusBarColor(useLightStatusBarIcons = true)
```

#### Swipeable

`SwipeableContainer` wraps content in a horizontally draggable container with left and right reveal slots (e.g. for swipe-to-delete
or swipe-to-action rows):

```kotlin
SwipeableContainer(
	canSwipeToLeft = true,
	onSwipedToLeft = { deleteItem() },
	swipeToLeftContent = {
		Box(Modifier.fillMaxSize().background(Color.Red)) {
			Icon(Icons.Default.Delete, contentDescription = null)
		}
	},
) {
	MyListItem()
}
```

The underlying `AnchoredDraggableState<DragAnchor>` can be obtained via `rememberDraggableState()` for external control.

#### Toggleable

`Toggleable` renders a full-width tappable row (checkbox or switch + label) that is correctly exposed to accessibility services.
Content is specified via a DSL:

```kotlin
// Checkbox
Toggleable(
	toggleState = if (isChecked) ToggleableState.On else ToggleableState.Off,
	onClick = { isChecked = !isChecked }
) {
	checkbox(toggleState)
	spacing(10.dp)
	label("Accept terms", MaterialTheme.typography.bodyMedium)
}

// Switch
Toggleable(
	toggleState = isEnabled.toToggleableState(),
	onClick = { isEnabled = !isEnabled }
) {
	label("Enable notifications", MaterialTheme.typography.bodyMedium)
	spacing(10.dp)
	switch(checked = isEnabled)
}
```

Helper extensions:

```kotlin
val state: ToggleableState = true.toToggleableState()
val bool: Boolean = ToggleableState.Indeterminate.toBoolean(indeterminateValue = false)
```

#### WebView

`UbiqueWebView` is a Compose wrapper around the Android `WebView` with back-navigation support, loading state tracking, and error
collection:

```kotlin
val state = remember { WebViewState(WebContent.Url("https://example.com")) }

UbiqueWebView(
	state = state,
	captureBackPresses = true,
	onCreated = { webView -> webView.settings.javaScriptEnabled = true },
)

// Check loading state
if (state.isLoading) CircularProgressIndicator()
```

`WebContent` variants: `WebContent.Url`, `WebContent.Data` (HTML string), `WebContent.Post`.

## Development & Testing

Most features of this library can be implemented with test-driven development using unit tests with a mock webserver instance.

To test any changes locally in an app, you can either include the library via dependency substitution in an application project,
or deploy a build to your local maven repository and include that from any application:

1. Define a unique custom version by setting the `VERSION_NAME` variable in the `gradle.properties` file.
2. Deploy the library artifact by running `./gradlew publishToMavenLocal`
3. Reference the local maven repository in your application's build script:

    ```kotlin
    repositories {
        mavenLocal()
    }
    ```

4. And apply the local library version:

    ```kotlin
    dependencies {
        implementation("ch.ubique.kmp:kmpanion:$yourLocalVersion")
    }
    ```

### Unit Tests

Unit tests and coverage reports are run on the JVM target by default.
See also workflows for [Test](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/test.yml)
and [Coverage](https://github.com/UbiqueInnovation/kmpanion/actions/workflows/coverage.yml).

## Deployment

Create a [Release](https://github.com/UbiqueInnovation/kmpanion/releases),
setting the Tag to the desired version prefixed with a `v`.

Each release on GitHub will be deployed to the Ubique Artifactory.

* Group: `ch.ubique.kmp`
* Artifact: `kmpanion`
* Version: `major.minor.revision`