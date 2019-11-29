//
//  AllCourses.swift
//  CECEQGO
//
//  Created by Marla on 2019/11/5.
//  Copyright © 2019 Marla. All rights reserved.
//
import UIKit
import Foundation

protocol AllCoursesProtocol: class {
    func coursesDownloaded(items: NSArray)
}

class AllCourses: NSObject, NSURLConnectionDataDelegate {
    
    //properties
    
    weak var delegate: AllCoursesProtocol!
    
    var data = Data()
    
    let urlPath: String = "https://www.upyougo.com.mx/3monthCourses.php" //this will be changed to the path where service.php lives

    func downloadItems() {
        
        let url: URL = URL(string: urlPath)!
        let defaultSession = Foundation.URLSession(configuration: URLSessionConfiguration.default)
        
        let task = defaultSession.dataTask(with: url) { (data, response, error) in
            
            if error != nil {
                print("Failed to download data")
            }else {
                print("Data downloaded")
                self.parseJSON(data!)
            }
            
        }
        
        task.resume()
    }
    
    func parseJSON(_ data:Data) {
        
        var jsonResult = NSArray()
        
        do{
            jsonResult = try JSONSerialization.jsonObject(with: data, options:JSONSerialization.ReadingOptions.allowFragments) as! NSArray
            
        } catch let error as NSError {
            print(error)
            
        }
        
        var jsonElement = NSDictionary()
        let coursesArray = NSMutableArray()
        
        for i in 0 ..< jsonResult.count
        {
            
            jsonElement = jsonResult[i] as! NSDictionary
            
            let courses = CoursesModel()
            
            //the following insures none of the JsonElement values are nil through optional binding
            if let fecha = jsonElement["fecha"] as? String,
                let des_subevento = jsonElement["des_subevento"] as? String,
                let id_espacio = jsonElement["id_espacio"] as? String,
                let nombre_completo = jsonElement["nombre_completo"] as? String,
                let horainicial = jsonElement["horainicial"] as? String
            {
                
                courses.fecha = fecha
                courses.des_subevento = des_subevento
                courses.id_espacio = id_espacio
                courses.nombre_completo = nombre_completo
                courses.horainicial = horainicial
            }
            
            coursesArray.add(courses)
            
        }
        
        DispatchQueue.main.async(execute: { () -> Void in
            
            self.delegate.coursesDownloaded(items: coursesArray)
            
        })
    }
}

