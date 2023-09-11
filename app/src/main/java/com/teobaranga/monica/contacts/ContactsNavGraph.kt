import com.ramcosta.composedestinations.annotation.NavGraph

@HomeNavGraph
@NavGraph(route = "root_contacts")
annotation class ContactsNavGraph(
    val start: Boolean = false
)
