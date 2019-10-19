function dia_hoy(id){
    var today = new Date();
var dd = String(today.getDate()).padStart(2, '0');
var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
var yyyy = today.getFullYear();

today = dd + '/' + mm + '/' + yyyy;
    document.getElementById(id).innerHTML = 'Reporte de Hoy: ' + today;
}

function mes(id){
var monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
  "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
];

var d = new Date();
    document.getElementById(id).innerHTML = "Reporte del Mes: " + monthNames[d.getMonth()];
}

function year(id){
    var today = new Date();
var yyyy = today.getFullYear();

today = yyyy;
    document.getElementById(id).innerHTML = 'Reporte del año: ' + today;
}

function rango(id){
    document.getElementById(id).innerHTML = 'Reporte de: <input type ="date" class ="date_label">  a  <input type ="date" class ="date_label">';
}