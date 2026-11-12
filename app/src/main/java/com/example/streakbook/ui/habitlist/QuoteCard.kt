
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streakbook.ui.habitlist.QuoteState


@Composable
fun QuoteCard(state: QuoteState, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val text = MaterialTheme.typography

    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerHighest),
    ) {
        Column(Modifier.padding(16.dp).animateContentSize()) {     // smooth height change
            Text("Quote of the day", style = text.labelMedium, color = colors.primary)
            Spacer(Modifier.height(10.dp))
            when (state) {
                QuoteState.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                    Text("Loading quote…", style = text.bodyMedium)
                }
                is QuoteState.Error -> Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning, contentDescription = null,
                        tint = colors.error, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(state.message, style = text.bodyMedium, modifier = Modifier.weight(1f))
                    TextButton(onClick = onRetry) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Try again")
                    }
                }
                is QuoteState.Success -> Column {
                    Text("“${state.quote.text}”",
                        style = text.bodyLarge.copy(fontStyle = FontStyle.Italic, lineHeight = 22.sp))
                    Spacer(Modifier.height(8.dp))
                    Text("— ${state.quote.author}", style = text.bodyMedium,
                        color = colors.onSurfaceVariant, modifier = Modifier.align(Alignment.End))
                }
            }
        }
    }
}