//
//  MoreInformationViewController.swift
//  CECEQGO
//
//  Created by Marla on 2019/9/27.
//  Copyright © 2019 Marla. All rights reserved.
//

import UIKit
import  WebKit
import QuartzCore
import PDFKit

class MoreInformationViewController: UIViewController {

    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    
    @IBAction func backArrow(_ sender: Any) {
        
        dismiss(animated: true, completion: nil)
    }
    
  
    @IBAction func information(_ sender: Any) {
        let viewC = UIViewController()
        
        let url = Bundle.main.url(forResource: "terms", withExtension: ".pdf")! // for file in bundle
        let webView = UIWebView.init(frame: UIScreen.main.bounds)
        viewC.view.addSubview(webView)
        webView.backgroundColor = UIColor.white
        webView.autoresizingMask = [.flexibleHeight, .flexibleWidth]
        webView.loadRequest(URLRequest.init(url: url))
        
        let button = UIButton(type: .custom)
        button.frame = CGRect(x: 0, y: UIScreen.main.bounds.size.height - 100, width: UIScreen.main.bounds.size.width, height: 60)
        button.backgroundColor = UIColor.red
        button.setTitle("Salir", for: .normal)
        button.addTarget(self, action: #selector(self.closeTerms(_ :)), for: .touchUpInside)
        viewC.view.addSubview(button)
        
        self.present(viewC, animated: true, completion: {
            
        })
        
    }
    
    
    @IBAction func terms(_ sender: Any) {
        let email = "marla.galvn@gmail.com"
        if let url = URL(string: "mailto:\(email)") {
           UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    
    @IBAction func ayuda(_ sender: Any) {
        let email = "marla.galvn@gmail.com"
        if let url = URL(string: "mailto:\(email)") {
           UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    
    @objc func closeTerms(_ sender: Any){
        print("Close")
        dismiss(animated: true, completion: nil)
    }
    
}
