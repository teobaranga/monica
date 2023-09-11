import com.ramcosta.composedestinations.annotation.NavGraph

@HomeNavGraph(start = true)
@NavGraph(route = "root_dashboard")
annotation class DashboardNavGraph(
    val start: Boolean = false
)
