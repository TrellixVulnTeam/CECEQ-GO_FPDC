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
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var coursesInfoTable: UITableView!
    @IBOutlet weak var moreInformationBtn: UIButton!
    
    var directionsRoute: Route?
    var feedItems: NSArray = NSArray()
    var selectedLocation : CoursesModel = CoursesModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let homeModel = HomeModel()
        homeModel.delegate = self
        homeModel.downloadItems()
        
        coursesInfoTable.delegate = self
        coursesInfoTable.dataSource = self
        coursesInfoTable.layer.cornerRadius = 15
        
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.showsUserLocation = true
        mapView.delegate = self
        mapView.setUserTrackingMode(.follow, animated: true, completionHandler: nil)
        
        searchBar.layer.cornerRadius = 22
        
        moreInformationBtn.layer.masksToBounds = true
        moreInformationBtn.layer.cornerRadius = 25
        moreInformationBtn.layer.shadowRadius = 25.0
        moreInformationBtn.layer.shadowColor = UIColor(red:1.0, green:0.00, blue:0.00, alpha:1.0).cgColor
        moreInformationBtn.layer.shadowOffset = CGSize.zero
        moreInformationBtn.layer.shadowOpacity = 1.0

    }


}

extension HomeViewController : HomeModelProtocol{
    
    func itemsDownloaded(items: NSArray) {
        feedItems = items
        self.coursesInfoTable.reloadData()
    }
    
    
    
}

extension HomeViewController : UITableViewDelegate, UITableViewDataSource{
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return feedItems.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CoursesTableViewCell", for: indexPath) as! CoursesTableViewCell
        
        cell.courseImage.image = #imageLiteral(resourceName: "ico_escultura")
        
        let item: CoursesModel = feedItems[indexPath.row] as! CoursesModel
        cell.courseNameLabel.text = item.des_subevento
        
        cell.classroomNameLabel.text = item.nombre_completo
        
        return cell
        
    }
    
    
    
}

extension HomeViewController : MGLMapViewDelegate{
    
    func calculateRoute(from originCoor: CLLocationCoordinate2D, to destinationCoor: CLLocationCoordinate2D, completion: @escaping (Route?, Error) -> Void){
        
        let origin = Waypoint(coordinate: originCoor, coordinateAccuracy: -1 , name: "Start")
        
        let destination = Waypoint(coordinate: destinationCoor, coordinateAccuracy: -1, name: "Finish")
        
        let options = NavigationRouteOptions(waypoints: [origin, destination], profileIdentifier: .walking)
        
        _ = Directions.shared.calculate(options, completionHandler: { (waypoints, routes, error) in
            self.directionsRoute = routes?.first
            
            //draw line
            let coordinateBounds = MGLCoordinateBounds(sw: destinationCoor, ne: originCoor)
            let insets = UIEdgeInsets(top: 50, left: 50, bottom: 50, right: 50)
            let routeCam = self.mapView.cameraThatFitsCoordinateBounds(coordinateBounds, edgePadding: insets)
            self.mapView.setCamera(routeCam, animated: true)
            
        })
    }
    
   
}

