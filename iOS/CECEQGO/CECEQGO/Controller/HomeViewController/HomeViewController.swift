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
    
    var directionsRoute: Route?
    
    var feedItems: NSArray = NSArray()
    var selectedLocation : CoursesModel = CoursesModel()
    
    lazy var searchBar:UISearchBar = UISearchBar()
    
    var expandedIndexPath: NSIndexPath? // Index path of the cell that is currently expanded
    let collapsedHeight: CGFloat = 44.0 // Constant to set the default collapsed height
    //var ticketHistoryService = TicketHistoryService() // Service to gather info about Ticket History CoreData
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let homeModel = HomeModel()
        homeModel.delegate = self
        homeModel.downloadItems()
        
        coursesInfoTable.delegate = self
        coursesInfoTable.dataSource = self
        coursesInfoTable.layer.cornerRadius = 30
        coursesInfoTable.layer.borderWidth = 0.5
        coursesInfoTable.layer.masksToBounds = false
        coursesInfoTable.layer.cornerRadius = 25
        
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.showsUserLocation = true
        mapView.delegate = self
        mapView.setUserTrackingMode(.follow, animated: true, completionHandler: nil)
        
        searchBar.searchBarStyle = UISearchBar.Style.prominent
        searchBar.placeholder = " Buscar..."
        searchBar.layer.cornerRadius = 20
        searchBar.sizeToFit()
        searchBar.isTranslucent = false
        searchBar.backgroundColor = UIColor.yellow
        searchBar.delegate = self
        view.addSubview(searchBar)
        
        definesPresentationContext = true
        
        moreInformationBtn.layer.masksToBounds = false
        moreInformationBtn.layer.cornerRadius = 25
        moreInformationBtn.layer.shadowColor = UIColor.init(red: 0.725, green: 0.796, blue: 0.877, alpha: 1.0).cgColor
        moreInformationBtn.layer.shadowOffset = CGSize.zero
        moreInformationBtn.layer.shadowOpacity = 0.8
        moreInformationBtn.layer.shadowRadius = 5

    }


}

extension HomeViewController: UISearchBarDelegate {
    
    @objc func dismissKeyboard(_ sender: UITapGestureRecognizer) {
        view.endEditing(true)
        
        if let nav = self.navigationController {
            nav.view.endEditing(true)
        }
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(HomeViewController.dismissKeyboard(_:)))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    

    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        self.searchBar.endEditing(true)
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.searchBar.endEditing(true)
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

