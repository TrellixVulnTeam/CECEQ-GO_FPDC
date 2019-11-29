//
//  PlantaAltaModel.swift
//  CECEQGO
//
//  Created by Marla on 2019/11/5.
//  Copyright © 2019 Marla. All rights reserved.
//

import UIKit
import Foundation


struct Collection : Decodable {
    let type : String
    let features : [Feature]
}

struct Feature : Decodable {
    let type : String
    let properties : Properties
    // there is also geometry
}

struct Properties : Decodable {
    let level : String
    let navi : Bool
    let stack : Int
    let english : String
    let show_label : Bool
    let spanish : Int
    let label : String
    let render : String
    let show_region : Bool
    let id : String
}


class PlantaAltaModel: NSObject {
    
 //let urlBar = Bundle.main.url(forResource: "Planta_Baja_CECEQ", withExtension: "geojson")!
    
    
    let urlPath: String = "https://www.upyougo.com.mx/Planta_Alta_CECEQ.geojson"
    
    
    func geojeison() {
        let url: URL = URL(string: urlPath)!
        guard let jsonData = try? Data(contentsOf: url) else { return }
        do {
          
           let geoData = try JSONDecoder().decode(Collection.self, from: jsonData)
            print(geoData.features)
        } catch {
           print("\(error)")
        }
        
        /*do {
            let url: URL = URL(string: urlPath)!
            let jsonData = try Data(contentsOf: url)
            let result = try JSONDecoder().decode(Collection.self, from: jsonData )
            for feature in result.features {
                print("l4b3l:", feature.properties.label, "1d", feature.properties.id)
            }
        } catch {
            print("Error while parsing: \(error)")
        
        }*/
    }


}
