import com.ramcosta.composedestinations.annotation.NavGraph

@HomeNavGraph
@NavGraph(route = "root_journal")
annotation class JournalNavGraph(
    val start: Boolean = false
)
