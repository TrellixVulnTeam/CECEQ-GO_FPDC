//
//  ViewController.swift
//  CECEQGO
//
//  Created by Marla on 2019/9/23.
//  Copyright © 2019 Marla. All rights reserved.
//

import UIKit
import MapKit
import MapboxCoreNavigation
import MapboxNavigation
import MapboxDirections


class HomeViewController: UIViewController {
    
  
    @IBOutlet weak var mapView: NavigationMapView!
    @IBOutlet weak var coursesInfoTable: UITableView!
    @IBOutlet weak var moreInformationBtn: UIButton!
    @IBOutlet weak var searchBarView: UIView!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var cursosActualesHeader: UIView!
    @IBOutlet weak var topTableConstraint: NSLayoutConstraint!
    
    @IBOutlet weak var plantaAltaBtn: UIButton!
    @IBOutlet weak var plantaBajaBtn: UIButton!
    
    //Mapbox variables
    var directionsRoute: Route?
    var layerPB : MGLFillStyleLayer?
    var layerPA : MGLFillStyleLayer?
    var labelLayerPB : MGLSymbolStyleLayer?
    var labelLayerPA : MGLSymbolStyleLayer?
    var labelStairsLayerPB : MGLSymbolStyleLayer?
    var labelStairsLayerPA : MGLSymbolStyleLayer?
    var labelRestroomLayerPB : MGLSymbolStyleLayer?
    var labelRestroomLayerPA : MGLSymbolStyleLayer?
    
    
    //5 current courses data
    var feedItems: NSArray = NSArray()
    var selectedLocation : CoursesModel = CoursesModel()
    
    //All courses data
    var allCoursesItems: NSArray = NSArray()
    var monthCourses : CoursesModel = CoursesModel()
    
    //All courses data
    var planta_alta = PlantaAltaModel()
    
    //Filter auxiliar variables
    var filtered: NSArray = NSArray()
    var original: NSArray = NSArray()
    
    //UISearch bar
    var searchController = UISearchController(searchResultsController: nil)
    var searchActive : Bool = false
    var filtroPorCurso : Bool = false
    
    var firstFloor : Bool = true
    var geojsonFile : String = "Planta_Baja_CECEQ"
    
    var figures_PlantaBaja: [String] = []
    var figures_PlantaAlta: [String] = []
    var fiteredFigures: [String] = []
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        let homeModel = HomeModel()
        homeModel.delegate = self
        homeModel.downloadItems()
        
        let allCourses = AllCourses()
        allCourses.delegate = self
        allCourses.downloadItems()
        
        coursesInfoTable.delegate = self
        coursesInfoTable.dataSource = self
        
        mapView.delegate = self
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
 
        mapView.updateUserLocationAnnotationViewAnimated(withDuration: 0.001)
        mapView.showsUserHeadingIndicator = true
        mapView.showsUserLocation = true
         
        mapView.setUserTrackingMode(.follow, animated: true, completionHandler: nil)
        mapView.setCenter(CLLocationCoordinate2D(latitude: 20.585760, longitude: -100.386339), zoomLevel: 17, animated: false)
        
        searchController.searchResultsUpdater = self
        searchController.searchBar.delegate = self
        searchBarView.addSubview(searchController.searchBar)
        searchController.searchBar.delegate = self

        searchController.searchBar.resignFirstResponder()
        self.searchController.hidesNavigationBarDuringPresentation  = false
        self.definesPresentationContext = true
        coursesInfoTable.allowsMultipleSelection = false
        
        let singleTap = UITapGestureRecognizer(target: self, action: #selector(handleMapTap(sender:)))
        for recognizer in mapView.gestureRecognizers! where recognizer is UITapGestureRecognizer {
        singleTap.require(toFail: recognizer)
        }
        mapView.addGestureRecognizer(singleTap)

        setUpStyle()
    }
    
    @IBAction func plantaAlta(_ sender: Any) {
        self.firstFloor = false
        plantaAltaBtn.backgroundColor = UIColor.lightGray
        plantaAltaBtn.titleLabel?.textColor = UIColor.white
        plantaBajaBtn.backgroundColor = UIColor.white
        plantaBajaBtn.titleLabel?.textColor = UIColor.darkGray
        self.showPA()
    }
    
    @IBAction func plantaBajaAction(_ sender: Any) {
        self.firstFloor = true
        plantaBajaBtn.backgroundColor = UIColor.lightGray
        plantaBajaBtn.titleLabel?.textColor = UIColor.white
        plantaAltaBtn.backgroundColor = UIColor.white
        plantaAltaBtn.titleLabel?.textColor = UIColor.darkGray
        self.showPB()
    }
}

// MARK: KEYBOARD AND SEARCH
extension HomeViewController: UISearchBarDelegate, UISearchControllerDelegate, UISearchResultsUpdating {
    
    //Keyboard hiding
    
    @objc func dismissKeyboard(_ sender: UITapGestureRecognizer) {
        view.endEditing(true)
        searchActive = false
        print(searchActive)
        if let nav = self.navigationController {
            nav.view.endEditing(true)
         
        }
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(HomeViewController.dismissKeyboard(_:)))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
        topTableConstraint.constant = -20
        searchActive = false
      
    }
    
     
    //UISearchBar BEGIN of edition Methods
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if feedItems.count > 0{
            filtered = allCoursesItems
            let resultPredicate = NSPredicate(format: "des_subevento CONTAINS[c] %@", searchText)
            self.filtered = self.original.filter{resultPredicate.evaluate(with: $0)} as NSArray
            searchActive = true
            filtroPorCurso = true
    
        }
        if(filtered.count == 0){
            fiteredFigures = self.figures_PlantaBaja
            fiteredFigures.append(contentsOf: self.figures_PlantaAlta)
            //self.fiteredFigures = fiteredFigures.filter({ $0.localizedCaseInsensitiveContains(searchText) })
            self.fiteredFigures = fiteredFigures.filter({  $0.range(of: searchText, options: [.diacriticInsensitive, .caseInsensitive]) != nil })
                    print(fiteredFigures)
                   searchActive = true
                   filtroPorCurso = false
        }
        
        if(filtered.count == 0 && self.fiteredFigures.count == 0){
            searchActive = false
            filtroPorCurso = false
        }
        
        
        self.coursesInfoTable.reloadData()
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        hideKeyboardWhenTappedAround() 
        scrollView.setContentOffset(CGPoint(x: 0.0, y: 330.0), animated: true)
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        topTableConstraint.constant = -330
    }
    
    func updateSearchResults(for searchController: UISearchController) {
    }
    
    
    //UISearchBar END of edition Methods
    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        topTableConstraint.constant = -20
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        topTableConstraint.constant = -20
        //searchActive = false
    }
}



// MARK: JSON PROTOCOLS

extension HomeViewController : HomeModelProtocol, AllCoursesProtocol{
    
    
    func itemsDownloaded(items: NSArray) {
        feedItems = items
        
        self.coursesInfoTable.reloadData()
    }
    
    func coursesDownloaded(items: NSArray) {
           allCoursesItems = items
           original = items
           self.coursesInfoTable.reloadData()
       }
    

}

// MARK: TABLE DELEGATES

extension HomeViewController : UITableViewDelegate, UITableViewDataSource{

    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(searchActive) {
            if(filtroPorCurso){
                let count = filtered.count > 6 ?  6 : filtered.count ;
                return count
            }else{
             let count = fiteredFigures.count > 6 ?  6 : fiteredFigures.count ;
             return count
            }
        }
        return feedItems.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CoursesTableViewCell", for: indexPath) as! CoursesTableViewCell
        cell.selectionStyle = UITableViewCell.SelectionStyle.blue
    
        if(searchActive){
            if(filtroPorCurso){
                cell.classroomNameLabel.isHidden = false
                cell.courseImage.image = #imageLiteral(resourceName: "ico_escultura")
                let item: CoursesModel = filtered[indexPath.row] as! CoursesModel
                cell.courseNameLabel.text = item.des_subevento
                cell.classroomNameLabel.text = item.nombre_completo
                
                var time = item.horainicial
                time =  String(((time?.split(separator: " ").last!)?.dropLast(3))!)
                cell.timeLabel.text = "Horario:  " + (time ?? "10:00")
                
                var date = item.horainicial
                date = String((((date?.split(separator: " ").first!)!)))
                cell.dateLabel.text = "Fecha: " + (date ?? "14/06/99")
            }else {
                cell.courseImage.image = #imageLiteral(resourceName: "ico_universidad")
                let item = fiteredFigures[indexPath.row]
                cell.courseNameLabel.text = item
                cell.classroomNameLabel.text = item
                cell.classroomNameLabel.isHidden = true
                cell.timeLabel.text = " "
                cell.dateLabel.text = ""
            }
        }else {
            cell.classroomNameLabel.isHidden = false
            cell.courseImage.image = #imageLiteral(resourceName: "ico_escultura")
            let item: CoursesModel = feedItems[indexPath.row] as! CoursesModel
            cell.courseNameLabel.text = item.des_subevento
            cell.classroomNameLabel.text = item.nombre_completo
            
            var time = item.horainicial
            time = String(((time?.split(separator: " ").last!)?.dropLast(3))!)
            cell.timeLabel.text = "Horario: " + (time ?? "10:00")
            
            var date = item.horainicial
            date = String((((date?.split(separator: " ").first!)!)))
            cell.dateLabel.text = "Fecha: " + (date ?? "14/06/99")
        }
        
        return cell
    }
   
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath) as! CoursesTableViewCell
        var salon : String = ""
            if(searchActive){
                if(filtroPorCurso){
                    salon = cell.classroomNameLabel.text!
                    print("selected class ", salon)
                    selectClassroom(name: cell.classroomNameLabel.text!)
        
                }else {
                    salon = cell.courseNameLabel.text!
                    print("selected classroom ", salon)
                    changeOpacity(name: cell.courseNameLabel.text!)
                }
            }else {
                salon = cell.classroomNameLabel.text!
                print("selected class ", salon)
                selectClassroom(name: cell.classroomNameLabel.text!)
      
            }
        if(figures_PlantaBaja.contains(salon)){
            self.firstFloor = true
            plantaBajaBtn.backgroundColor = UIColor.lightGray
            plantaBajaBtn.titleLabel?.textColor = UIColor.white
            plantaAltaBtn.backgroundColor = UIColor.white
            plantaAltaBtn.titleLabel?.textColor = UIColor.darkGray
            self.showPB()
           
        }else{
            self.firstFloor = false
            plantaAltaBtn.backgroundColor = UIColor.lightGray
            plantaAltaBtn.titleLabel?.textColor = UIColor.white
            plantaBajaBtn.backgroundColor = UIColor.white
            plantaBajaBtn.titleLabel?.textColor = UIColor.darkGray
            self.showPA()
            
        }
        mapView.setCenter(CLLocationCoordinate2D(latitude: 20.585760, longitude: -100.386339), zoomLevel: 17, animated: false)
        scrollView.setContentOffset(CGPoint(x: 0.0, y: 0.0), animated: true)
    }

}

// MARK: MAPBOX DELEGATE
extension HomeViewController : MGLMapViewDelegate{
 
    
    
    func mapView(_ mapView: MGLMapView, didFinishLoading style: MGLStyle) {
        self.figures_PlantaAlta.removeAll()
         self.figures_PlantaBaja.removeAll()
        let files : [String] = ["Planta_Baja_CECEQ", "Planta_Alta_CECEQ"]
        
        for file in files {
           DispatchQueue.global().async {
               guard let url = Bundle.main.url(forResource: file, withExtension: "geojson") else {
                   preconditionFailure("Failed to load local GeoJSON file")
               }
        
               guard let data = try? Data(contentsOf: url) else {
                   preconditionFailure("Failed to decode GeoJSON file")
                }
                do {

                    if let jsonDict = try JSONSerialization.jsonObject(with: data, options: []) as? [String : AnyObject] {
                 
                        if let features = jsonDict["features"] as? NSArray {
                            for feature in features {
                                if let feature = feature as? NSDictionary {
                                    if let properties = feature["properties"] as? NSDictionary {
                                        let label = properties["label"] as! String
                                        if file == "Planta_Baja_CECEQ"{
                                            self.figures_PlantaBaja.append(label)
                                        }else{
                                            self.figures_PlantaAlta.append(label)
                                        }
                                    }
                                }
                            }
                            
                        }
                    }
                }catch{ print("GeoJSON parsing failed") }
            }
        }
        DispatchQueue.main.async {
               self.drawBuilding()
            }
        }
    
    @objc @IBAction func handleMapTap(sender: UITapGestureRecognizer) {
        // Get the CGPoint where the user tapped.
        let spot = sender.location(in: mapView)
        
        // Get the name of the selected state.
        var layerIdentifier : String = "figs-pb"
        layerIdentifier = firstFloor == true ?  "figs-pb" :  "Planta_Alta_CECEQ";
         
        // Access the features at that point within the state layer.
        let features = mapView.visibleFeatures(at: spot, styleLayerIdentifiers: Set([layerIdentifier]))
         
        if let feature = features.first, let state = feature.attribute(forKey: "label") as? String {
            
            changeOpacity(name: state)
        } else {
            changeOpacity(name: "")
        }
    }
    
    func changeOpacity(name: String) {
        // Get the name of the selected state.
        var layerIdentifier : String = "figs-pb"
        layerIdentifier = firstFloor == true ?  "figs-pb" :  "Planta_Alta_CECEQ";
        
        guard let layer = mapView.style?.layer(withIdentifier: layerIdentifier) as? MGLFillStyleLayer else {
                fatalError("Could not cast to specified MGLFillStyleLayer")
        }
        // Check if a state was selected, then change the opacity of the states that were not selected.
        if !name.isEmpty {
            //layer.fillOpacity = NSExpression(format: "TERNARY(label = %@, 1, 0)", name)
            layer.fillColor = NSExpression(format: "TERNARY(label = %@, '#ffe24d', '#9CBFD9')", name)
        } else {
            // Reset the opacity for all states if the user did not tap on a state.
            layerPB!.fillColor = NSExpression(format: "TERNARY(label == 'Baños C' OR label == 'Baños A' OR label == 'Baños B' OR label == 'Baños D', '#ffe24d', '#9CBFD9')")
            layerPA!.fillColor = NSExpression(format: "TERNARY(label == 'Baños PA C' OR label == 'Baños PA A' OR label == 'Baños PA B' OR label == 'Baños PA D', '#ffe24d', '#9CBFD9')")
          
        }
    }
    
    func selectClassroom(name: String) {
        let files : [String] = ["figs-pb",  "Planta_Alta_CECEQ"]
        
        for file in files {
         
            guard let layer = mapView.style?.layer(withIdentifier: file) as? MGLFillStyleLayer else {
                    fatalError("Could not cast to specified MGLFillStyleLayer")
            }
            if !name.isEmpty {
                 //layer.fillOpacity = NSExpression(format: "TERNARY(label = %@, 1, 0)", name)
                layer.fillColor = NSExpression(format: "TERNARY(label = %@, '#ffe24d', '#9CBFD9')", name)
               
               
            } else {
                //layer.fillOpacity = NSExpression(forConstantValue: 1)
               layerPB!.fillColor = NSExpression(format: "TERNARY(label == 'Baños C' OR label == 'Baños A' OR label == 'Baños B' OR label == 'Baños D', '#ffe24d', '#9CBFD9')")
            layerPA!.fillColor = NSExpression(format: "TERNARY(label == 'Baños PA C' OR label == 'Baños PA A' OR label == 'Baños PA B' OR label == 'Baños PA D', '#ffe24d', '#9CBFD9')")
            }
        }
    }
     
    

    func drawBuilding()  {
      
                let sourcePB = MGLShapeSource(identifier: "Planta_Baja_CECEQ" , url:  Bundle.main.url(forResource: "Planta_Baja_CECEQ", withExtension: "geojson")!, options: nil)
                mapView.style?.addSource(sourcePB)
                // PLANTA BAJA SALONES
                layerPB = MGLFillStyleLayer(identifier: "figs-pb" , source: sourcePB)
                layerPB!.fillColor = nil
                layerPB!.fillColor = NSExpression(format: "TERNARY(label == 'Baños C' OR label == 'Baños A' OR label == 'Baños B' OR label == 'Baños D', '#ffe24d', '#9CBFD9')")
        layerPB!.fillOutlineColor = NSExpression(forConstantValue: UIColor(displayP3Red: 0.7804, green: 0.7804, blue: 0.7804, alpha: 1.0))
                mapView.style?.addLayer(layerPB!)
        
        
                // PLANTA BAJA NOMBRES
                labelLayerPB = MGLSymbolStyleLayer(identifier: "pb-labels", source: sourcePB)
                labelLayerPB!.text = NSExpression(format: "CAST(label, 'NSString')")
                labelLayerPB!.textOpacity = NSExpression(format: "mgl_interpolate:withCurveType:parameters:stops:($zoomLevel, 'linear', nil, %@)",  [16: 0, 17: 1])
                mapView.style?.addLayer(labelLayerPB!)
        
        
                 // PLANTA BAJA ESCALERAS
                labelStairsLayerPB = MGLSymbolStyleLayer(identifier: "pb-stairsIcon", source: sourcePB)
                labelStairsLayerPB?.predicate = NSPredicate(format: "label == 'Escalera A' OR label == 'Escalera B' OR label == 'Escalera C' OR label == 'Escalera D'")
                labelStairsLayerPB?.iconImageName = NSExpression(forConstantValue: "stairs")
                labelStairsLayerPB?.iconAllowsOverlap = NSExpression(forConstantValue: false)
                labelStairsLayerPB?.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(labelStairsLayerPB!)
                mapView.style!.setImage(UIImage(named: "stairs")!, forName: "stairs")
        
                // PLANTA BAJA BAÑOS
                labelRestroomLayerPB = MGLSymbolStyleLayer(identifier: "pb-restroomIcon", source: sourcePB)
                labelRestroomLayerPB?.predicate = NSPredicate(format: "label == 'Baños C' OR label == 'Baños A' OR label == 'Baños B' OR label == 'Baños D'")
                labelRestroomLayerPB?.text = NSExpression(format: "CAST(label, 'NSString')")
                labelRestroomLayerPB?.textAnchor = NSExpression(forConstantValue: NSValue(mglTextAnchor: .top))
                labelRestroomLayerPB?.iconImageName = NSExpression(forConstantValue: "ico_inodoro")
                labelRestroomLayerPB?.iconAllowsOverlap = NSExpression(forConstantValue: false)
                labelRestroomLayerPB?.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(labelRestroomLayerPB!)
                mapView.style!.setImage(UIImage(named: "ico_inodoro")!, forName: "ico_inodoro")
        
        
                // PLANTA ALTA SALONES
                let sourcePA = MGLShapeSource(identifier: "Planta_Alta_CECEQ" , url:  Bundle.main.url(forResource: "Planta_Alta_CECEQ", withExtension: "geojson")!, options: nil)
                mapView.style?.addSource(sourcePA)
                layerPA = MGLFillStyleLayer(identifier: "Planta_Alta_CECEQ" , source: sourcePA)
                layerPA!.fillColor = nil
                layerPA!.fillColor = NSExpression(format: "TERNARY(label == 'Baños PA C' OR label == 'Baños PA A' OR label == 'Baños PA B' OR label == 'Baños PA D', '#ffe24d', '#9CBFD9')")
                layerPA!.fillOutlineColor = NSExpression(forConstantValue: UIColor.white)
                mapView.style?.addLayer(layerPA!)
        
        
                 // PLANTA ALTA NOMBRES
                labelLayerPA = MGLSymbolStyleLayer(identifier: "pa-labels", source: sourcePA)
                labelLayerPA!.text = NSExpression(format: "CAST(label, 'NSString')")
                labelLayerPA!.textOpacity = NSExpression(format: "mgl_interpolate:withCurveType:parameters:stops:($zoomLevel, 'linear', nil, %@)", [16: 0, 17: 1])
                mapView.style?.addLayer(labelLayerPA!)
        
        
                 // PLANTA ALTA ESCALERAS
                labelStairsLayerPA = MGLSymbolStyleLayer(identifier: "pA-stairsIcon", source: sourcePA)
                labelStairsLayerPA?.predicate = NSPredicate(format: "label == 'Escalera PA A' OR label == 'Escalera PA B' OR label == 'Escalera PA C' OR label == 'Escalera PA D'")
                labelStairsLayerPA?.iconImageName = NSExpression(forConstantValue: "stairs")
                labelStairsLayerPA?.iconAllowsOverlap = NSExpression(forConstantValue: false)
                labelStairsLayerPA?.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(labelStairsLayerPA!)
                mapView.style!.setImage(UIImage(named: "stairs")!, forName: "stairs")
        
                // PLANTA ALTA BAÑOS
                labelRestroomLayerPA = MGLSymbolStyleLayer(identifier: "pb-restroomIconPA", source: sourcePA)
                labelRestroomLayerPA?.predicate = NSPredicate(format: "label == 'Baños PA C' OR label == 'Baños PA A' OR label == 'Baños PA B' OR label == 'Baños PA D'")
                labelRestroomLayerPA?.text = NSExpression(format: "CAST(label, 'NSString')")
                labelRestroomLayerPA?.textAnchor = NSExpression(forConstantValue: NSValue(mglTextAnchor: .top))
                labelRestroomLayerPA?.iconImageName = NSExpression(forConstantValue: "ico_inodoro")
                labelRestroomLayerPA?.iconAllowsOverlap = NSExpression(forConstantValue: false)
                labelRestroomLayerPA?.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(labelRestroomLayerPA!)
                mapView.style!.setImage(UIImage(named: "ico_inodoro")!, forName: "ico_inodoro")
        
                //  ACCESOS
                let sourceAccesos = MGLShapeSource(identifier: "Acceso_CECEQ" , url:  Bundle.main.url(forResource: "Acceso_CECEQ", withExtension: "geojson")!, options: nil)
                mapView.style?.addSource(sourceAccesos)
                
                // ACCESOS
                let accesosLayer = MGLSymbolStyleLayer(identifier: "figs-accesos" , source: sourceAccesos)
                accesosLayer.text = NSExpression(format: "CAST(label, 'NSString')")
                accesosLayer.iconImageName = NSExpression(forConstantValue: "ico_entrance")
                accesosLayer.iconAllowsOverlap = NSExpression(forConstantValue: false)
                labelStairsLayerPA?.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(accesosLayer)
                mapView.style!.setImage(UIImage(named: "ico_entrance")!, forName: "ico_entrance")
        
                //  FUENTES
                let fuenteLayerIcon = MGLSymbolStyleLayer(identifier: "figs-FUENTE" , source: sourcePB)
                fuenteLayerIcon.predicate = NSPredicate(format: "label == 'Fuente'")
                fuenteLayerIcon.iconImageName = NSExpression(forConstantValue: "ico_fuente")
                fuenteLayerIcon.iconAllowsOverlap = NSExpression(forConstantValue: false)
                fuenteLayerIcon.iconScale = NSExpression(forConstantValue: 0.9)
                mapView.style?.addLayer(fuenteLayerIcon)
                mapView.style!.setImage(UIImage(named: "ico_fuente")!, forName: "ico_fuente")
                
                self.labelLayerPA?.isVisible = false
                self.layerPA?.isVisible = false
                self.labelStairsLayerPA?.isVisible = false
                self.labelRestroomLayerPA?.isVisible = false
        
    }
        
    
    
    func showPA() {
        self.layerPB?.isVisible = false
         self.layerPA?.isVisible = true
        self.labelLayerPA?.isVisible = true
        self.labelLayerPB?.isVisible = false
        self.labelStairsLayerPB?.isVisible = false
        self.labelStairsLayerPA?.isVisible = true
        self.labelRestroomLayerPB?.isVisible = false
        self.labelRestroomLayerPA?.isVisible = true
    }
     
    func showPB() {
        self.layerPA?.isVisible = false
        self.labelLayerPA?.isVisible = false
        self.layerPB?.isVisible = true
        self.labelLayerPB?.isVisible = true
        self.labelStairsLayerPB?.isVisible = true
        self.labelStairsLayerPA?.isVisible = false
        self.labelRestroomLayerPB?.isVisible = true
        self.labelRestroomLayerPA?.isVisible = false
    }
    
    
}

// MARK: STYLING EXTENSION
extension HomeViewController {
    func setUpStyle(){
        
        coursesInfoTable.layer.cornerRadius = 5
        coursesInfoTable.layer.borderWidth = 0.5
        coursesInfoTable.layer.masksToBounds = false
        coursesInfoTable.layer.cornerRadius = 25
        coursesInfoTable.layer.borderColor = #colorLiteral(red: 1, green: 1.0039, blue: 1, alpha: 1)
        coursesInfoTable.sectionIndexMinimumDisplayRowCount = 6
        
        cursosActualesHeader.layer.cornerRadius = 5
        cursosActualesHeader.layer.borderWidth = 0.5
        cursosActualesHeader.layer.masksToBounds = false
        cursosActualesHeader.layer.cornerRadius = 25
        cursosActualesHeader.layer.borderColor =  #colorLiteral(red: 1, green: 1.0039, blue: 1, alpha: 1)
        
        searchController.searchBar.searchBarStyle = UISearchBar.Style.minimal
        searchController.searchBar.barTintColor = UIColor.white
        searchController.searchBar.placeholder = " Buscar..."
        searchController.searchBar.layer.cornerRadius = 25
        searchController.searchBar.tintColor = UIColor.black
        searchController.searchBar.backgroundColor = UIColor.white
        searchController.searchBar.translatesAutoresizingMaskIntoConstraints = true
        searchController.searchBar.leadingAnchor.constraint(equalTo: searchBarView.leadingAnchor, constant: 20).isActive = true
        searchController.searchBar.trailingAnchor.constraint(equalTo: searchBarView.trailingAnchor, constant: -20).isActive = true
        
        moreInformationBtn.layer.masksToBounds = false
        moreInformationBtn.layer.cornerRadius = 25
        moreInformationBtn.layer.shadowColor = UIColor(red: 0.0314, green: 0.1255, blue: 0.4863, alpha: 1.0).cgColor
        moreInformationBtn.layer.shadowOffset = CGSize.zero
        moreInformationBtn.layer.shadowOpacity = 0.4
        moreInformationBtn.layer.shadowRadius = 5
        
        plantaAltaBtn.layer.cornerRadius = 20
        plantaAltaBtn.layer.shadowOpacity = 0.2
        plantaAltaBtn.layer.shadowRadius = 1
        
        plantaBajaBtn.layer.cornerRadius = 20
        plantaBajaBtn.layer.shadowOpacity = 0.2
        plantaBajaBtn.layer.shadowRadius = 1
        
        /*mapiew.attributionButtonPosition = .bottomLeft
         mapView.logoViewPosition = .topRight*/
        
        //mapView.attributionButton.contentHorizontalAlignment = .left
        //mapView.attributionButton.frame.size = CGSize(width:   self.mapView.frame.size.width - 120, height: 25)V
        
        self.view.addConstraints([
        NSLayoutConstraint(item: mapView.logoView, attribute: .left, relatedBy: .equal, toItem: self.view, attribute: .left, multiplier: 1.0, constant: 10),
        NSLayoutConstraint(item: mapView.logoView, attribute: .bottom, relatedBy: .equal, toItem: self.coursesInfoTable, attribute: .top, multiplier: 1.0, constant: -10),
        
        NSLayoutConstraint(item: mapView.attributionButton, attribute: .width, relatedBy: .equal, toItem: nil, attribute: .width, multiplier: 1.0, constant: 25),
        NSLayoutConstraint(item: mapView.attributionButton, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 25),
        NSLayoutConstraint(item: mapView.attributionButton, attribute: .left, relatedBy: .equal, toItem: self.mapView.logoView, attribute: .left, multiplier: 1.0, constant: 90),
        NSLayoutConstraint(item: mapView.attributionButton, attribute: .bottom, relatedBy: .equal, toItem: self.coursesInfoTable, attribute: .top, multiplier: 1.0, constant: -10),
        
        NSLayoutConstraint(item: mapView.compassView, attribute: .right, relatedBy: .equal, toItem: self.searchBarView, attribute: .right, multiplier: 1.0, constant: -20),
        NSLayoutConstraint(item: mapView.compassView, attribute: .top, relatedBy: .equal, toItem: self.searchBarView, attribute: .bottom, multiplier: 1.0, constant:10),
        
        ])
      
}

}
