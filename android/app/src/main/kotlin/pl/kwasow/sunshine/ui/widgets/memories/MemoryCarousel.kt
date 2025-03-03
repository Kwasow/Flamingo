package pl.kwasow.sunshine.ui.widgets.memories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.kwasow.sunshine.data.Memory

// ====== Public composables
@Composable
fun MemoryCarousel(
    todayMemories: List<Memory>,
    leadingMemories: List<Memory>,
) {
    val memoriesAndPhotos = getMemoriesWithPhotos(todayMemories)

    if (memoriesAndPhotos.isEmpty()) {
        PhotoGallery(memories = leadingMemories)
    } else {
        MemoryGallery(memories = memoriesAndPhotos)
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoGallery(memories: List<Memory>) {
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { memories.count() },
        preferredItemWidth = 300.dp,
        itemSpacing = 12.dp,
    ) { index ->
        val item = memories[index]

        MemoryPhotoView(
            url = item.photo ?: "",
            modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge),
        )
    }
}

@Composable
private fun MemoryGallery(memories: List<Any>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        SlidingCarousel(
            modifier = Modifier.fillMaxSize(),
            itemsCount = memories.size,
        ) { index ->
            when (val item = memories[index]) {
                is Memory -> MemoryView(memory = item)
                is String ->
                    MemoryPhotoView(url = item)
            }
        }
    }
}

@Composable
private fun SlidingCarousel(
    modifier: Modifier = Modifier,
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { itemsCount })

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(state = pagerState) { page ->
            itemContent(page)
        }

        Surface(
            modifier =
                Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = pagerState.currentPage,
                dotSize = 8.dp,
            )
        }
    }
}

@Composable
private fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unSelectedColor: Color = Color.Gray,
    dotSize: Dp,
) {
    LazyRow(
        modifier =
            modifier
                .wrapContentWidth()
                .wrapContentHeight(),
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize,
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
private fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(color),
    )
}

private fun getMemoriesWithPhotos(memories: List<Memory>): List<Any> {
    val result = mutableListOf<Any>()

    memories.forEach {
        if (it.photo != null) {
            result.add(it.photo)
        }

        result.add(it)
    }

    return result
}

// ====== Previews
@Preview(widthDp = 400, heightDp = 200)
@Composable
private fun PhotoGalleryPreview() {
    val memory =
        Memory(
            id = 0,
            startDate = "2024-01-01",
            endDate = "2024-01-15",
            title = "Event name",
            description = "This is a description of a memory",
            photo =
                "https://en.wikipedia.org/wiki/Photograph#/media/" +
                    "File:Nic%C3%A9phore_Ni%C3%A9pce_Oldest_Photograph_1825.jpg",
        )

    PhotoGallery(
        memories = listOf(memory, memory, memory, memory),
    )
}

@Preview(widthDp = 400, heightDp = 200)
@Composable
private fun MemoryGalleryPreview() {
    val memory =
        Memory(
            id = 0,
            startDate = "2024-01-01",
            endDate = "2024-01-15",
            title = "Event name",
            description = "This is a description of a memory",
            photo =
                "https://en.wikipedia.org/wiki/Photograph#/media/" +
                    "File:Nic%C3%A9phore_Ni%C3%A9pce_Oldest_Photograph_1825.jpg",
        )

    MemoryGallery(
        memories = listOf(memory, memory, memory, memory),
    )
}
