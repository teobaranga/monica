import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph(route = "root_home")
annotation class HomeNavGraph(
    val start: Boolean = false,
)
