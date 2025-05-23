# Image Carousel Module

A Compose Multiplatform image carousel component that provides smooth scrolling with center-snapping and scale animations.

## Features

- Cross-platform support (Android, iOS, Desktop, Web)
- Center-snapping behavior
- Scale animation based on item position
- Customizable item width fraction
- Optional fixed height for items
- DSL-style API for easy usage

## Usage

```kotlin
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.items

// Using aspect ratio (default behavior)
ImageCarousel(
    modifier = Modifier.fillMaxWidth(),
    itemWidthFraction = 0.75f // Items take 75% of screen width
) {
    items(items = myItemsList) { item ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        ) {
            // Content
        }
    }
}

// Using fixed height
ImageCarousel(
    modifier = Modifier.fillMaxWidth(),
    itemWidthFraction = 0.75f,
    itemHeight = 200.dp // Fixed height for all items
) {
    items(items = myItemsList) { item ->
        Card(
            modifier = Modifier.fillMaxSize() // Will use the fixed height
        ) {
            // Content
        }
    }
}
```

## Module Structure

```
image-carousel/
├── src/
│   ├── commonMain/
│   │   └── kotlin/
│   │       └── com/harrrshith/imagecarousel/
│   │           ├── ImageCarousel.kt
│   │           ├── ImageCarouselScope.kt
│   │           ├── ImageCarouselScopeImpl.kt
│   │           ├── ImageCarouselItemScope.kt
│   │           ├── CarouselItemWrapper.kt
│   │           └── utils/
│   │               └── ScreenSize.kt
│   ├── androidMain/
│   ├── iosMain/
│   ├── desktopMain/
│   └── wasmJsMain/
└── build.gradle.kts
```

## API

### ImageCarousel

Main composable function for the carousel.

**Parameters:**
- `modifier`: Modifier for the carousel
- `state`: LazyListState for controlling scroll position
- `contentPadding`: Padding values for content
- `itemWidthFraction`: Fraction of screen width for each item (default: 0.75f)
- `itemHeight`: Optional fixed height for carousel items (default: null)
- `content`: DSL block for defining carousel items

### ImageCarouselScope

Scope for defining carousel items with `item()` and `items()` functions.

### ImageCarouselItemScope

Provides access to:
- `itemWidth`: The calculated width of the item
- `itemHeight`: The fixed height of the item (if specified)
- `scale`: Current scale factor based on position
