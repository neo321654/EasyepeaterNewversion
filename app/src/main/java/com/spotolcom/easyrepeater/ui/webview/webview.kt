package com.spotolcom.easyrepeater.ui.webview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spotolcom.easyrepeater.R
import kotlinx.android.synthetic.main.webview_fragment.*


class webview : Fragment() {
    lateinit var webView: WebView
    companion object {
        fun newInstance() = webview()
    }

    private lateinit var viewModel: WebviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val view :View = inflater.inflate(R.layout.webview_fragment, container, false)
      //  web_view.setloadUrl("https://www.avito.ru/")

        webView = view.findViewById(R.id.web_view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebviewViewModel::class.java)
        // TODO: Use the ViewModel

    //    CookieSyncManager.createInstance(this)
//        val cookieManager: CookieManager = CookieManager.getInstance()
//        cookieManager.setAcceptCookie(true)
//
//        val webview = WebView(context)
//        val ws = webview.settings
//        ws.saveFormData = true
//        ws.savePassword = true // Not needed for API level 18 or greater (deprecated)
//
//
    //    WebAction(webview)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }

        webView.loadUrl("https://www.avito.ru/")


    }




}
