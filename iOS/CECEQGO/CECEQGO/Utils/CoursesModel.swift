//
//  CoursesModel.swift
//  CECEQGO
//
//  Created by Marla on 2019/10/2.
//  Copyright © 2019 Marla. All rights reserved.
//

import UIKit
import Foundation

class CoursesModel: NSObject {
    
    //properties
    
    var fecha: String?
    var des_subevento: String?
    var id_espacio: String?
    var nombre_completo: String?
    
    
    //empty constructor
    
    override init()
    {
        
    }
    
    //construct with @name, @address, @latitude, and @longitude parameters
    
    init(fecha: String, des_subevento: String, id_espacio: String, nombre_completo: String) {
        
        self.fecha = fecha
        self.des_subevento = des_subevento
        self.id_espacio = id_espacio
        self.nombre_completo = nombre_completo
        
    }
    
    
    //prints object's current state
    
    override var description: String {
        return "fecha: \(fecha), des_subevento: \(des_subevento), id_espacio: \(id_espacio), nombre_completo: \(nombre_completo)"
        
    }
}
