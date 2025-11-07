import MonicaApp
import Sentry
import SwiftUI

@main
struct iOSApp: App {

    init() {
        #if	!DEBUG
        SentrySetupKt.initializeSentry()
        #endif
        IosWebBrowserKt.iosWebBrowser = iOSWebBrowser()
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
