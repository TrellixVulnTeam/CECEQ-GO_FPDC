function dia_hoy(id,id2){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = dd + '/' + mm + '/' + yyyy;
    document.getElementById(id).innerHTML = 'Reporte de Hoy: ' + today + ' <br><br><button class ="btn btn-primary" id ="hoy1">Descargar</button>';

    document.getElementById(id2).innerHTML = '';
    var speedCanvas = document.getElementById("densityChart");

    Chart.defaults.global.defaultFontFamily = "Lato";
    Chart.defaults.global.defaultFontSize = 18;

    var speedData = {
      labels: ["8:00 Am", "10:00 Am", "12:00pm", "2:00pm", "4:00pm", "6:00pm", "8:00pm"],
      datasets: [{
        label: "Visitas Hoy",
        data: [0, 59, 75, 20, 20, 55, 40],
      }]
    };

    var chartOptions = {
      legend: {
        display: true,
        position: 'top',
        labels: {
          boxWidth: 80,
          fontColor: 'black'
        }
      }
    };

    var lineChart = new Chart(speedCanvas, {
      type: 'line',
      data: speedData,
      options: chartOptions
    });
}

function mes(id,id2){
    var monthNames = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
      "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

    var d = new Date();
    document.getElementById(id).innerHTML = "Reporte del Mes: " + monthNames[d.getMonth()]+ '<br><br><button class ="btn btn-primary" id="mes1" >Descargar</button>';
    document.getElementById(id2).innerHTML = '';
    var densityCanvas1 = document.getElementById("densityChart");

    Chart.defaults.global.defaultFontFamily = "Lato";
    Chart.defaults.global.defaultFontSize = 18;

    var densityData1 = {
      label: 'Visitas en el mes de ' + monthNames[d.getMonth()],
      data: [100, 120, 130, 98]
    };

    var barChart1 = new Chart(densityCanvas1, {
      type: 'bar',
      data: {
        labels: ["Semana 1", "semana 2", "Semana 3", "Semana 4"],
        datasets: [densityData1]
      }
    });
}

function year(id,id2){
    var today = new Date();
    var yyyy = today.getFullYear();

    today = yyyy;
    document.getElementById(id).innerHTML = 'Reporte del año: ' + today + ' <br><br><button class ="btn btn-primary" id="año1">Descargar</button>';
    document.getElementById(id2).innerHTML = '';
    var densityCanvas2 = document.getElementById("densityChart");

    Chart.defaults.global.defaultFontFamily = "Lato";
    Chart.defaults.global.defaultFontSize = 18;

    var densityData2 = {
      label: today,
      data: [1200, 1322, 988, 1002, 1203, 1021, 1232, 2001,1952,2123,1222,992]
    };

    var barChart2 = new Chart(densityCanvas2, {
      type: 'bar',
      data: {
        labels: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto","Septiembre","Octubre","Noviembre","Diciembre"],
        datasets: [densityData2]
      }
    });



}

function rango(id,id2){
    document.getElementById(id).innerHTML = 'Reporte de: <input type ="date" class ="date_label">  a  <input type ="date" class ="date_label">' + '<br><br><button class ="btn btn-primary" id="rango1">Descargar</button>';
}

    var fecha = new Date();
    hora = fecha.getHours()+":"+fecha.getMinutes();
    document.getElementById('hora').innerHTML ='Reporte de Usuarios activos a las: ' + hora;


