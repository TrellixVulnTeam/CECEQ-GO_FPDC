# Importar la libreria para URLS
from django.urls import path

# Uso del "punto" para referenciar esta libreria e importar el views
from . import views

urlpatterns = [

    # primero se pondra el nombre de la ruta a llama
    # Se pone el nombre views seguido de un nombre que se le dara
    # Despues se pondra el nombre en este caso login
    path('reporte_1/', views.reporte_1, name='reporte_1'),

]