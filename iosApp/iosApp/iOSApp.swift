import MonicaApp
import Sentry
import SwiftUI

@main
struct iOSApp: App {

    init() {
        IosWebBrowserKt.iosWebBrowser = iOSWebBrowser()
        SentrySDK.start { options in
            options.dsn = "https://c0f986968ef67872a5f0899cdb4cc1fb@o4510130255364096.ingest.de.sentry.io/4510171783757904"
            options.debug = true
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    AppLinksHandler.shared.handle(url: url.absoluteString)
                }
        }
    }
}
