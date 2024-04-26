import com.ramcosta.composedestinations.annotation.NavGraph

@NavGraph<HomeNavGraph>(route = "root_contacts")
annotation class ContactsNavGraph(
    val start: Boolean = false,
)
