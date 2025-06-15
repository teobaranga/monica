import MonicaApp
import SafariServices

class iOSWebBrowser: @preconcurrency PlatformWebBrowser {
    
    @MainActor
    func open(url: String) {
        if let url = URL(string: url) {
            let vc = SFSafariViewController(url: url)
            UIApplication.shared.firstKeyWindow?.rootViewController?.present(vc, animated: true)
        }
    }
}

extension UIApplication {
    var firstKeyWindow: UIWindow? {
        return UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .filter { $0.activationState == .foregroundActive }
            .first?.keyWindow
    }
}
