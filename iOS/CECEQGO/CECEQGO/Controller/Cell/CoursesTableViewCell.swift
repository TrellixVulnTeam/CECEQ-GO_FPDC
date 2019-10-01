//
//  CursesTableViewCell.swift
//  CECEQGO
//
//  Created by Marla on 2019/9/25.
//  Copyright © 2019 Marla. All rights reserved.
//

import UIKit

class CoursesTableViewCell: UITableViewCell {
    
    @IBOutlet weak var courseImage: UIImageView!
    @IBOutlet weak var courseNameLabel: UILabel!
    @IBOutlet weak var classroomNameLabel: UILabel!
    
    

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
