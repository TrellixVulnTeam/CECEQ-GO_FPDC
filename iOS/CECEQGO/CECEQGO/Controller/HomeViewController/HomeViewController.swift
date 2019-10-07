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
    
    
    var directionsRoute: Route?
    
    var feedItems: NSArray = NSArray()
    var selectedLocation : CoursesModel = CoursesModel()
    
    lazy var searchBar:UISearchBar = UISearchBar()
    let searchController = UISearchController(searchResultsController: nil)
    
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
        
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.showsUserLocation = true
        mapView.delegate = self
        mapView.setUserTrackingMode(.follow, animated: true, completionHandler: nil)
        
        searchController.searchBar.delegate = self
        searchBarView.addSubview(searchController.searchBar)
        
        self.searchController.hidesNavigationBarDuringPresentation  = false
        self.definesPresentationContext = true
        

        setUpStyle()
    }
    
    func setUpStyle(){
        
        coursesInfoTable.layer.cornerRadius = 30
        coursesInfoTable.layer.borderWidth = 0.5
        coursesInfoTable.layer.masksToBounds = false
        coursesInfoTable.layer.cornerRadius = 25
        
        searchController.searchBar.searchBarStyle = UISearchBar.Style.minimal
        searchController.searchBar.barTintColor = UIColor(red: 1.0, green: 0.872, blue: 0.349, alpha: 1.0)
        searchController.searchBar.placeholder = " Buscar..."
        searchController.searchBar.layer.cornerRadius = 25
        searchController.searchBar.backgroundColor = UIColor(red: 1.0, green: 0.872, blue: 0.349, alpha: 1.0)
        searchController.searchBar.translatesAutoresizingMaskIntoConstraints = true
        searchController.searchBar.leadingAnchor.constraint(equalTo: searchBarView.leadingAnchor, constant: 20).isActive = true
        searchController.searchBar.trailingAnchor.constraint(equalTo: searchBarView.trailingAnchor, constant: 20).isActive = true
        
        moreInformationBtn.layer.masksToBounds = false
        moreInformationBtn.layer.cornerRadius = 25
        moreInformationBtn.layer.shadowColor = UIColor(red: 0.0314, green: 0.1255, blue: 0.4863, alpha: 1.0).cgColor
        moreInformationBtn.layer.shadowOffset = CGSize.zero
        moreInformationBtn.layer.shadowOpacity = 0.4
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
    
   
    
    private func searchBarTextDidBeginEditing(searchBar: UISearchBar) {
        
        self.coursesInfoTable.topAnchor.constraint(lessThanOrEqualTo: coursesInfoTable.topAnchor, constant: -300).isActive = true
      
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(HomeViewController.dismissKeyboard(_:)))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    

    
    func searchBarTextDidEndEditing(_ searchBar: UISearchBar) {
        self.searchBar.endEditing(true)
         self.coursesInfoTable.topAnchor.constraint(lessThanOrEqualTo: coursesInfoTable.topAnchor, constant: -7).isActive = true
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.searchBar.endEditing(true)
        self.coursesInfoTable.topAnchor.constraint(lessThanOrEqualTo: coursesInfoTable.topAnchor, constant: -7).isActive = true
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

