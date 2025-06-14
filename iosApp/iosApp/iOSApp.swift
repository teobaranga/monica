import SwiftUI
import MonicaApp

@main
struct iOSApp: App {
    
    init() {
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
