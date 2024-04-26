import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph<HomeNavGraph>(route = "root_journal")
annotation class JournalNavGraph(
    val start: Boolean = false,
)
