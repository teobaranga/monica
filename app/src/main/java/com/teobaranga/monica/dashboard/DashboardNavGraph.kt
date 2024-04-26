import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph<HomeNavGraph>(start = true, route = "root_dashboard")
annotation class DashboardNavGraph(
    val start: Boolean = false
)
